package com.innrate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final String welcomePath;

    @Autowired
    public WelcomeController(@Value("${innrate.frontend-path-prefix}") String pathPrefix) {
        this.welcomePath = "redirect:" + pathPrefix + "/index.html";
    }

    @GetMapping(value = "/")
    public String welcomePageRedirect() {
        return welcomePath;
    }
}
