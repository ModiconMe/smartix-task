package io.modicon.smartixtask.infrastructure.exception;

import io.modicon.smartixtask.web.dto.ApiExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<ApiExceptionDto> handle(ApiException e) {
        return new ResponseEntity<>(new ApiExceptionDto(e.getMessage()), e.getStatus());
    }

    /**
     * Spring validation exception handling
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDto> handle(MethodArgumentNotValidException e) {
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return new ResponseEntity<>(new ApiExceptionDto(defaultMessage), e.getStatusCode());
    }

}
