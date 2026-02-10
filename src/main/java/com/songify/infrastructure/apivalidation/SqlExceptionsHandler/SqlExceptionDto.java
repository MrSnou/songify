package com.songify.infrastructure.apivalidation.SqlExceptionsHandler;

import org.springframework.http.HttpStatus;

import java.util.List;

record SqlExceptionDto(HttpStatus status, List<String> messages) {
}
