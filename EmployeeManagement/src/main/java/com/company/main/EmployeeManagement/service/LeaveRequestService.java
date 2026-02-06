package com.company.main.EmployeeManagement.service;

import com.company.main.EmployeeManagement.dto.LeaveRequestDTO;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.LeaveBalance;
import com.company.main.EmployeeManagement.entity.LeaveRequest;
import com.company.main.EmployeeManagement.entity.type.LeaveStatus;
import com.company.main.EmployeeManagement.entity.type.LeaveType;
import com.company.main.EmployeeManagement.entity.type.Role;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import com.company.main.EmployeeManagement.repository.EmployeeRepository;
import com.company.main.EmployeeManagement.repository.LeaveBalanceRepository;
import com.company.main.EmployeeManagement.repository.LeaveRequestRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository, LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public LeaveRequestDTO saveRequest(Long employeeId, LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest leaveRequest = modelMapper.map(leaveRequestDTO,LeaveRequest.class);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        leaveRequest.setEmployee(employee);
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setTotalDays((int)(
                ChronoUnit.DAYS.between(
                        leaveRequest.getStartDate(),
                        leaveRequest.getEndDate()
                ) + 1));
        return modelMapper.map(leaveRequestRepository.save(leaveRequest),LeaveRequestDTO.class);
    }

    @Transactional
    public LeaveRequestDTO approveLeave(Long managerId, Long leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId).orElseThrow(()->new ResourceNotFoundException("Leave Not Found"));
        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeId(leaveRequest.getEmployee().getId());
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not found"));
        if(leaveRequest.getStatus()!=LeaveStatus.PENDING)
        {
            throw new ResourceNotFoundException("leave status is not pending");
        }
        if(manager!=leaveRequest.getEmployee().getManager())
        {
            throw new ResourceNotFoundException("Invalid manager");
        }
        if(leaveRequest.getLeaveType().equals(LeaveType.SICK))
        {
            int diff = leaveBalance.getSickLeave()-leaveRequest.getTotalDays();
            if(diff<0)
            {
                leaveRequest.setStatus(LeaveStatus.REJECTED);
                leaveRequest.setMessage("insufficient leave balance");
                return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
            }
            leaveBalance.setSickLeave(diff);
        }
        else
        {
            int diff = leaveBalance.getCasualLeave()-leaveRequest.getTotalDays();
            if(diff<0)
            {
                leaveRequest.setStatus(LeaveStatus.REJECTED);
                leaveRequest.setMessage("insufficient leave balance");
                return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
            }
            leaveBalance.setCasualLeave(diff);
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setMessage("your leave is approved");

        leaveRequestRepository.save(leaveRequest);
        leaveBalanceRepository.save(leaveBalance);

        return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
    }

    public List<LeaveRequestDTO> getAll(Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        return leaveRequests.stream().map((element) -> modelMapper.map(element, LeaveRequestDTO.class)).toList();
    }

    public List<LeaveRequestDTO> getAllPending(Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        List<LeaveRequest> pendingReq = new ArrayList<>();
        leaveRequests.forEach(leaveRequest -> {
            if(leaveRequest.getStatus().equals(LeaveStatus.PENDING))
            {
                pendingReq.add(leaveRequest);
            }
        });
        return pendingReq.stream().map((req)->modelMapper.map(req,LeaveRequestDTO.class)).toList();
    }

    public List<LeaveRequestDTO> getAllApproved(Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        List<LeaveRequest> pendingReq = new ArrayList<>();
        leaveRequests.forEach(leaveRequest -> {
            if(leaveRequest.getStatus().equals(LeaveStatus.APPROVED))
            {
                pendingReq.add(leaveRequest);
            }
        });
        return pendingReq.stream().map((req)->modelMapper.map(req,LeaveRequestDTO.class)).toList();
    }

    public List<LeaveRequestDTO> getAllRejected(Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        List<LeaveRequest> pendingReq = new ArrayList<>();
        leaveRequests.forEach(leaveRequest -> {
            if(leaveRequest.getStatus().equals(LeaveStatus.REJECTED))
            {
                pendingReq.add(leaveRequest);
            }
        });
        return pendingReq.stream().map((req)->modelMapper.map(req,LeaveRequestDTO.class)).toList();
    }

    public List<LeaveRequestDTO> getAllById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAllByEmployeeIdOrderByAppliedAtDesc(employeeId);
        return leaveRequests.stream().map((leaveRequest -> modelMapper.map(leaveRequest,LeaveRequestDTO.class))).toList();
    }

    public LeaveRequestDTO cancelLeave(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        LeaveRequest leaveRequest = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId,LeaveStatus.PENDING);
        if(leaveRequest==null)
        {
            throw new ResourceNotFoundException("No Pending Leave To Cancel");
        }
        leaveRequest.setStatus(LeaveStatus.CANCELLED);
        leaveRequest.setMessage("leave cancelled successfully");
        return modelMapper.map(leaveRequestRepository.save(leaveRequest),LeaveRequestDTO.class);
    }

    public List<LeaveRequestDTO> getAllByManager(Long managerId) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not found"));
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAllByStatusAndEmployee_Manager_Id(LeaveStatus.PENDING,managerId);
        return leaveRequests.stream().map((element) -> modelMapper.map(element, LeaveRequestDTO.class)).toList();
    }

    public LeaveRequestDTO rejectLeave(Long managerId, Long leaveId, Map<String, Object> data) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId).orElseThrow(()->new ResourceNotFoundException("leave not found"));
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        if(manager!=leaveRequest.getEmployee().getManager())
        {
            throw new ResourceNotFoundException("Invalid Manager");
        }
        if(leaveRequest.getStatus()!=LeaveStatus.PENDING)
        {
            throw new ResourceNotFoundException("leave status is not pending");
        }
        data.forEach((key,value)->{
            Field field = ReflectionUtils.findField(LeaveRequest.class,key);
            field.setAccessible(true);
            ReflectionUtils.setField(field,leaveRequest,value);
        });
        leaveRequest.setStatus(LeaveStatus.REJECTED);
        return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
    }

    public LeaveRequestDTO approveLeaveByAdmin(Long adminId, Long leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId).orElseThrow(()->new ResourceNotFoundException("leave not found"));
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("admin not found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("invalid admin");
        }
        if(leaveRequest.getEmployee().getRole()!=Role.MANAGER)
        {
            throw new ResourceNotFoundException("this is not a manager");
        }
        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setMessage("leave approved by admin");
        return modelMapper.map(leaveRequestRepository.save(leaveRequest),LeaveRequestDTO.class);
    }

    public LeaveRequestDTO rejectLeaveByAdmin(Long adminId, Long leaveId, Map<String, Object> data) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId).orElseThrow(()->new ResourceNotFoundException("leave not found"));
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("admin not found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("invalid admin");
        }
        if(leaveRequest.getEmployee().getRole()!=Role.MANAGER)
        {
            throw new ResourceNotFoundException("this is not a manager");
        }
        data.forEach((key,value)->{
            Field field = ReflectionUtils.findField(LeaveRequest.class,key);
            field.setAccessible(true);
            ReflectionUtils.setField(field,leaveRequest,value);
        });
        leaveRequest.setStatus(LeaveStatus.REJECTED);
        return modelMapper.map(leaveRequestRepository.save(leaveRequest),LeaveRequestDTO.class);
    }

    public LeaveRequestDTO approveLeaveOfAdmin(Long leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId).orElseThrow(()->new ResourceNotFoundException("leave not found"));
        Employee admin = leaveRequest.getEmployee();
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("invalid admin");
        }
        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeId(admin.getId());
        if(leaveRequest.getLeaveType().equals(LeaveType.SICK))
        {
            int diff = leaveBalance.getSickLeave()-leaveRequest.getTotalDays();
            if(diff<0)
            {
                leaveRequest.setStatus(LeaveStatus.REJECTED);
                leaveRequest.setMessage("insufficient leave balance");
                return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
            }
            leaveBalance.setSickLeave(diff);
        }
        else
        {
            int diff = leaveBalance.getCasualLeave()-leaveRequest.getTotalDays();
            if(diff<0)
            {
                leaveRequest.setStatus(LeaveStatus.REJECTED);
                leaveRequest.setMessage("insufficient leave balance");
                return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
            }
            leaveBalance.setCasualLeave(diff);
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setMessage("your leave is approved");

        leaveRequestRepository.save(leaveRequest);
        leaveBalanceRepository.save(leaveBalance);

        return modelMapper.map(leaveRequest,LeaveRequestDTO.class);
    }
}
