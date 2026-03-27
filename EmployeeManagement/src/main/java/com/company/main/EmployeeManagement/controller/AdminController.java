package com.company.main.EmployeeManagement.controller;

import com.company.main.EmployeeManagement.Security.service.AuthService;
import com.company.main.EmployeeManagement.Util.ResponseUtil;
import com.company.main.EmployeeManagement.dto.ApiResponse;
import com.company.main.EmployeeManagement.dto.EmployeeDTO;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.type.Role;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import com.company.main.EmployeeManagement.repository.EmployeeRepository;
import com.company.main.EmployeeManagement.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/create-admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmployeeService employeeService;
    private final AuthService authService;
    private final EmployeeRepository employeeRepository;

    @PostMapping("/department/{departmentId}/save")
    public ResponseEntity<ApiResponse<EmployeeDTO>> saveAdmin(@PathVariable Long departmentId, @RequestBody EmployeeDTO employeeDTO, HttpServletRequest request)
    {
        boolean present = employeeRepository.existsByRole(Role.ADMIN);
        if(present == true) throw new ResourceNotFoundException("Admin Already Exists");

        authService.sign(employeeDTO);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Admin Created Successfully",
                        employeeService.saveAdmin(departmentId,employeeDTO),
                        request
                )
        );
    }
}
