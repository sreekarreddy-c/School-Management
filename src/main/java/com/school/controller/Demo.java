package com.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test") // Corrected annotation from @Mapping to @RequestMapping
public class Demo {

    @GetMapping("/demo")
    public String m1() { // Specify return type as String
        return "{\"message\": \"json values for test purpose\"}"; // Return valid JSON
    }
}
