package com.thehecklers.productionai;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.ai.prompt.PromptTemplate;
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
    // return "Hello, AI!";
    // }

    @GetMapping
    public String generateResponse(@RequestParam(defaultValue = "What is the meaning of life") String prompt) {
        return client.generate(prompt);
    }

    @GetMapping("/template")
    public String generateResponseFromTemplate(@RequestParam String requestType, @RequestParam String requestTopic) {
        var template = new PromptTemplate("Tell me a {type} about {topic}",
                Map.of("type", requestType, "topic", requestTopic));
        return client.generate(template.create()).getGeneration().getContent();
    }
}
