package com.example.authserver.config;

import com.example.authserver.controllers.UserService;
import com.example.authserver.filters.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserService userService;

    private final JwtFilter jwtFilter;

    public SecurityConfig(UserService userService, JwtFilter jwtFilter)
    {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // allow JWT functions without valid JWT
                .antMatchers("/auth", "/validate").permitAll()
                // Allow user creation without valid JWT
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                // OpenAPI endpoint, enable this and springdocs apidocs value to generate docs with make docs command
//                .antMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService);
    }
}
