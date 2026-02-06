package com.company.main.EmployeeManagement.controller;

import com.company.main.EmployeeManagement.Util.ResponseUtil;
import com.company.main.EmployeeManagement.dto.ApiResponse;
import com.company.main.EmployeeManagement.dto.LeaveBalanceDTO;
import com.company.main.EmployeeManagement.service.LeaveBalanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leavebalance")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @PostMapping("/admin/{adminId}/employee/{employeeId}/save")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> saveBalance(@PathVariable Long adminId, @PathVariable Long employeeId, @Valid @RequestBody LeaveBalanceDTO leaveBalanceDTO, HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave Balance Saved Successfully",
                        leaveBalanceService.saveBalance(employeeId,leaveBalanceDTO,adminId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDTO>>> getAllByDepartment(@PathVariable Long adminId,@PathVariable Long departmentId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave balance fetched successfully",
                        leaveBalanceService.getAllByDepartment(adminId,departmentId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/employee/{employeeId}")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> getByEmployee(@PathVariable Long adminId,@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave balance fetched successfully",
                        leaveBalanceService.getByEmployee(adminId,employeeId),
                        request
                )
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<LeaveBalanceDTO>> getBalance(@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave balance fetched successfully",
                        leaveBalanceService.getBalance(employeeId),
                        request
                )
        );
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<ApiResponse<List<LeaveBalanceDTO>>> getAllByManager(@PathVariable Long managerId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave balance fetched successfully",
                        leaveBalanceService.getAllByManager(managerId),
                        request
                )
        );
    }
}
