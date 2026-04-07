package com.daiqi.dto;

public class TagResponse {
    private Long id;
    private String name;
    private String color;
    private long cardCount;

    public TagResponse() {
    }

    public TagResponse(Long id, String name, String color, long cardCount) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.cardCount = cardCount;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getCardCount() {
        return cardCount;
    }

    public void setCardCount(long cardCount) {
        this.cardCount = cardCount;
    }
}
