package com.daiqi.dto;

import java.util.List;

import lombok.Data;

@Data
public class SceneListResponse {
    private List<SceneResponse> pinned;
    private List<SceneResponse> records;
    private long total;
    private int page;
    private int size;
    private long totalPages;
}
