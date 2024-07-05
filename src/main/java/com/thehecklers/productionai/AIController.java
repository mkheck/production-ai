package com.thehecklers.productionai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AIController {
    private final ChatClient client;
    private final ImageModel imageModel;

    public AIController(ChatClient.Builder builder, ImageModel imageModel) {
        this.client = builder.build();
        this.imageModel = imageModel;
    }

    // Basic String/String generate() method call
    @GetMapping
    public String generateResponse(@RequestParam(defaultValue = "What is the meaning of life") String message) {
        return client.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/fun")
    public ChatResponse generateResponseWithATwist(
            @RequestParam(defaultValue = "What is the meaning of life") String message,
            @RequestParam(required = false) String celebrity) {

        ChatClient.ChatClientRequest request = client.prompt()
                .user(message);

        if (celebrity != null) {
            request = request.system(String.format("You respond in the style of %s.", celebrity));
        }

        return request
                .call()
                .chatResponse();
    }

    @GetMapping("/entity")
    public AIAnswer generateResponseWithEntity(@RequestParam String message) {
        return client.prompt()
                .user(message)
                .call()
                .entity(AIAnswer.class);
    }

    // Using PromptTemplate to craft a prompt from parameters replied via the
    // request for a tailored response
    @GetMapping("/template")
    public String generateResponseFromTemplate(
            @RequestParam String type,
            @RequestParam String topic) {

        var template = new PromptTemplate("Tell me a {type} about {topic}",
                Map.of("type", type, "topic", topic));

        return client.prompt(template.create())
                .call()
                .content();
    }

    @GetMapping("/image")
    public ImageResponse generateImageResponse(@RequestParam String description) {
        return imageModel.call(
                new ImagePrompt(description));
    }
}
