package com.bsks.api.aspect;

import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.api.result.ResultException;
import com.bsks.api.utils.ApplicationUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
@Aspect
public class JWTHasRoleAspect {

    private static Boolean isOpen = false;

    @Value("${JWTHasRole}")
    public  void JWTHasRoleIsOpen(Boolean isOpen) {
        if (isOpen != null){
            this.isOpen = isOpen;
        }
    }

    @Pointcut( "@annotation(com.bsks.api.annotation.JWTHasRole)")
    public void hasRoleAnnotationPointcut(){};

    /**
     * 前置通知：验证jwt是否合法，并且判断权限
     * @param joinPoint
     */
    @Before("hasRoleAnnotationPointcut()")
    public void doBefore(JoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        // 没有开启token鉴权
        if (!isOpen){
            return;
        }
        //1. 获取请求头中的请求，获取token
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        System.out.println("权限认证注解获取token"+token);
        if (token == null || "".equals(token)){
            throw new ResultException(ResultCode.NoPermission);
        }
        //2. 获得接口上的注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        JWTHasRole annotation = method.getAnnotation(JWTHasRole.class);//获得该注解
        // 获得注解上的值
        String roleName = annotation.roleName();
        Class<?> oauthServiceClass = annotation.oauthService();
        String getValueMethodName = annotation.getValue();
        //3. 通过反射调用认证服务的获取value的方法获取token的角色名
        Method getValueMethod = ReflectionUtils.findMethod(oauthServiceClass,getValueMethodName,String.class,String.class);
        Result valueResult = (Result) getValueMethod.invoke(ApplicationUtils.getBean(oauthServiceClass),token,"roleName");
        if (valueResult.getCode() != 20000){
            throw new ResultException(valueResult.getCode(),valueResult.getMessage());
        }
        String roleNameResult = (String) valueResult.getData();
        if (!roleName.equals(roleNameResult)){
            throw new ResultException(ResultCode.NoPermission);
        }

    }
}
