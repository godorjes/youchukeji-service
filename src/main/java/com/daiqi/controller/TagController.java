package com.daiqi.controller;

import java.util.List;

import com.daiqi.dto.TagIdRequest;
import com.daiqi.dto.TagRequest;
import com.daiqi.dto.TagResponse;
import com.daiqi.dto.TagUpdateRequest;
import com.daiqi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/list")
    public List<TagResponse> list(@RequestBody(required = false) Object body) {
        return tagService.getAllTags();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(@Validated @RequestBody TagRequest request) {
        return tagService.createTag(request);
    }

    @PostMapping("/update")
    public TagResponse update(@Validated @RequestBody TagUpdateRequest request) {
        TagRequest payload = new TagRequest();
        payload.setName(request.getName());
        payload.setColor(request.getColor());
        return tagService.updateTag(request.getId(), payload);
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Validated @RequestBody TagIdRequest request) {
        tagService.deleteTag(request.getId());
    }
}
