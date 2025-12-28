package com.plateforme.dons.controller;

import com.plateforme.dons.dto.payload.LoginRequest;
import com.plateforme.dons.dto.payload.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        // Main entry point for the UI
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @GetMapping("/annonces")
    public String listAnnonces() {
        return "annonces/list";
    }

    @GetMapping("/annonces/{id}")
    public String detailAnnonce() {
        return "annonces/detail";
    }

    @GetMapping("/annonces/new")
    public String createAnnonceForm(Model model) {
        model.addAttribute("annonceRequest", new com.plateforme.dons.dto.payload.AnnonceRequest());
        return "annonces/form";
    }

    @GetMapping("/annonces/edit/{id}")
    public String editAnnonceForm(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        // We'll let the frontend JS fetch the data using the ID from the URL
        return "annonces/form";
    }
}
