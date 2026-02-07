package com.songify.infrastructure.apivalidation;

import org.springframework.http.HttpStatus;

import java.util.List;

record ApiValidationErrorResponseDto(HttpStatus status, List<String> errors) {



}
