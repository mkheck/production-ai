package com.thehecklers.productionai;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/")
public class AIController {
    @GetMapping
    public String livenessCheck() {
        return "Hello, AI!";
    }
}
