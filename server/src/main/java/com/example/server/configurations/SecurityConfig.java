package com.example.server.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.server.constants.RolesConstant;
import com.example.server.exception.CustomAccessDeniedException;
import com.example.server.exception.CustomUnAuthorizeException;
import com.example.server.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomUnAuthorizeException())
                        .accessDeniedHandler(new CustomAccessDeniedException()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register",
                                "/products/get-all-products",
                                "/products/get-product/{id}",
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**")
                        .permitAll()
                        .requestMatchers("/upload-user-image",
                                "/cart/**",
                                "/transaction/**",
                                "/auth/edit-profile",
                                "/auth/profile"
                        ).hasAuthority(RolesConstant.USER_ROLE)
                        .requestMatchers("/products/**",
                                "/category/**",
                                "/auth/profile"
                        ).hasAuthority(RolesConstant.ADMIN_ROLE)
                        .anyRequest().authenticated())
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
