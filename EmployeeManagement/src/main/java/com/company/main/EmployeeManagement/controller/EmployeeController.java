package com.company.main.EmployeeManagement.controller;

import com.company.main.EmployeeManagement.Util.ResponseUtil;
import com.company.main.EmployeeManagement.dto.ApiResponse;
import com.company.main.EmployeeManagement.dto.EmployeeDTO;
import com.company.main.EmployeeManagement.repository.LeaveRequestRepository;
import com.company.main.EmployeeManagement.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.ReplicateScaleFilter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final LeaveRequestRepository leaveRequestRepository;

    public EmployeeController(EmployeeService employeeService,
                              LeaveRequestRepository leaveRequestRepository) {
        this.employeeService = employeeService;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @PostMapping("/admin/{adminId}/department/{departmentId}")  // adding manager
    public ResponseEntity<ApiResponse<EmployeeDTO>> saveManager(@PathVariable Long departmentId, @PathVariable Long adminId,@Valid @RequestBody EmployeeDTO employeeDTO, HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Manager saved successfully",
                        employeeService.saveManager(departmentId,adminId,employeeDTO),
                        request
                )
        );
    }

    @PostMapping("/admin/{adminId}/department/{departmentId}/manager/{managerId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> saveEmployee(@PathVariable Long adminId, @PathVariable Long departmentId, @PathVariable Long managerId,@Valid @RequestBody EmployeeDTO employeeDTO,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employee saved successfully",
                        employeeService.saveEmployee(adminId,departmentId,managerId,employeeDTO),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByDepartment(@PathVariable Long adminId,@PathVariable Long departmentId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employees fetched successfully",
                        employeeService.getEmployeesByDepartment(adminId,departmentId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/department/{departmentId}/manager/{managerId}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByManager(@PathVariable Long adminId,@PathVariable Long departmentId,@PathVariable Long managerId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employees fetched successfully",
                        employeeService.getEmployeesByManager(adminId,departmentId,managerId),
                        request
                )
        );
    }

    @PatchMapping("/admin/{adminId}/employee/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateStatus(@Valid @RequestBody Map<String,Object> data, @PathVariable Long adminId, @PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employee updated successfully",
                        employeeService.updateStatus(data,adminId,employeeId),
                        request
                )
        );
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployee(@PathVariable Long employeeId, HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employee Fetched Successfully",
                        employeeService.getEmployee(employeeId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/employee/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeByAdmin(@PathVariable Long adminId,@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employee fetched successfully",
                        employeeService.getEmployeeBYAdmin(adminId,employeeId),
                        request
                )
        );
    }

    @GetMapping("/manager/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getManagerId(@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Manager fetched successfully",
                        employeeService.getManagerId(employeeId),
                        request
                )
        );
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllByManager(@PathVariable Long managerId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employees fetched successfully",
                        employeeService.getAllByManager(managerId),
                        request
                )
        );
    }
}
