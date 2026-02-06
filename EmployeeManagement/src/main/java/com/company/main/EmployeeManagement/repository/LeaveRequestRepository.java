package com.company.main.EmployeeManagement.repository;

import com.company.main.EmployeeManagement.entity.LeaveRequest;
import com.company.main.EmployeeManagement.entity.type.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findAllByEmployeeIdOrderByAppliedAtDesc(Long employeeId);

    LeaveRequest findByEmployeeIdAndStatus(Long employeeId, LeaveStatus leaveStatus);

    List<LeaveRequest> findAllByStatusAndEmployee_Manager_Id(LeaveStatus leaveStatus, Long managerId);
}