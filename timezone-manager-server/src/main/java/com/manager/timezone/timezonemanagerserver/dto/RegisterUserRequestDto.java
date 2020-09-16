package com.manager.timezone.timezonemanagerserver.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class RegisterUserRequestDto {
    @NotBlank(message = "Username cannot be blank.")
    @Min(value = 6, message = "Username must have minimum 6 characters")
    @Max(value = 30, message = "Username must have maximum 30 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Min(value = 6, message = "Password must have minimum 6 characters")
    private String password;

    private Set<RoleType> roles;

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

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}
