package com.daiqi.dto;

import java.util.List;

public class SceneDetailResponse {
    private Long id;
    private String name;
    private String icon;
    private boolean pinned;
    private List<TagSimple> tags;

    public SceneDetailResponse() {
    }

    public SceneDetailResponse(Long id, String name, String icon, boolean pinned, List<TagSimple> tags) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.pinned = pinned;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public List<TagSimple> getTags() {
        return tags;
    }

    public void setTags(List<TagSimple> tags) {
        this.tags = tags;
    }
}
