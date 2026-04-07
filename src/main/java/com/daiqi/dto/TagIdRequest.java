package com.daiqi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TagIdRequest {
    @NotNull
    private Long id;
}
