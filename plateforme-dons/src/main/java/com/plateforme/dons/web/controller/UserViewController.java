package com.plateforme.dons.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/mes-recherches")
    public String savedSearchesPage() {
        return "user/saved_searches";
    }

    @GetMapping("/notifications")
    public String notificationsPage() {
        return "user/notifications";
    }

    @GetMapping("/mon-profil")
    public String profilePage() {
        return "user/profile";
    }

}
