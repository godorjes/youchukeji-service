package com.daiqi.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiqi.dto.CardTagRow;
import com.daiqi.entity.Card;
import org.apache.ibatis.annotations.Param;

public interface CardMapper extends BaseMapper<Card> {

    List<Card> selectPageByTagId(@Param("tagId") Long tagId, @Param("offset") int offset, @Param("size") int size);

    Long countByTagId(Long tagId);

    Boolean existsByTagId(Long tagId);

    List<Card> selectCardsBySceneId(Long sceneId);

    List<Long> selectCardIdsBySceneId(Long sceneId);

    Long countCardsBySceneId(Long sceneId);

    Boolean existsCardInScene(@Param("sceneId") Long sceneId, @Param("cardId") Long cardId);

    List<CardTagRow> selectTagsByCardIds(@Param("cardIds") Collection<Long> cardIds);
}
