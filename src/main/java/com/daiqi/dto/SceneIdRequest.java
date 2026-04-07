package com.daiqi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SceneIdRequest {
    @NotNull
    private Long id;
}
