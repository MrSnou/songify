package com.songify.infrastructure.apivalidation.SqlExceptionsHandler;

import org.postgresql.util.PSQLException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class SqlExceptionHandler {

    @ControllerAdvice
    public class ApiValidationErrorHandler {

        // 23505 - DB duplicate.
        @ExceptionHandler(PSQLException.class)
        public ResponseEntity<SqlExceptionDto> handleValidationException(PSQLException ex) {
            List<String> messages = List.of(ex.getMessage());
            String sqlState = ex.getSQLState();
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
