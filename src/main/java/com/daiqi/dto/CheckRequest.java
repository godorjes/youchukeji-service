package com.daiqi.dto;

import javax.validation.constraints.NotNull;

public class CheckRequest {
    @NotNull
    private Boolean checked;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
