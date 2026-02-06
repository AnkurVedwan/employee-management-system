package com.company.main.EmployeeManagement.service;

import com.company.main.EmployeeManagement.dto.AttendanceDTO;
import com.company.main.EmployeeManagement.entity.Attendance;
import com.company.main.EmployeeManagement.entity.Department;
import com.company.main.EmployeeManagement.entity.Employee;
import com.company.main.EmployeeManagement.entity.type.Role;
import com.company.main.EmployeeManagement.exception.ResourceNotFoundException;
import com.company.main.EmployeeManagement.repository.AttendanceRepository;
import com.company.main.EmployeeManagement.repository.DepartmentRepository;
import com.company.main.EmployeeManagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public AttendanceDTO saveAttendance(Long employeeId, AttendanceDTO attendanceDTO) {
        Attendance attendance = modelMapper.map(attendanceDTO, Attendance.class);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with id : "+employeeId));
        attendance.setEmployee(employee);
        return modelMapper.map(attendanceRepository.save(attendance), AttendanceDTO.class);
    }

    public AttendanceDTO saveAttendanceCheckOut(Long employeeId, LocalDate date) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not found with id : "+employeeId ));

        Attendance attendance = attendanceRepository.findByDateAndEmployeeId(date,employeeId);

        attendance.setCheckOut(LocalTime.now());

// ✅ Correct order: checkIn → checkOut
        Duration duration = Duration.between(
                attendance.getCheckIn(),
                attendance.getCheckOut()
        );

// handle midnight case (optional but recommended)
        if (duration.isNegative()) {
            duration = duration.plusHours(24);
        }

        long totalMinutes = duration.toMinutes();

// ✅ Convert duration → LocalTime
        attendance.setWorkingHours(LocalTime.of(
                (int) (totalMinutes / 60),
                (int) (totalMinutes % 60)
        ));

        return modelMapper.map(attendanceRepository.save(attendance), AttendanceDTO.class);
    }

    public List<AttendanceDTO> getAllAttendanceByEmployee(Long adminId, Long employeeId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Admin Not Found with id : "+adminId));
        if (admin.getRole() != Role.ADMIN) {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with id : "+employeeId));
        List<Attendance> attendances = attendanceRepository.findAllByEmployeeId(employeeId);
        return attendances.stream().map((attendance -> modelMapper.map(attendance, AttendanceDTO.class))).toList();
    }

    public List<AttendanceDTO> getAllAttendanceByDate(LocalDate date, Long departmentId, Long adminId) {
        Employee admin = employeeRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Admin Not Found with id : "+adminId));
        if (admin.getRole() != Role.ADMIN) {
            throw new ResourceNotFoundException("Invalid Admin");
        }
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department Not Found with id : "+departmentId));
        List<Attendance> attendances =
                attendanceRepository.findByDateAndEmployee_Department_Id(
                        date, departmentId);
        return attendances.stream().map((attendance -> modelMapper.map(attendance,AttendanceDTO.class))).toList();
    }

    public AttendanceDTO getByEmployee(Long employeeId, LocalDate date) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found with id : "+employeeId));
        Attendance attendance = attendanceRepository.findByDateAndEmployeeId(date,employeeId);
        return modelMapper.map(attendance,AttendanceDTO.class);
    }

    public List<AttendanceDTO> getAllByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not FOund with id : "+employeeId));
        List<Attendance> attendances = attendanceRepository.findAllByEmployeeId(employeeId);
        return attendances.stream().map((attendance -> modelMapper.map(attendance,AttendanceDTO.class))).toList();
    }

    public List<AttendanceDTO> getByMonth(Long employeeId, int year, int month) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Attendance> attendances = attendanceRepository.findAllByDateBetweenAndEmployeeId(startDate,endDate,employeeId);
        return attendances.stream().map((attendance -> modelMapper.map(attendance,AttendanceDTO.class))).toList();
    }

    public List<AttendanceDTO> getAllByManager(Long managerId, LocalDate date) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        List<Attendance> attendances = attendanceRepository.findAllByDateAndEmployee_Manager_Id(date,managerId);
        return attendances.stream().map((element) -> modelMapper.map(element, AttendanceDTO.class)).toList();
    }

    public List<AttendanceDTO> getByMonthAndManager(Long managerId, Long employeeId, int year, int month) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Manager Not Found"));
        Employee employee = employeeRepository.findById(managerId).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        if(employee.getManager()!=manager)
        {
            throw new ResourceNotFoundException("This is not Employee's Manager");
        }
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Attendance> attendances = attendanceRepository.findAllByDateBetweenAndEmployeeId(startDate,endDate,employeeId);
        return attendances.stream().map((attendance -> modelMapper.map(attendance,AttendanceDTO.class))).toList();
    }
}