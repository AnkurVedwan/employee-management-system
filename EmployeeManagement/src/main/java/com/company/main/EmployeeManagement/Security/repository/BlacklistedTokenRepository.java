package com.company.main.EmployeeManagement.Security.repository;

import com.company.main.EmployeeManagement.Security.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}