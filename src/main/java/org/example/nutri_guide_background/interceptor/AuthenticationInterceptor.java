package org.example.nutri_guide_background.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.nutri_guide_background.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行登录和注册接口
        if (request.getRequestURI().contains("/user/login") || 
            request.getRequestURI().contains("/user/register")) {
            return true;
        }

        // 获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("未登录");
        }

        // 验证token
        if (!jwtUtils.validateToken(token)) {
            throw new RuntimeException("token无效");
        }

        // 设置用户信息到请求属性中
        Long userId = jwtUtils.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        
        return true;
    }
} 