package com.company.main.EmployeeManagement.Security.service;

import com.company.main.EmployeeManagement.Security.entity.User;
import com.company.main.EmployeeManagement.Security.repository.BlacklistedTokenRepository;
import com.company.main.EmployeeManagement.Security.repository.UserRepository;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("incomming request {}",request.getRequestURI());

        String requestTokenHeader = request.getHeader("Authorization");

        if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer"))
        {
            filterChain.doFilter(request,response);
            return;
        }

        String token = requestTokenHeader.substring(7).trim();

        if (blacklistedTokenRepository.existsByToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is invalid. Please login again.");
            return;
        }

        String username = authUtil.generateUsernameFromToken(token);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() ==  null)
        {
            User user = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);
    }
}
