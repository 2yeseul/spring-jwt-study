package com.example.lunit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/api/docs")
    public String docs() {
        return "doc/api-docs";
    }
}
