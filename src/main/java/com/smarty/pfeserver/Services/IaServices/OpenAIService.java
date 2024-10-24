package com.smarty.pfeserver.Services.IaServices;

import com.smarty.pfeserver.Request.Projet.ChatCompletionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class OpenAIService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String listModels() {
        String url = "https://api.openai.com/v1/models";

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        // Create an HttpEntity object
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the GET request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }



    public String getPrediction(String prompt) {
        // Create the request body
        ChatCompletionRequest request = new ChatCompletionRequest("chatgpt-4o-latest", prompt);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Create the HttpEntity with the request body and headers
        HttpEntity<ChatCompletionRequest> requestEntity = new HttpEntity<>(request, headers);

        // Make the POST request
        String url = "https://api.openai.com/v1/chat/completions";
        try {
            Map<String, Object> response = restTemplate.postForObject(url, requestEntity, Map.class);
            if (response != null && response.containsKey("choices")) {
                Map<String, Object> choice = ((List<Map<String, Object>>) response.get("choices")).get(0);
                Map<String, String> message = (Map<String, String>) choice.get("message");
                return message.get("content"); // Return the content of the message
            }
        } catch (RestClientException e) {
            System.err.println("Error getting prediction: " + e.getMessage());
            throw e; // Rethrow or handle as needed
        }
        return null; // Return null if no response
    }
}

