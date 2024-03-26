package com.thehecklers.productionai;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.ChatMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/")
public class AIController {
    private final AzureOpenAiChatClient client;

    private final List<Message> buffer = new ArrayList<>();

    public AIController(AzureOpenAiChatClient client) {
        this.client = client;
    }

    // Basic String/String generate() method call
    @GetMapping
    public String generateResponse(@RequestParam(defaultValue = "What is the meaning of life") String message) {
        return client.call(message);
    }

    @GetMapping("/fun")
    public ChatResponse generateResponseWithATwist(
            @RequestParam(defaultValue = "What is the meaning of life") String message,
            @RequestParam(required = false) String celebrity) {

        var promptMessages = new ArrayList<Message>();

        promptMessages.add(new ChatMessage(MessageType.USER, message));

        if (celebrity != null) {
            // Add a system message to the prompt to indicate the celebrity
            var systemMessage = new SystemPromptTemplate("You respond in the style of {celebrity}.")
                    .createMessage(Map.of("celebrity", celebrity));
            promptMessages.add(systemMessage);
        }

        promptMessages.forEach(m -> System.out.println(m.getContent()));

        var response = client.call(new Prompt(promptMessages));
        //If you retrieve > 1 result, you can iterate over them to get the output
        //response.getResults().forEach(r -> buffer.add(r.getOutput()));
        buffer.add(response.getResult().getOutput());

        return response;
    }

    // Using PromptTemplate to craft a prompt from parameters replied via the
    // request for a tailored response
    @GetMapping("/template")
    public String generateResponseFromTemplate(@RequestParam String requestType, @RequestParam String requestTopic) {
        var template = new PromptTemplate("Tell me a {type} about {topic}",
                Map.of("type", requestType, "topic", requestTopic));
        return client.call(template.create()).getResult().getOutput().getContent();
    }
}
