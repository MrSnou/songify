package com.songify.infrastructure.apivalidation.SqlExceptionsHandler;

import lombok.extern.log4j.Log4j2;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

public class SqlExceptionHandler {

    @Log4j2
    @ControllerAdvice
    public static class ApiValidationErrorHandler {

        // 23505 - DB duplicate.
        @ExceptionHandler(PSQLException.class)
        public ResponseEntity<SqlExceptionDto> handleValidationException(PSQLException ex) {
            List<String> messages = List.of(ex.getMessage());
            String sqlState = ex.getSQLState();
            log.error("PSQLException while accessing database!");
            if ((sqlState).equals("23505")) {
                SqlExceptionDto response = new SqlExceptionDto(HttpStatus.CONFLICT, messages);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                SqlExceptionDto response = new SqlExceptionDto(HttpStatus.BAD_REQUEST, List.of("Database error"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }
}
