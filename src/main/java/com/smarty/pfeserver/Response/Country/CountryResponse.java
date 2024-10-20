package com.smarty.pfeserver.Response.Country;


import com.smarty.pfeserver.Models.country.Root;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CountryResponse {
    private List<Root> countries = new ArrayList<>();
    private int currentPage;
    private Long totalItems;
    private int totalPages;

    public CountryResponse() {
    }

    public CountryResponse(List<Root> countries, int currentPage, Long totalItems, int totalPages) {
        this.countries = countries;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }
}
