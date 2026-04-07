package com.songify.infrastructure.usercrud.dto;


public record UserRegisterRequestDto(
        String email,
        String password) {
}
