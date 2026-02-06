package com.company.main.EmployeeManagement.dto;

import com.company.main.EmployeeManagement.entity.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDTO {

    private Long id;

    private Employee employee;

    private LocalDate date;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private LocalTime workingHours;
}
