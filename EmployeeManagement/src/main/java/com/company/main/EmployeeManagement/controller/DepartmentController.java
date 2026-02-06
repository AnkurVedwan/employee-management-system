package com.company.main.EmployeeManagement.controller;

import com.company.main.EmployeeManagement.Util.ResponseUtil;
import com.company.main.EmployeeManagement.dto.ApiResponse;
import com.company.main.EmployeeManagement.dto.DepartmentDTO;
import com.company.main.EmployeeManagement.entity.Department;
import com.company.main.EmployeeManagement.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> saveDepartment(@Valid @RequestBody DepartmentDTO departmentDTO, @PathVariable Long adminId, HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Department Saved Successfully",
                        departmentService.saveDepartment(departmentDTO,adminId),
                        request
                )
        );
    }

    @PatchMapping("{departmentId}/admin/{adminId}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> updateDepartment(@Valid @RequestBody Map<String,Object> data,@PathVariable Long departmentId,@PathVariable Long adminId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Department Updated Successfully",
                        departmentService.updateDepartment(data,departmentId,adminId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> getAll(@PathVariable Long adminId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "All Department Fetched Successfully",
                        departmentService.getAll(adminId),
                        request
                )
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> getByEmployee(@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Department By Employee Fetched Successfully",
                        departmentService.getByEmployee(employeeId),
                        request
                )
        );
    }
}
