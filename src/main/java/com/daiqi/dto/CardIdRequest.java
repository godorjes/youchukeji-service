package com.daiqi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CardIdRequest {
    @NotNull
    private Long id;
}
