package com.company.main.EmployeeManagement.Security;

import com.company.main.EmployeeManagement.Security.service.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    {
        httpSecurity.csrf((csrf)->csrf.disable())
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/api/employee/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/employee/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/attendance/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/attendance/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/department/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/leave-balance/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/leave-balance/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/request/admin/**").hasRole("ADMIN")
                        .requestMatchers("api/request/manager/**").hasRole("MANAGER")
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
    {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
