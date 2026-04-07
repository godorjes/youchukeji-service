package com.daiqi.controller;

import com.daiqi.dto.CardIdRequest;
import com.daiqi.dto.CardListRequest;
import com.daiqi.dto.CardRequest;
import com.daiqi.dto.CardResponse;
import com.daiqi.dto.CardUpdateRequest;
import com.daiqi.dto.PageResponse;
import com.daiqi.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/list")
    public PageResponse<CardResponse> list(@RequestBody(required = false) CardListRequest request) {
        Long tagId = request == null ? null : request.getTagId();
        Integer page = request == null ? null : request.getPage();
        Integer size = request == null ? null : request.getSize();
        return cardService.listCards(tagId, page, size);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse create(@Validated @RequestBody CardRequest request) {
        return cardService.createCard(request);
    }

    @PostMapping("/update")
    public CardResponse update(@Validated @RequestBody CardUpdateRequest request) {
        CardRequest payload = new CardRequest();
        payload.setTitle(request.getTitle());
        payload.setTagIds(request.getTagIds());
        return cardService.updateCard(request.getId(), payload);
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Validated @RequestBody CardIdRequest request) {
        cardService.deleteCard(request.getId());
    }
}
