package com.company.main.EmployeeManagement.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRangeValid {

    String message() default "end date must be after start date";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


