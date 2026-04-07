package com.daiqi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiqi.dto.TagCountRow;
import com.daiqi.dto.TagRequest;
import com.daiqi.dto.TagResponse;
import com.daiqi.entity.Tag;
import com.daiqi.exception.ConflictException;
import com.daiqi.exception.NotFoundException;
import com.daiqi.mapper.CardMapper;
import com.daiqi.mapper.SceneMapper;
import com.daiqi.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    private final TagMapper tagMapper;
    private final CardMapper cardMapper;
    private final SceneMapper sceneMapper;

    public TagService(TagMapper tagMapper, CardMapper cardMapper, SceneMapper sceneMapper) {
        this.tagMapper = tagMapper;
        this.cardMapper = cardMapper;
        this.sceneMapper = sceneMapper;
    }

    @Cacheable(cacheNames = "tags")
    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>().orderByAsc("id"));
        Map<Long, Long> counts = new HashMap<>();
        for (TagCountRow row : tagMapper.countCardsByTag()) {
            counts.put(row.getId(), row.getCnt() == null ? 0L : row.getCnt());
        }
        List<TagResponse> results = new ArrayList<>();
        for (Tag tag : tags) {
            long count = counts.getOrDefault(tag.getId(), 0L);
            results.add(new TagResponse(tag.getId(), tag.getName(), tag.getColor(), count));
        }
        return results;
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public TagResponse createTag(TagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tagMapper.insert(tag);
        long count = tagMapper.countCardsByTagId(tag.getId());
        return new TagResponse(tag.getId(), tag.getName(), tag.getColor(), count);
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new NotFoundException("Tag not found");
        }
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tagMapper.updateById(tag);
        long count = tagMapper.countCardsByTagId(tag.getId());
        return new TagResponse(tag.getId(), tag.getName(), tag.getColor(), count);
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new NotFoundException("Tag not found");
        }
        Boolean cardRef = cardMapper.existsByTagId(id);
        Boolean sceneRef = sceneMapper.existsByTagId(id);
        if (Boolean.TRUE.equals(cardRef) || Boolean.TRUE.equals(sceneRef)) {
            throw new ConflictException("Tag is referenced by cards or scenes");
        }
        tagMapper.deleteById(id);
    }
}
