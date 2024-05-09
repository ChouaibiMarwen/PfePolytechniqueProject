package com.camelsoft.rayaserver.Response.Chat;




import com.camelsoft.rayaserver.Models.Chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageResponse {
    private List<ChatMessage> messages = new ArrayList<>();
    private int currentPage;
    private Long totalItems;
    private int totalPages;

    public ChatMessageResponse(List<ChatMessage> messages, int currentPage, Long totalItems, int totalPages) {
        this.messages = messages;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
