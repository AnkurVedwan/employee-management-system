package com.company.main.EmployeeManagement.service;

import com.company.main.EmployeeManagement.dto.DepartmentDTO;
import com.company.main.EmployeeManagement.entity.Department;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.type.Role;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import com.company.main.EmployeeManagement.repository.DepartmentRepository;
import com.company.main.EmployeeManagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
    }

    public DepartmentDTO saveDepartment(DepartmentDTO departmentDTO, Long adminId) {
        Department department = modelMapper.map(departmentDTO,Department.class);
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        return modelMapper.map(departmentRepository.save(department),DepartmentDTO.class);
    }

    public DepartmentDTO updateDepartment(Map<String, Object> data, Long departmentId, Long adminId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!= Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        data.forEach((key,value)->{
            Field updateField = ReflectionUtils.findField(Department.class,key);
            updateField.setAccessible(true);
            ReflectionUtils.setField(updateField,department,value);
        });
        return modelMapper.map(department,DepartmentDTO.class);
    }

    public List<DepartmentDTO> getAll(Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map((department -> modelMapper.map(department,DepartmentDTO.class))).toList();
    }

    public DepartmentDTO getByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        return modelMapper.map(employee.getDepartment(),DepartmentDTO.class);
    }
}
