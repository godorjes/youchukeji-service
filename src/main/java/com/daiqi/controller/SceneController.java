package com.daiqi.controller;

import java.util.List;

import com.daiqi.dto.CheckRequest;
import com.daiqi.dto.SceneCardCheckRequest;
import com.daiqi.dto.SceneCardResponse;
import com.daiqi.dto.SceneDetailResponse;
import com.daiqi.dto.SceneIdRequest;
import com.daiqi.dto.SceneRequest;
import com.daiqi.dto.SceneResponse;
import com.daiqi.dto.SceneUpdateRequest;
import com.daiqi.service.SceneService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/list")
    public List<SceneResponse> list(@RequestBody(required = false) Object body) {
        return sceneService.listScenes();
    }

    @PostMapping("/get")
    public SceneDetailResponse get(@Validated @RequestBody SceneIdRequest request) {
        return sceneService.getScene(request.getId());
    }

    @PostMapping("/cards")
    public List<SceneCardResponse> cards(@Validated @RequestBody SceneIdRequest request) {
        return sceneService.getSceneCards(request.getId());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SceneDetailResponse create(@Validated @RequestBody SceneRequest request) {
        return sceneService.createScene(request);
    }

    @PostMapping("/update")
    public SceneDetailResponse update(@Validated @RequestBody SceneUpdateRequest request) {
        SceneRequest payload = new SceneRequest();
        payload.setName(request.getName());
        payload.setIcon(request.getIcon());
        payload.setPinned(request.getPinned());
        payload.setTagIds(request.getTagIds());
        return sceneService.updateScene(request.getId(), payload);
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Validated @RequestBody SceneIdRequest request) {
        sceneService.deleteScene(request.getId());
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setCheck(@Validated @RequestBody SceneCardCheckRequest request) {
        CheckRequest payload = new CheckRequest();
        payload.setChecked(request.getChecked());
        sceneService.setCheck(request.getSceneId(), request.getCardId(), payload);
    }

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetChecks(@Validated @RequestBody SceneIdRequest request) {
        sceneService.resetChecks(request.getId());
    }
}
