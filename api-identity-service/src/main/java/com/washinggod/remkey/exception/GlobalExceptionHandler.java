package com.washinggod.remkey.exception;


import com.washinggod.remkey.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> appExceptionHandler(AppException exception) {

        ErrorCode errorCode = exception.getErrorCode();

        String message = errorCode.getMessage();
        Long code = errorCode.getCode();
        HttpStatus httpStatus = errorCode.getHttpStatus();

        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(code);
        apiResponse.setMessage(message);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> exceptionHandler(Exception exception) {

        log.error("ERROR: {}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        Long code = errorCode.getCode();
        String message = errorCode.getMessage();
        HttpStatus httpStatus = errorCode.getHttpStatus();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        String errorCodeName = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(errorCodeName);
        Long code = errorCode.getCode();
        String message = errorCode.getMessage();
        HttpStatus httpStatus = errorCode.getHttpStatus();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }
}
