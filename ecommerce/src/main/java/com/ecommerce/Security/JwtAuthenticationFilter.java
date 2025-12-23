package com.ecommerce.Security;

import com.ecommerce.Entity.User;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.extractClaims(token);

            String userName = claims.getSubject();
            String role = claims.get("role", String.class);

            if (userName != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByUserName(userName).orElse(null);
                if (user == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userName, null, List.of(authority)
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.warn("Invalid or expired JWT");
        }

        // 8 Continue request
        filterChain.doFilter(request,response);
    }

}
