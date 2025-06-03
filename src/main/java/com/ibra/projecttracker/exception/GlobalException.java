package com.ibra.projecttracker.exception;

import com.ibra.projecttracker.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllException(Exception ex, WebRequest request){
        Response response = Response.builder()
                .message(ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request){
        Response response = Response.builder()
                .message(ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
