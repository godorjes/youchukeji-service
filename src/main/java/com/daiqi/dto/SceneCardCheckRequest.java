package com.daiqi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SceneCardCheckRequest {
    @NotNull
    private Long sceneId;

    @NotNull
    private Long cardId;

    @NotNull
    private Boolean checked;
}
