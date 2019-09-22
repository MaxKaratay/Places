package com.basecamp.rest.controller;

import com.basecamp.rest.exception.ErrorBean;
import com.basecamp.rest.exception.NotFoundException;
import com.basecamp.rest.exception.ObjectExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
@ResponseBody
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        ErrorBean errorBean = new ErrorBean(message, HttpStatus.NOT_FOUND, ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorBean, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ObjectExistsException.class})
    public ResponseEntity<ErrorBean> handleObjectExistsException(ObjectExistsException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        ErrorBean errorBean = new ErrorBean(message, HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorBean, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBean> handleAll(Exception ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        ErrorBean errorBean = new ErrorBean(message, HttpStatus.INTERNAL_SERVER_ERROR, ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorBean, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex.getCause());
        Map<String, String> errors = getErrorsFromBindingResult(ex.getBindingResult());
        log.info(errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    static Map<String, String> getErrorsFromBindingResult(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> fieldErrorMapCollector = Collectors
                .toMap(FieldError::getField,
                        FieldError::getDefaultMessage
                );
        return bindingResult.getFieldErrors().stream().collect(fieldErrorMapCollector);
    }
}
