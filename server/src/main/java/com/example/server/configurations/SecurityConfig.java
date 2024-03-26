package com.example.server.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.server.exception.CustomAccessDeniedException;
import com.example.server.exception.CustomUnAuthorizeException;
import com.example.server.jwt.JwtFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    JwtFilter jwtFilter;
    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex -> ex
        .authenticationEntryPoint(new CustomUnAuthorizeException())
        .accessDeniedHandler(new CustomAccessDeniedException()))
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register",
                    "/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**")
                .permitAll()
                .anyRequest().authenticated())
        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
