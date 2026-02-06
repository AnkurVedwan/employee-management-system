package com.company.main.EmployeeManagement.service;

import com.company.main.EmployeeManagement.dto.LeaveBalanceDTO;
import com.company.main.EmployeeManagement.dto.LeaveRequestDTO;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.LeaveBalance;
import com.company.main.EmployeeManagement.entity.type.Role;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import com.company.main.EmployeeManagement.repository.DepartmentRepository;
import com.company.main.EmployeeManagement.repository.EmployeeRepository;
import com.company.main.EmployeeManagement.repository.LeaveBalanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepository;

    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper, DepartmentRepository departmentRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.departmentRepository = departmentRepository;
    }

    public LeaveBalanceDTO saveBalance(Long employeeId, LeaveBalanceDTO leaveBalanceDTO, Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!= Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        LeaveBalance leaveBalance = modelMapper.map(leaveBalanceDTO,LeaveBalance.class);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        leaveBalance.setEmployee(employee);
        return modelMapper.map(leaveBalanceRepository.save(leaveBalance),LeaveBalanceDTO.class);
    }

    public List<LeaveBalanceDTO> getAllByDepartment(Long adminId, Long departmentId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!= Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        List<LeaveBalance> leaveBalances = leaveBalanceRepository.findAllByEmployee_Department_Id(departmentId);
        return leaveBalances.stream().map((leaveBalance -> modelMapper.map(leaveBalance,LeaveBalanceDTO.class))).toList();
    }

    public LeaveBalanceDTO getByEmployee(Long adminId, Long employeeId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!= Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeId(employeeId);
        if(leaveBalance==null)
        {
            throw new ResourceNotFoundException("Employee Not Found");
        }
        return modelMapper.map(leaveBalance,LeaveBalanceDTO.class);
    }

    public LeaveBalanceDTO getBalance(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeId(employeeId);
        return modelMapper.map(leaveBalance,LeaveBalanceDTO.class);
    }

    public List<LeaveBalanceDTO> getAllByManager(Long managerId) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        List<LeaveBalance> leaveBalances = leaveBalanceRepository.findAllByEmployee_Manager_Id(managerId);
        return leaveBalances.stream().map((element) -> modelMapper.map(element, LeaveBalanceDTO.class)).toList();
    }
}
