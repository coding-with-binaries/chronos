package com.manager.timezone.timezonemanagerserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterUserRequestDto {
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 6, max = 30, message = "Username must have minimum 6 characters and maximum 30 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 6, message = "Password must have minimum 6 characters")
    private String password;

    private RoleType role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
