package com.example.Wime_java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Wime_java.config.GoogleOAuthConfig;

@Controller
public class GoogleOAuthController {

    private final GoogleOAuthConfig config;
    

    public GoogleOAuthController(GoogleOAuthConfig config) {
        this.config = config;
    }

    @GetMapping("/auth/google/login")
    public RedirectView loginGoogle() {
        return new RedirectView(config.buildAuthUrl());
    }
}
