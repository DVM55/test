package com.example.demo.exception;

import com.example.demo.dto.res.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .details(e.toString())
                .message(e.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().getFirst();

        String fieldName = fieldError.getField();
        String errorMessage = fieldError.getDefaultMessage();

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST)
                .message(errorMessage)
                .details("Invalid field: " + fieldName)
                .build();

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .details(ex.toString())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleConflictException(ConflictException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.CONFLICT)
                .details(e.toString())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .details("Không tìm thấy tài nguyên được yêu cầu")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleInvalidEnum(HttpMessageNotReadableException ex) {

        ExceptionResponse response = ExceptionResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST)
                .message("Dữ liệu gửi lên không hợp lệ")
                .details(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}