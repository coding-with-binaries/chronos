package com.manager.timezone.timezonemanagerserver.dto;

public class AuthenticateUserResponseDto {
    private String accessToken;

    private String tokenType = "Authorization";

    public AuthenticateUserResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
