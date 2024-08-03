package com.example.baseback.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        return "Welcome, " + user.getAttribute("name");
    }
}
