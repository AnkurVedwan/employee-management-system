package com.company.main.EmployeeManagement.annotation;

import com.company.main.EmployeeManagement.dto.LeaveRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator
        implements ConstraintValidator<DateRangeValid, LeaveRequestDTO> {

    @Override
    public boolean isValid(
            LeaveRequestDTO dto,
            ConstraintValidatorContext context
    ) {
        if (dto == null) return true;

        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();

        if (start == null || end == null) return true;

        if (end.isBefore(start)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "end-date is smaller than start-date"
                    ).addPropertyNode("endDate")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
