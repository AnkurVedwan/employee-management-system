package com.company.main.EmployeeManagement.repository;

import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    LeaveBalance findByEmployeeId(Long employeeId);

    List<LeaveBalance> findAllByEmployee_Department_Id(Long departmentId);

    List<LeaveBalance> findAllByEmployee_Manager_Id(Long managerId);
}