package com.sparta.pinterest_clone.security;

import com.sparta.pinterest_clone.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "Jwt VerificationFilter")
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl serviceImpl;

    public JwtVerificationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl serviceImpl) {
        this.jwtUtil = jwtUtil;
        this.serviceImpl = serviceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token)) {
            token = jwtUtil.substringJwt(token);
            if (!jwtUtil.validateJwt(token)) {
                log.error("유효성 검증에 실패했습니다.");
                return;
            }

            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.getSubject();
            log.info("username: {}", username);
            try {
                setAuthentication(username);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = createAuthentication(username);
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = serviceImpl.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
