package com.daiqi.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageResponse<T> {
    private List<T> records;
    private long total;
    private long totalPages;
    private int page;
    private int size;

    public PageResponse() {
    }

    public PageResponse(List<T> records, long total, long totalPages, int page, int size) {
        this.records = records;
        this.total = total;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }
}
