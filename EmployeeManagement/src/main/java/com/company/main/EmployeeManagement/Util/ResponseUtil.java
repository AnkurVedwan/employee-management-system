
package com.company.main.EmployeeManagement.Util;

import com.company.main.EmployeeManagement.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

public class ResponseUtil {

    private ResponseUtil() {
        // prevent object creation
    }

    public static <T> ApiResponse<T> success(
            String message,
            T data,
            HttpServletRequest request
    ) {
        return ApiResponse.<T>builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    public static ApiResponse<Void> error(
            String message,
            HttpServletRequest request
    ) {
        return ApiResponse.<Void>builder()
                .status("ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }
}
