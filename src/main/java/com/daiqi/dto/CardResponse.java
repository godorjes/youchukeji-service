package com.daiqi.dto;

import java.util.List;

public class CardResponse {
    private Long id;
    private String title;
    private List<TagSimple> tags;

    public CardResponse() {
    }

    public CardResponse(Long id, String title, List<TagSimple> tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
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
}
