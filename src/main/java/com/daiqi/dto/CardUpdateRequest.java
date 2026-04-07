package com.daiqi.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CardUpdateRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotEmpty
    private List<Long> tagIds;
}
