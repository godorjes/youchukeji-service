package com.daiqi.dto;

import lombok.Data;

@Data
public class CardListRequest {
    private Long tagId;
    private Integer page;
    private Integer size;
}
