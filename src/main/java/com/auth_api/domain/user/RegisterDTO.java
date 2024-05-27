package com.auth_api.domain.user;

public record RegisterDTO(String login, String password, String email, UserRole role) {

}
