package com.daiqi.dto;

import lombok.Data;

@Data
public class SceneListRequest {
    private String keyword;
    private int page = 1;
    private int size = 8;
}
