package com.daiqi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daiqi.dto.CardRequest;
import com.daiqi.dto.CardResponse;
import com.daiqi.dto.CardTagRow;
import com.daiqi.dto.PageResponse;
import com.daiqi.dto.TagSimple;
import com.daiqi.entity.Card;
import com.daiqi.entity.Tag;
import com.daiqi.exception.BadRequestException;
import com.daiqi.exception.NotFoundException;
import com.daiqi.mapper.CardMapper;
import com.daiqi.mapper.CardTagMapper;
import com.daiqi.mapper.TagMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private final CardMapper cardMapper;
    private final TagMapper tagMapper;
    private final CardTagMapper cardTagMapper;

    public CardService(CardMapper cardMapper, TagMapper tagMapper, CardTagMapper cardTagMapper) {
        this.cardMapper = cardMapper;
        this.tagMapper = tagMapper;
        this.cardTagMapper = cardTagMapper;
    }

    @Cacheable(cacheNames = "cards", key = "'tag=' + (#tagId == null ? 'all' : #tagId) + ':page=' + #page + ':size=' + #size")
    @Transactional(readOnly = true)
    public PageResponse<CardResponse> listCards(Long tagId, Integer page, Integer size) {
        int pageNum = (page == null || page < 1) ? 1 : page;
        int pageSize = (size == null || size < 1) ? 20 : size;
        int offset = (pageNum - 1) * pageSize;

        List<Card> cards;
        long total;
        long totalPages;
        if (tagId != null) {
            Tag tag = tagMapper.selectById(tagId);
            if (tag == null) {
                throw new NotFoundException("Tag not found");
            }
            total = cardMapper.countByTagId(tagId);
            totalPages = calcTotalPages(total, pageSize);
            cards = cardMapper.selectPageByTagId(tagId, offset, pageSize);
        } else {
            Page<Card> pageResult = cardMapper.selectPage(new Page<>(pageNum, pageSize),
                    new QueryWrapper<Card>().orderByAsc("id"));
            total = pageResult.getTotal();
            totalPages = pageResult.getPages();
            cards = pageResult.getRecords();
        }

        List<CardResponse> records = toResponses(cards);
        return new PageResponse<>(records, total, totalPages, pageNum, pageSize);
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public CardResponse createCard(CardRequest request) {
        Set<Tag> tags = loadTags(request.getTagIds());
        Card card = new Card();
        card.setTitle(request.getTitle());
        cardMapper.insert(card);
        cardTagMapper.insertCardTags(card.getId(), new ArrayList<>(toTagIds(tags)));
        return toResponse(card, tags);
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public CardResponse updateCard(Long id, CardRequest request) {
        Card card = cardMapper.selectById(id);
        if (card == null) {
            throw new NotFoundException("Card not found");
        }
        Set<Tag> tags = loadTags(request.getTagIds());
        card.setTitle(request.getTitle());
        cardMapper.updateById(card);
        cardTagMapper.deleteByCardId(id);
        cardTagMapper.insertCardTags(id, new ArrayList<>(toTagIds(tags)));
        return toResponse(card, tags);
    }

    @CacheEvict(cacheNames = {"tags", "cards", "scenes"}, allEntries = true)
    @Transactional
    public void deleteCard(Long id) {
        Card card = cardMapper.selectById(id);
        if (card == null) {
            throw new NotFoundException("Card not found");
        }
        cardMapper.deleteById(id);
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

    private List<CardResponse> toResponses(List<Card> cards) {
        List<CardResponse> results = new ArrayList<>();
        if (cards == null || cards.isEmpty()) {
            return results;
        }
        List<Long> cardIds = new ArrayList<>();
        for (Card card : cards) {
            cardIds.add(card.getId());
        }
        Map<Long, List<TagSimple>> tagMap = new HashMap<>();
        List<CardTagRow> rows = cardMapper.selectTagsByCardIds(cardIds);
        for (CardTagRow row : rows) {
            tagMap.computeIfAbsent(row.getCardId(), k -> new ArrayList<>())
                    .add(new TagSimple(row.getTagId(), row.getName(), row.getColor()));
        }
        for (List<TagSimple> list : tagMap.values()) {
            list.sort(Comparator.comparing(TagSimple::getId));
        }
        for (Card card : cards) {
            List<TagSimple> tags = tagMap.getOrDefault(card.getId(), new ArrayList<>());
            results.add(new CardResponse(card.getId(), card.getTitle(), tags));
        }
        return results;
    }

    private CardResponse toResponse(Card card, Set<Tag> tags) {
        List<TagSimple> tagSimples = new ArrayList<>();
        for (Tag tag : tags) {
            tagSimples.add(new TagSimple(tag.getId(), tag.getName(), tag.getColor()));
        }
        tagSimples.sort(Comparator.comparing(TagSimple::getId));
        return new CardResponse(card.getId(), card.getTitle(), tagSimples);
    }

    private List<Long> toTagIds(Set<Tag> tags) {
        List<Long> ids = new ArrayList<>();
        for (Tag tag : tags) {
            ids.add(tag.getId());
        }
        return ids;
    }

    private long calcTotalPages(long total, int size) {
        if (size <= 0) {
            return 0L;
        }
        return (total + size - 1) / size;
    }
}
