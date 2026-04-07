package com.songify.infrastructure.usercrud.error;

import org.springframework.http.HttpStatus;

record UserNotFoundResponseDto(HttpStatus httpStatus, String message) {
}
