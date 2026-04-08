package com.daiqi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiqi.dto.CheckRequest;
import com.daiqi.dto.SceneCardResponse;
import com.daiqi.dto.SceneDetailResponse;
import com.daiqi.dto.SceneListRequest;
import com.daiqi.dto.SceneListResponse;
import com.daiqi.dto.SceneRequest;
import com.daiqi.dto.SceneResponse;
import com.daiqi.dto.SceneTagRow;
import com.daiqi.dto.TagSimple;
import com.daiqi.entity.Card;
import com.daiqi.entity.Scene;
import com.daiqi.entity.SceneCardCheck;
import com.daiqi.entity.Tag;
import com.daiqi.exception.BadRequestException;
import com.daiqi.exception.NotFoundException;
import com.daiqi.mapper.CardMapper;
import com.daiqi.mapper.SceneCardCheckMapper;
import com.daiqi.mapper.SceneMapper;
import com.daiqi.mapper.SceneTagMapper;
import com.daiqi.mapper.TagMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SceneService {

    private static final long DEFAULT_USER_ID = 0L;

    private final SceneMapper sceneMapper;
    private final TagMapper tagMapper;
    private final CardMapper cardMapper;
    private final SceneTagMapper sceneTagMapper;
    private final SceneCardCheckMapper checkMapper;

    public SceneService(SceneMapper sceneMapper, TagMapper tagMapper, CardMapper cardMapper,
                        SceneTagMapper sceneTagMapper, SceneCardCheckMapper checkMapper) {
        this.sceneMapper = sceneMapper;
        this.tagMapper = tagMapper;
        this.cardMapper = cardMapper;
        this.sceneTagMapper = sceneTagMapper;
        this.checkMapper = checkMapper;
    }

    @Transactional(readOnly = true)
    public SceneListResponse listScenes(SceneListRequest request) {
        String keyword = request.getKeyword() == null ? "" : request.getKeyword().trim();
        int page = Math.max(1, request.getPage());
        int size = Math.min(Math.max(1, request.getSize()), 50);

        // Pinned scenes: always return all, filtered by keyword when provided
        QueryWrapper<Scene> pinnedQuery = new QueryWrapper<Scene>().eq("pinned", true).orderByAsc("id");
        if (!keyword.isEmpty()) {
            pinnedQuery.like("name", keyword);
        }
        List<Scene> pinnedScenes = sceneMapper.selectList(pinnedQuery);

        // Non-pinned scenes: paginated + optional keyword filter
        int offset = (page - 1) * size;
        List<Scene> otherScenes = sceneMapper.selectOthersPage(keyword, offset, size);
        Long total = sceneMapper.countOthers(keyword);
        if (total == null) total = 0L;
        long totalPages = (total + size - 1) / size;

        // Batch-load tags for all scenes in one query
        List<Long> allIds = new ArrayList<>();
        for (Scene s : pinnedScenes) allIds.add(s.getId());
        for (Scene s : otherScenes) allIds.add(s.getId());
        Map<Long, List<TagSimple>> tagMap = allIds.isEmpty() ? new HashMap<>() : buildSceneTagMap(allIds);

        SceneListResponse response = new SceneListResponse();
        response.setPinned(buildSceneResponses(pinnedScenes, tagMap));
        response.setRecords(buildSceneResponses(otherScenes, tagMap));
        response.setTotal(total);
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(totalPages);
        return response;
    }

    private List<SceneResponse> buildSceneResponses(List<Scene> scenes, Map<Long, List<TagSimple>> tagMap) {
        List<SceneResponse> results = new ArrayList<>();
        for (Scene scene : scenes) {
            List<Long> cardIds = cardMapper.selectCardIdsBySceneId(scene.getId());
            long totalCount = cardIds.size();
            long checkedCount = 0L;
            if (!cardIds.isEmpty()) {
                Long count = checkMapper.countCheckedByUserSceneCardIds(DEFAULT_USER_ID, scene.getId(), cardIds);
                checkedCount = count == null ? 0L : count;
            }
            List<TagSimple> tags = tagMap.getOrDefault(scene.getId(), new ArrayList<>());
            results.add(new SceneResponse(scene.getId(), scene.getName(), scene.getIcon(), scene.isPinned(),
                    tags, totalCount, checkedCount));
        }
        return results;
    }

    @Transactional(readOnly = true)
    public SceneDetailResponse getScene(Long id) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null) {
            throw new NotFoundException("Scene not found");
        }
        List<Long> sceneIds = new ArrayList<>();
        sceneIds.add(id);
        List<TagSimple> tags = buildSceneTagMap(sceneIds).getOrDefault(id, new ArrayList<>());
        return new SceneDetailResponse(scene.getId(), scene.getName(), scene.getIcon(), scene.isPinned(), tags);
    }

    @Transactional(readOnly = true)
    public List<SceneCardResponse> getSceneCards(Long sceneId) {
        Scene scene = sceneMapper.selectById(sceneId);
        if (scene == null) {
            throw new NotFoundException("Scene not found");
        }
        List<Card> cards = cardMapper.selectCardsBySceneId(sceneId);
        if (cards.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> cardIds = new ArrayList<>();
        for (Card card : cards) {
            cardIds.add(card.getId());
        }
        Map<Long, Boolean> checkedMap = new HashMap<>();
        Map<Long, java.time.LocalDateTime> checkedAtMap = new HashMap<>();
        List<SceneCardCheck> checks = checkMapper.selectByUserSceneCardIds(DEFAULT_USER_ID, sceneId, cardIds);
        for (SceneCardCheck check : checks) {
            checkedMap.put(check.getCardId(), check.isChecked());
            if (check.isChecked() && check.getUpdatedAt() != null) {
                checkedAtMap.put(check.getCardId(), check.getUpdatedAt());
            }
        }

        Map<Long, List<TagSimple>> tagMap = buildCardTagMap(cardIds);
        List<SceneCardResponse> results = new ArrayList<>();
        for (Card card : cards) {
            boolean checked = checkedMap.getOrDefault(card.getId(), false);
            List<TagSimple> tags = tagMap.getOrDefault(card.getId(), new ArrayList<>());
            SceneCardResponse resp = new SceneCardResponse(card.getId(), card.getTitle(), tags, checked);
            resp.setLastCheckedAt(checkedAtMap.get(card.getId()));
            results.add(resp);
        }
        return results;
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public SceneDetailResponse createScene(SceneRequest request) {
        Set<Tag> tags = loadTags(request.getTagIds());
        Scene scene = new Scene();
        scene.setName(request.getName());
        scene.setIcon(request.getIcon());
        scene.setPinned(request.getPinned() != null && request.getPinned());
        sceneMapper.insert(scene);
        sceneTagMapper.insertSceneTags(scene.getId(), toTagIds(tags));
        return new SceneDetailResponse(scene.getId(), scene.getName(), scene.getIcon(), scene.isPinned(),
                toTagSimples(tags));
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public SceneDetailResponse updateScene(Long id, SceneRequest request) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null) {
            throw new NotFoundException("Scene not found");
        }
        Set<Tag> tags = loadTags(request.getTagIds());
        scene.setName(request.getName());
        scene.setIcon(request.getIcon());
        scene.setPinned(request.getPinned() != null && request.getPinned());
        sceneMapper.updateById(scene);
        sceneTagMapper.deleteBySceneId(id);
        sceneTagMapper.insertSceneTags(id, toTagIds(tags));
        return new SceneDetailResponse(scene.getId(), scene.getName(), scene.getIcon(), scene.isPinned(),
                toTagSimples(tags));
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public void deleteScene(Long id) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null) {
            throw new NotFoundException("Scene not found");
        }
        sceneMapper.deleteById(id);
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public void setCheck(Long sceneId, Long cardId, CheckRequest request) {
        Scene scene = sceneMapper.selectById(sceneId);
        if (scene == null) {
            throw new NotFoundException("Scene not found");
        }
        Card card = cardMapper.selectById(cardId);
        if (card == null) {
            throw new NotFoundException("Card not found");
        }
        Boolean inScene = cardMapper.existsCardInScene(sceneId, cardId);
        if (!Boolean.TRUE.equals(inScene)) {
            throw new BadRequestException("Card does not belong to scene");
        }

        SceneCardCheck check = checkMapper.selectByUserSceneCard(DEFAULT_USER_ID, sceneId, cardId);
        if (check == null) {
            check = new SceneCardCheck();
            check.setUserId(DEFAULT_USER_ID);
            check.setSceneId(sceneId);
            check.setCardId(cardId);
            check.setChecked(request.getChecked());
            checkMapper.insert(check);
        } else {
            check.setChecked(request.getChecked());
            checkMapper.updateById(check);
        }
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public void resetChecks(Long sceneId) {
        Scene scene = sceneMapper.selectById(sceneId);
        if (scene == null) {
            throw new NotFoundException("Scene not found");
        }
        checkMapper.deleteByUserScene(DEFAULT_USER_ID, sceneId);
    }

    private Set<Tag> loadTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            throw new BadRequestException("tagIds is required");
        }
        List<Tag> tags = tagMapper.selectBatchIds(tagIds);
        if (tags.size() != new HashSet<>(tagIds).size()) {
            throw new BadRequestException("Some tagIds are invalid");
        }
        return new HashSet<>(tags);
    }

    private List<TagSimple> toTagSimples(Set<Tag> tags) {
        List<TagSimple> list = new ArrayList<>();
        for (Tag tag : tags) {
            list.add(new TagSimple(tag.getId(), tag.getName(), tag.getColor()));
        }
        list.sort(Comparator.comparing(TagSimple::getId));
        return list;
    }

    private List<Long> toTagIds(Set<Tag> tags) {
        List<Long> ids = new ArrayList<>();
        for (Tag tag : tags) {
            ids.add(tag.getId());
        }
        return ids;
    }

    private Map<Long, List<TagSimple>> buildSceneTagMap(List<Long> sceneIds) {
        Map<Long, List<TagSimple>> map = new HashMap<>();
        if (sceneIds == null || sceneIds.isEmpty()) {
            return map;
        }
        List<SceneTagRow> rows = sceneMapper.selectTagsBySceneIds(sceneIds);
        for (SceneTagRow row : rows) {
            map.computeIfAbsent(row.getSceneId(), k -> new ArrayList<>())
                    .add(new TagSimple(row.getTagId(), row.getName(), row.getColor()));
        }
        for (List<TagSimple> list : map.values()) {
            list.sort(Comparator.comparing(TagSimple::getId));
        }
        return map;
    }

    private Map<Long, List<TagSimple>> buildCardTagMap(List<Long> cardIds) {
        Map<Long, List<TagSimple>> map = new HashMap<>();
        if (cardIds == null || cardIds.isEmpty()) {
            return map;
        }
        List<com.daiqi.dto.CardTagRow> rows = cardMapper.selectTagsByCardIds(cardIds);
        for (com.daiqi.dto.CardTagRow row : rows) {
            map.computeIfAbsent(row.getCardId(), k -> new ArrayList<>())
                    .add(new TagSimple(row.getTagId(), row.getName(), row.getColor()));
        }
        for (List<TagSimple> list : map.values()) {
            list.sort(Comparator.comparing(TagSimple::getId));
        }
        return map;
    }
}
