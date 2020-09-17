package com.chronos.chronosserver.dto;

import javax.validation.constraints.Size;

public class UpdatePasswordDto {
    private String currentPassword;

    @Size(min = 6, message = "Password must have minimum 6 characters")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
