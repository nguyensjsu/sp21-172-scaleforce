package com.java.backend.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor
{
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private static String AUTH_SERVER_URL;

    public AuthInterceptor(@Value("${auth.url}") String authServerURL)
    {
        AUTH_SERVER_URL = authServerURL;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception
    {
        try
        {
            HttpClient client = HttpClient.newHttpClient();

            java.net.http.HttpRequest authReq = HttpRequest.newBuilder(
                    URI.create(AUTH_SERVER_URL + "/validate"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", request.getHeader("Authorization"))
                    .POST(HttpRequest.BodyPublishers.ofString("test"))
                    .build();

            HttpResponse<String> authRes = client.send(authReq, HttpResponse.BodyHandlers.ofString());

            logger.info("JWT validation attempted with response code " + authRes.statusCode());
            if (authRes.statusCode() != 200)
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

            return true;
        } catch (Exception e)
        {
            logger.warn("Error occurred: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
