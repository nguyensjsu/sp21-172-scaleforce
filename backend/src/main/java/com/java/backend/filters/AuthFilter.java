package com.java.backend.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.responses.AuthValidateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class AuthFilter extends OncePerRequestFilter
{
    private static String AUTH_SERVER_URL;

    public AuthFilter(@Value("${auth.url}") String authServerURL)
    {
        AUTH_SERVER_URL = authServerURL;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        try
        {
            HttpResponse<String> authRes = validateJwt(request);

            ObjectMapper mapper = new ObjectMapper();
            AuthValidateResponse authBody = mapper.readValue(authRes.body(), AuthValidateResponse.class);

            for (String permission : authBody.getPermissions())
                authorities.add(new SimpleGrantedAuthority(permission));

            // get authentication token
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken( authBody.getEmail(), null, authorities);

            // set the details
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // tell security context request is authenticated
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e)
        {
            logger.warn("Error occurred: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    private HttpResponse<String> validateJwt(HttpServletRequest request) throws IOException, InterruptedException
    {
        HttpClient client = HttpClient.newHttpClient();

        java.net.http.HttpRequest authReq = HttpRequest.newBuilder(
                URI.create(AUTH_SERVER_URL + "/validate"))
                .header("Content-Type", "application/json")
                .header("Authorization", request.getHeader("Authorization"))
                .POST(HttpRequest.BodyPublishers.ofString("auth"))
                .build();

        HttpResponse<String> authRes = client.send(authReq, HttpResponse.BodyHandlers.ofString());

        logger.info("JWT validation attempted with response code " + authRes.statusCode());
        if (authRes.statusCode() != 200)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        return authRes;
    }
}
