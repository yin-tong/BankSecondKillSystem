package com.bsks.api.aspect;

import com.bsks.api.annotation.AccessLimiter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Component
@Aspect
public class AccessLimiterAspect {

    private static Boolean isOpen = false;

    @Value("${AccessLimiter}")
    public  void AccessLimiterIsOpen(Boolean isOpen) {
        if (isOpen != null){
            this.isOpen = isOpen;
        }
    }

    @Qualifier("apiRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    // 接口防刷的lua脚本
    @Qualifier("limitScript")
    @Autowired
    private DefaultRedisScript<String> limitScript;

    @Pointcut( "@annotation(com.bsks.api.annotation.AccessLimiter)")
    public void limitAnnotationPointcut(){};

    /**
     * 前置通知：验证jwt是否合法，并且判断权限
     * @param joinPoint
     */
    @Before("limitAnnotationPointcut()")
    public void doBefore(JoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        // 没有开启接口防刷
        if (!isOpen) {
            return;
        }
        //1. 获取请求头中的请求，获取token
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        System.out.println("接口防刷注解获取token" + token);
        if (token == null || "".equals(token)) {
            throw new ResultException(ResultCode.NoPermission);
        }
        //2. 获得接口上的注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // 获得注解
        AccessLimiter annotation = method.getAnnotation(AccessLimiter.class);
        // 获得注解中的值
        String limitCount = String.valueOf(annotation.limitCount());
        String limitTime = String.valueOf(annotation.limitTime());
        Class<?> oauthServiceClass = annotation.oauthService();
        String getValueMethodName = annotation.getValue();
        //3. 通过反射调用认证服务的获取value的方法获取账户id
        Method getValueMethod = ReflectionUtils.findMethod(oauthServiceClass, getValueMethodName, String.class, String.class);
        Result valueResult = (Result) getValueMethod.invoke(ApplicationUtils.getBean(oauthServiceClass), token, "accountId");
        if (valueResult.getCode() != 20000) {
            throw new ResultException(valueResult.getCode(), valueResult.getMessage());
        }
        // 从token中获取账户id
        String accountId = (String) valueResult.getData();
        // 将访问信息存入redis
        String key = method.getName()+ ":" + accountId;
        System.out.println("key: "+key);
        Object is = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                limitScript.getScriptAsString().getBytes(),
                ReturnType.INTEGER,
                1,
                key.getBytes(StandardCharsets.UTF_8),
                limitTime.getBytes(StandardCharsets.UTF_8),
                limitCount.getBytes(StandardCharsets.UTF_8))
        );
        System.out.println("is: "+ is);
        if ((Long) is == 0) {
            System.out.println("----达到限制访问数量,方法名:" + method.getName() + "账户id: " + accountId);
            throw new ResultException(ResultCode.OverLimit);
        }
    }
}
