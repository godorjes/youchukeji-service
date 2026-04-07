package com.daiqi.controller;

import java.util.List;

import com.daiqi.dto.CheckRequest;
import com.daiqi.dto.SceneCardResponse;
import com.daiqi.dto.SceneDetailResponse;
import com.daiqi.dto.SceneRequest;
import com.daiqi.dto.SceneResponse;
import com.daiqi.service.SceneService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenes")
public class SceneController {

    private final SceneService sceneService;

    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @GetMapping
    public List<SceneResponse> list() {
        return sceneService.listScenes();
    }

    @GetMapping("/{id}")
    public SceneDetailResponse get(@PathVariable Long id) {
        return sceneService.getScene(id);
    }

    @GetMapping("/{id}/cards")
    public List<SceneCardResponse> cards(@PathVariable Long id) {
        return sceneService.getSceneCards(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SceneDetailResponse create(@Validated @RequestBody SceneRequest request) {
        return sceneService.createScene(request);
    }

    @PutMapping("/{id}")
    public SceneDetailResponse update(@PathVariable Long id, @Validated @RequestBody SceneRequest request) {
        return sceneService.updateScene(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sceneService.deleteScene(id);
    }

    @PutMapping("/{id}/cards/{cardId}/check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setCheck(@PathVariable Long id, @PathVariable Long cardId,
                         @Validated @RequestBody CheckRequest request) {
        sceneService.setCheck(id, cardId, request);
    }

    @DeleteMapping("/{id}/checks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetChecks(@PathVariable Long id) {
        sceneService.resetChecks(id);
    }
}
