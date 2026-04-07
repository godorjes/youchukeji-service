package com.daiqi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TagUpdateRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String color;
}
