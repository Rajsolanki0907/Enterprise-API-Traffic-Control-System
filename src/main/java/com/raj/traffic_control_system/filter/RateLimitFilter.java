package com.raj.traffic_control_system.filter;

import com.raj.traffic_control_system.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip login endpoint
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String username =
                (String) request.getAttribute("username");

        String role =
                (String) request.getAttribute("role");

        boolean allowed =
                rateLimiterService.allowRequest(
                        username,
                        role
                );

        if (!allowed) {

            response.setStatus(429);

            response.getWriter()
                    .write("Rate limit exceeded!");

            return;
        }

        filterChain.doFilter(request, response);
    }
}