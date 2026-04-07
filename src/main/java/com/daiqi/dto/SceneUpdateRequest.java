package com.daiqi.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SceneUpdateRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String icon;

    private Boolean pinned;

    @NotEmpty
    private List<Long> tagIds;
}
