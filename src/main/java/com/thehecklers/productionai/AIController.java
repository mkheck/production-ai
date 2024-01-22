package com.thehecklers.productionai;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/")
public class AIController {
    private final AzureOpenAiChatClient client;

    public AIController(AzureOpenAiChatClient client) {
        this.client = client;
    }

    // @GetMapping
    // public String livenessCheck() {
    //     return "Hello, AI!";
    // }

    @GetMapping
    public String generateResponse(@RequestParam(defaultValue = "What is the meaning of life") String prompt) {
        return client.generate(prompt);
    }
}
