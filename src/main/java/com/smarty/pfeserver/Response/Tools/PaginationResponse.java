package com.smarty.pfeserver.Response.Tools;

import java.util.ArrayList;
import java.util.List;

public class PaginationResponse {
    private List<?> result = new ArrayList<>();
    private int currentPage;
    private Long totalItems;
    private int totalPages;

    public PaginationResponse() {
    }

    public PaginationResponse(List<?> result, int currentPage, Long totalItems, int totalPages) {
        this.result = result;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
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
