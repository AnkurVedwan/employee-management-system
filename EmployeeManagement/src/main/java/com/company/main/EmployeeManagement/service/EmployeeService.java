package com.company.main.EmployeeManagement.service;

import com.company.main.EmployeeManagement.EmployeeManagementApplication;
import com.company.main.EmployeeManagement.dto.EmployeeDTO;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.departmentRepository = departmentRepository;
    }

    public EmployeeDTO saveManager(Long departmentId, Long adminId, EmployeeDTO employeeDTO) {

        Employee employee = modelMapper.map(employeeDTO,Employee.class);
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));
        employee.setDepartment(department);
        return modelMapper.map(employeeRepository.save(employee),EmployeeDTO.class);
    }

    public EmployeeDTO saveEmployee(Long adminId,Long departmentId, Long managerId, EmployeeDTO employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO,Employee.class);
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));
        employee.setDepartment(department);
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        employee.setManager(manager);
        return modelMapper.map(employeeRepository.save(employee),EmployeeDTO.class);
    }

    public List<EmployeeDTO> getEmployeesByDepartment(Long adminId, Long departmentId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));
        List<Employee> employees = employeeRepository.findAllByDepartmentId(departmentId);
        return employees.stream().map((employee -> modelMapper.map(employee,EmployeeDTO.class))).toList();
    }

    public List<EmployeeDTO> getEmployeesByManager(Long adminId, Long departmentId, Long managerId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department Not Found"));
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        List<Employee> employees = employeeRepository.findAllByManagerId(managerId);
        return employees.stream().map((employee -> modelMapper.map(employee,EmployeeDTO.class))).toList();
    }

    public EmployeeDTO updateStatus(Map<String, Object> data, Long adminId, Long employeeId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(()->new ResourceNotFoundException("Admin Not Found"));
        if(admin.getRole()!=Role.ADMIN)
        {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found"));

        data.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Employee.class, key);
            if (field == null) return;

            field.setAccessible(true);

            if (field.getType().isEnum()) {
                Object enumValue = Enum.valueOf(
                        (Class<Enum>) field.getType(),
                        value.toString()
                );
                ReflectionUtils.setField(field, employee, enumValue);
            } else {
                ReflectionUtils.setField(field, employee, value);
            }
        });
        if(employee.getRole().equals(Role.MANAGER))
        {
            employee.setManager(null);
        }
        return modelMapper.map(employeeRepository.save(employee), EmployeeDTO.class);
    }

    public EmployeeDTO getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found with id : "+employeeId));
        return modelMapper.map(employee,EmployeeDTO.class);
    }

    public EmployeeDTO getEmployeeBYAdmin(Long adminId, Long employeeId) {
        return getEmployee(employeeId);
    }

    public EmployeeDTO getManagerId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        return modelMapper.map(employee.getManager(),EmployeeDTO.class);
    }

    public List<EmployeeDTO> getAllByManager(Long managerId) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        List<Employee> employees = employeeRepository.findAllByManagerId(managerId);
        return employees.stream().map((employee -> modelMapper.map(employee,EmployeeDTO.class))).toList();
    }
}
