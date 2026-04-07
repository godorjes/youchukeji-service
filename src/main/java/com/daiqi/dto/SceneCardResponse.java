package com.daiqi.dto;

import java.util.List;

public class SceneCardResponse {
    private Long id;
    private String title;
    private List<TagSimple> tags;
    private boolean checked;

    public SceneCardResponse() {
    }

    public SceneCardResponse(Long id, String title, List<TagSimple> tags, boolean checked) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TagSimple> getTags() {
        return tags;
    }

    public void setTags(List<TagSimple> tags) {
        this.tags = tags;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
