package com.company.main.EmployeeManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status;        // SUCCESS / ERROR
    private String message;       // Human readable message
    private T data;               // Actual response data
    private LocalDateTime timestamp;
    private String path;          // API path
}
