package com.smarty.pfeserver.Request.Projet;


import java.util.ArrayList;
import java.util.List;

public class ChatCompletionRequest {
    private String model;
    private List<IaChatMessage> messages;

    // Constructor
    public ChatCompletionRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new IaChatMessage("user", prompt));
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<IaChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<IaChatMessage> messages) {
        this.messages = messages;
    }
}
