package com.java.backend.filters;

import com.java.backend.config.MutableHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);

        mutableRequest.putHeader("x-custom-header", "custom value");
        filterChain.doFilter(mutableRequest, response);
    }
}
