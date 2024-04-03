package com.example.server.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.server.repositorys.RolesRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RolesRepository rolesRepository;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtUtil.resolveClaims(request);
            if (claims != null && jwtUtil.validateClaims(claims)) {
                String username = claims.getSubject();

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, "", Collections.singleton(new SimpleGrantedAuthority(claims.get("role").toString())));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("Your access is forbidden");
        }
        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("unused")
    private Collection<? extends GrantedAuthority> getRoles() {
        return rolesRepository.findAll().stream().findAny()
        .map(role -> Collections.singleton(new SimpleGrantedAuthority(role.getRoleName()))).get();
    }

}
