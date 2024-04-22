package com.loong.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.loong.constant.JwtClaimsConstant;
import com.loong.context.BaseContext;
import com.loong.properties.JwtProperties;
import com.loong.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                
        // if (!(handler instanceof HandlerMethod)) {
        //     // 当前拦截到的不是动态方法，直接放行
        //     return true;
        // }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        // 2、校验令牌
        try {
            log.info("jwt check :{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long current_id;
            current_id = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("current user id: ", current_id);
            BaseContext.setCurrentId(current_id);
            // 3、通过，放行
            return true;
        } catch (Exception ex) {
            // 4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }

}
