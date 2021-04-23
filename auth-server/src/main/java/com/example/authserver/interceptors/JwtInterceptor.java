package com.example.authserver.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        return true;
//        try {
//            String jwt = request.getHeader("Authorization");
//            jwt = jwt.split("Bearer ")[0];
//            return JwtUtil.validateJwt(jwt);
//        } catch (Exception e)
//        {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
//        }
    }
}
