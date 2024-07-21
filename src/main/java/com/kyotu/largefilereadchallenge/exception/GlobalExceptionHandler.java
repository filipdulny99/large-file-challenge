package com.kyotu.largefilereadchallenge.exception;

import com.kyotu.largefilereadchallenge.mapper.ValidationExceptionMapper;
import com.kyotu.largefilereadchallenge.model.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ApiError> handleHttpStatusCodeExceptions(HttpStatusCodeException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ApiError(Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableExceptions(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ApiError(e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolation(HandlerMethodValidationException e) {
        return new ResponseEntity<>(new ApiError(ValidationExceptionMapper.toMessage(e)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileReadException.class)
    public ResponseEntity<ApiError> handleFileNotFoundExceptions(FileReadException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {JobInstanceAlreadyCompleteException.class, JobExecutionAlreadyRunningException.class,
            JobParametersInvalidException.class, JobRestartException.class})
    public ResponseEntity<ApiError> handleBatchJobExceptions(Exception e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

