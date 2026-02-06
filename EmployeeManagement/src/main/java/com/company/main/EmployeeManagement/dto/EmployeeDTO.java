package com.company.main.EmployeeManagement.dto;

import com.company.main.EmployeeManagement.annotation.ValidEnum;
import com.company.main.EmployeeManagement.entity.Department;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.type.EmployeeStatus;
import com.company.main.EmployeeManagement.entity.type.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Name Cannot Be Blank")
    private String name;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "email should be in format")
    private String email;

    @ValidEnum(enumClass = Role.class,message = "Role must be [MANAGER,ADMIN,EMPLOYEE]")
    private Role role;

    @ValidEnum(enumClass = EmployeeStatus.class,message = "employee status must be [ACTIVE,INACTIVE]")
    private EmployeeStatus status;

    private LocalDate joiningDate;

    private Department department;

    private Employee manager;
}
