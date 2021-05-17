package com.java.backend.config;

import com.java.backend.filters.AuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final AuthFilter authFilter;

    public SecurityConfig(AuthFilter authFilter)
    {
        this.authFilter = authFilter;
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
                // OpenAPI endpoint
//                .antMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
//                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        httpSecurity.addFilterBefore(new AuthFilter(), AuthFilter.class);
//        http.addFilterBefore(new JwtFilter(),UsernamePasswordAuthenticationFilter.class).authorizeRequests();
//        http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class).authorizeRequests();

    }
}
