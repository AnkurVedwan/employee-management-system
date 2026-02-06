package com.company.main.EmployeeManagement.repository;

import com.company.main.EmployeeManagement.entity.Attendance;
import com.company.main.EmployeeManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByEmployeeId(Long employeeId);

    List<Attendance> findByDateAndEmployee_Department_Id(LocalDate date, Long departmentId);

    Attendance findByDateAndEmployeeId(LocalDate date, Long employeeId);

    List<Attendance> findAllByDateBetweenAndEmployeeId(LocalDate startDate, LocalDate endDate, Long employeeId);

    List<Attendance> findAllByDateAndEmployee_Manager_Id(LocalDate date, Long managerId);

    Attendance findByEmployeeId(Employee employee);
}