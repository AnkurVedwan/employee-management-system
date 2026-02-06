package com.company.main.EmployeeManagement.controller;

import com.company.main.EmployeeManagement.Util.ResponseUtil;
import com.company.main.EmployeeManagement.dto.ApiResponse;
import com.company.main.EmployeeManagement.dto.LeaveBalanceDTO;
import com.company.main.EmployeeManagement.dto.LeaveRequestDTO;
import com.company.main.EmployeeManagement.service.LeaveRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.ReplicateScaleFilter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/request")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("employee/{employeeId}/save")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> saveRequest(@PathVariable Long employeeId, @Valid @RequestBody LeaveRequestDTO leaveRequestDTO, HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave Request Saved Successfully",
                        leaveRequestService.saveRequest(employeeId,leaveRequestDTO),
                        request
                )
        );
    }

    @PatchMapping("/manager/{managerId}/approve/{leaveId}")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> approveLeave(@PathVariable Long managerId,@PathVariable Long leaveId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave Approved Successfully",
                        leaveRequestService.approveLeave(managerId,leaveId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getAll(@PathVariable Long adminId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request fetched successfully",
                        leaveRequestService.getAll(adminId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/pending")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getAllPending(@PathVariable Long adminId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request fetched successfully",
                        leaveRequestService.getAllPending(adminId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/approved")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getAllApproved(@PathVariable Long adminId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request fetched successfully",
                        leaveRequestService.getAllApproved(adminId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/rejected")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getAllRejected(@PathVariable Long adminId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request fetched successfully",
                        leaveRequestService.getAllRejected(adminId),
                        request
                )
        );
    }

    @GetMapping("/employee/{employeeId}/check-status")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getAllById(@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request fetched successfully",
                        leaveRequestService.getAllById(employeeId),
                        request
                )
        );
    }

    @PatchMapping("/employee/{employeeId}/cancel-leave")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> cancelLeave(@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request cancelled successfully",
                        leaveRequestService.cancelLeave(employeeId),
                        request
                )
        );
    }

    @GetMapping("/manager/{managerId}/pending")
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getAllByManager(@PathVariable Long managerId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Leave request fetched successfully",
                        leaveRequestService.getAllByManager(managerId),
                        request
                )
        );
    }

    @PatchMapping("/manager/{managerId}/reject/{leaveId}")
    public LeaveRequestDTO rejectLeave(@RequestBody Map<String,Object> data,@PathVariable Long managerId,@PathVariable Long leaveId)
    {
        return leaveRequestService.rejectLeave(managerId,leaveId,data);
    }

    @PatchMapping("/admin/{adminId}/approve/{leaveId}")
    public LeaveRequestDTO approveLeaveByAdmin(@PathVariable Long adminId,@PathVariable Long leaveId)
    {
        return leaveRequestService.approveLeaveByAdmin(adminId,leaveId);
    }

    @PatchMapping("/admin/{adminId}/reject/{leaveId}")
    public LeaveRequestDTO rejectLeaveByAdmin(@RequestBody Map<String,Object> data,@PathVariable Long adminId,@PathVariable Long leaveId)
    {
        return leaveRequestService.rejectLeaveByAdmin(adminId,leaveId,data);
    }

    @PatchMapping("/admin/leave/{leaveId}")
    public LeaveRequestDTO approveLeaveOfAdmin(@PathVariable Long leaveId)
    {
        return leaveRequestService.approveLeaveOfAdmin(leaveId);
    }
}
