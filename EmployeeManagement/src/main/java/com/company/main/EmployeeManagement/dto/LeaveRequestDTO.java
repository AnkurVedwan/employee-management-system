package com.company.main.EmployeeManagement.dto;

import com.company.main.EmployeeManagement.annotation.DateRangeValid;
import com.company.main.EmployeeManagement.annotation.ValidEnum;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.type.LeaveStatus;
import com.company.main.EmployeeManagement.entity.type.LeaveType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DateRangeValid(message = "end-date is smaller than start-date")
public class LeaveRequestDTO {

    private Long id;

    private Employee employee;

    @Future(message = "leave start date must be in future")
    private LocalDate startDate;

    private LocalDate endDate;

    @ValidEnum(enumClass = LeaveType.class)
    private LeaveType leaveType;

    private LeaveStatus status;

    private Integer totalDays;

    private LocalDate appliedAt;

    private String message;
}
