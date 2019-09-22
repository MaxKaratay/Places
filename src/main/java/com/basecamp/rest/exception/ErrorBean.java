package com.basecamp.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorBean {
    private String errors;
    private HttpStatus httpStatus;
    private String exceptionName;
}
