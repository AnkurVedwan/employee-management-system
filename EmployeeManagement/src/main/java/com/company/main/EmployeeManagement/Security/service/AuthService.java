package com.company.main.EmployeeManagement.Security.service;

import com.company.main.EmployeeManagement.Security.dto.LoginRequestDTO;
import com.company.main.EmployeeManagement.Security.dto.LoginResponseDTO;
import com.company.main.EmployeeManagement.Security.dto.SignUpResponseDTO;
import com.company.main.EmployeeManagement.Security.entity.User;
import com.company.main.EmployeeManagement.Security.repository.UserRepository;
import com.company.main.EmployeeManagement.dto.EmployeeDTO;
import com.company.main.EmployeeManagement.exception.InvalidCredentialsException;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();

            String token = authUtil.generateAccessToken(user);

            return new LoginResponseDTO(token, user.getId());

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public SignUpResponseDTO sign(EmployeeDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElse(null);

        if(user != null) throw new ResourceNotFoundException("User Already Exist");

        user = userRepository.save(User.builder()
                .username(loginRequestDTO.getUsername())
                .password(passwordEncoder.encode("pass@123"))
                .roles(Set.of(loginRequestDTO.getRole()))
                .build()
        );

        return new SignUpResponseDTO(user.getId(),user.getUsername());
    }

    public Boolean updatePass(LoginRequestDTO loginRequestDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(loginRequestDTO.getPassword() != null)
        {
            user.setPassword(passwordEncoder.encode(loginRequestDTO.getPassword()));
        }

        userRepository.save(user);
        return true;
    }
}
