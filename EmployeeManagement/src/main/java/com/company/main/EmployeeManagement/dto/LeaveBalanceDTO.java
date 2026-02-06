package com.company.main.EmployeeManagement.dto;

import com.company.main.EmployeeManagement.entity.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceDTO {

    private Long id;

    private Employee employee;

    @NotNull(message = "casual leaves cannot be null")
    private Integer casualLeave;

    @NotNull(message = "sick leaves cannot be null")
    private Integer sickLeave;
}
