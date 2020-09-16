package com.manager.timezone.timezonemanagerserver.dto;

import javax.validation.constraints.Min;

public class UpdatePasswordDto {
    private String currentPassword;

    @Min(value = 6, message = "Password must have minimum 6 characters")
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
