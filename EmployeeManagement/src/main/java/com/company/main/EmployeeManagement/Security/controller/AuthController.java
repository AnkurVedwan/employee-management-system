package com.company.main.EmployeeManagement.Security.controller;

import com.company.main.EmployeeManagement.Security.dto.LoginRequestDTO;
import com.company.main.EmployeeManagement.Security.dto.LoginResponseDTO;
import com.company.main.EmployeeManagement.Security.dto.SignUpResponseDTO;
import com.company.main.EmployeeManagement.Security.entity.BlacklistedToken;
import com.company.main.EmployeeManagement.Security.repository.BlacklistedTokenRepository;
import com.company.main.EmployeeManagement.Security.service.AuthService;
import com.company.main.EmployeeManagement.Security.service.AuthUtil;
import com.company.main.EmployeeManagement.dto.AttendanceDTO;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.repository.EmployeeRepository;
import com.company.main.EmployeeManagement.service.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthUtil authUtil;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        attendanceService.saveAttendanceByLogin(loginRequestDTO);
        return authService.login(loginRequestDTO);
    }

//    @PostMapping("/auth/sign-up")
//    public SignUpResponseDTO sign(@RequestBody LoginRequestDTO loginRequestDTO)
//    {
//        return authService.sign(loginRequestDTO);
//    }

    @PatchMapping("/user/{username}/update-password")
    public String change(@PathVariable String username,@RequestBody LoginRequestDTO loginRequestDTO)
    {
        if(authService.updatePass(loginRequestDTO,username))
        {
            return "Password Changed Successfully";
        }

        return "Error Occured Try Again";
    }

    @PostMapping("/logout")
    public AttendanceDTO logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        String token = "";

        if (header != null && header.startsWith("Bearer ")) {

            token = header.substring(7).trim();

            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(token)
                    .expiryDate(authUtil.extractExpiration(token))
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
        }

        String username = authUtil.generateUsernameFromToken(token);

        Employee employee = employeeRepository.findByUsername(username);

        return attendanceService.saveAttendanceCheckOut(employee.getId(), LocalDate.now());
    }
}
