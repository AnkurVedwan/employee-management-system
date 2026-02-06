package com.company.main.EmployeeManagement.repository;

import com.company.main.EmployeeManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDepartmentId(Long departmentId);

    List<Employee> findAllByManagerId(Long managerId);
}