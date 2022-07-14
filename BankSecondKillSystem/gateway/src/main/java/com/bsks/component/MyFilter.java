package com.bsks.component;

import com.bsks.result.Result;
import com.bsks.result.ResultCode;
import com.bsks.result.ResultException;
import com.bsks.service.OauthService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 拦截器：拦截非法token的请求
 */
@Component
@Slf4j
public class MyFilter implements Ordered, GlobalFilter {

    String[] loginPaths = {
            "/account/admin/getCode", "/account/user/getCode",
            "/account/admin/codeLogin","/account/user/codeLogin",
            "/account/admin/passwordLogin","/account/user/passwordLogin",
            "/account/user/register"
    };

    @Autowired
    private OauthService oauthService;


    // 是否开启token验证
    @Value("${jwtIsOpen}")
    private boolean jwtIsOpen;

    @Override
    public int getOrder() {
        return 0;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        System.out.println("请求路径："+request.getPath().toString());
        //token验证未开启，swagger请求,放行
        if (!jwtIsOpen || request.getURI().getPath().contains("/v2/api-docs")) {
            return chain.filter(exchange);
        }
        // 不拦截登录请求
        for (String loginPath : loginPaths) {
            if (loginPath.equals(request.getPath().toString())){
                return chain.filter(exchange);
            }
        }
        //获取token

        String token = request.getHeaders().getFirst("Authorization");
        log.info("截获请求的token: "+token);
        if (token == null || "".equals(token)){
            throw new ResultException(ResultCode.NoPermission);
        }
        Result result = oauthService.verifyToken(token);
        System.out.println(result.toString());
        if (result.getCode() != 20000){
            throw new ResultException(ResultCode.OauthServerError);
        }
        boolean b = (boolean) result.getData();
        if (!b){
            throw new ResultException(ResultCode.NoPermission);
        }
        return chain.filter(exchange);
    }
}
