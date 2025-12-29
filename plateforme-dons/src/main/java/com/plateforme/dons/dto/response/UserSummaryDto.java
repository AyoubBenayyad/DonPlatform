package com.plateforme.dons.dto.response;

import lombok.Data;

@Data
public class UserSummaryDto {
    private Long id;
    private String username;
    private String email;
    private boolean notificationsActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotificationsActive() {
        return notificationsActive;
    }

    public void setNotificationsActive(boolean notificationsActive) {
        this.notificationsActive = notificationsActive;
    }
}
