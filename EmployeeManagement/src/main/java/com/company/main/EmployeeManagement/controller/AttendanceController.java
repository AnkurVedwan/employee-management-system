package com.company.main.EmployeeManagement.controller;

import com.company.main.EmployeeManagement.Util.ResponseUtil;
import com.company.main.EmployeeManagement.dto.ApiResponse;
import com.company.main.EmployeeManagement.dto.AttendanceDTO;
import com.company.main.EmployeeManagement.dto.EmployeeDTO;
import com.company.main.EmployeeManagement.service.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("employee/{employeeId}/check-in")
    public ResponseEntity<ApiResponse<AttendanceDTO>> saveAttendance(@PathVariable Long employeeId, @RequestBody AttendanceDTO attendanceDTO, HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employee checked-in Successfully",
                        attendanceService.saveAttendance(employeeId,attendanceDTO),
                        request
                )
        );
    }

    @PatchMapping("/employee/{employeeId}/check-out/date")
    public ResponseEntity<ApiResponse<AttendanceDTO>> saveAttendanceCheckOut(@PathVariable Long employeeId,@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Employee checked-out Successfully",
                        attendanceService.saveAttendanceCheckOut(employeeId,date),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getAllAttendanceByEmployee(@PathVariable Long adminId,@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance of employee fetched successfully",
                        attendanceService.getAllAttendanceByEmployee(adminId,employeeId),
                        request
                )
        );
    }

    @GetMapping("/admin/{adminId}/date")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getAllAttendanceByDate(@PathVariable Long adminId,@RequestParam(value = "date")
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
                                                      @RequestParam(value = "departmentId") Long departmentId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance of employees fetched successfully by specific date",
                        attendanceService.getAllAttendanceByDate(date,departmentId,adminId),
                        request
                )
        );
    }

    @GetMapping("/employee/{employeeId}/date")
    public ResponseEntity<ApiResponse<AttendanceDTO>> getByEmployee(@PathVariable Long employeeId,
                                       @RequestParam(value = "date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance of Employee fetched Successfully on specific date",
                        attendanceService.getByEmployee(employeeId,date),
                        request
                )
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getAllByEmployee(@PathVariable Long employeeId,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance Fetched Successfully",
                        attendanceService.getAllByEmployee(employeeId),
                        request
                )
        );
    }

    @GetMapping("/employee/{employeeId}/month")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getByMonth(@PathVariable Long employeeId,
                                          @RequestParam(value = "year") int year,
                                          @RequestParam(value = "month") int month,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance Fetched Successfully By Month",
                        attendanceService.getByMonth(employeeId,year,month),
                        request
                )
        );
    }

    @GetMapping("/manager/{managerId}/date")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getAllByManager(@PathVariable Long managerId,@RequestParam(value = "date")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance Fetched Successfully on specific date",
                        attendanceService.getAllByManager(managerId,date),
                        request
                )
        );
    }

    @GetMapping("/manager/{managerId}/employee/{employeeId}/date")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getByMonthAndManager(@PathVariable Long managerId,
                                          @PathVariable Long employeeId,
                                          @RequestParam(value = "year") int year,
                                          @RequestParam(value = "month") int month,HttpServletRequest request)
    {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance Fetched Successfully by month",
                        attendanceService.getByMonthAndManager(managerId,employeeId,year,month),
                        request
                )
        );
    }
}
