package com.directa24.main.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RequestDto {
    @JsonProperty("page")
    private int page;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total")
    private int total;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("data")
    private List<Movie> data;
}
