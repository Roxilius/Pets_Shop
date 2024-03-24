package com.example.server.security.jwt;

import java.time.Duration;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.server.models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    private String secretKey = "ssid2024";

    private Long accessTokenValidity = 60*60*1000L;

    private final String TOKEN_HEADER = "Authorization";

    private final String TOKEN_PREFIX = "Bearer ";

    private final JwtParser jwtParser;

    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }
    public String generateToken(Users user) {
        Claims claims = Jwts.claims().setSubject(user.getUserName());
        claims.put("username",user.getUserName());
        claims.put("role",user.getRoles().getRoleName());

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() +
                Duration.ofMinutes(accessTokenValidity).toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
}