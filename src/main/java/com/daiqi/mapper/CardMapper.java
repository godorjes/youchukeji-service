package com.daiqi.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiqi.dto.CardTagRow;
import com.daiqi.entity.Card;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface CardMapper extends BaseMapper<Card> {

    List<Card> selectPageByTagId(@Param("tagId") Long tagId, @Param("offset") int offset, @Param("size") int size);

    Long countByTagId(@Param("tagId") Long tagId);

    Boolean existsByTagId(@Param("tagId") Long tagId);

    List<Card> selectCardsBySceneId(@Param("sceneId") Long sceneId);

    List<Long> selectCardIdsBySceneId(@Param("sceneId") Long sceneId);

    Long countCardsBySceneId(@Param("sceneId") Long sceneId);

    Boolean existsCardInScene(@Param("sceneId") Long sceneId, @Param("cardId") Long cardId);

    List<CardTagRow> selectTagsByCardIds(@Param("cardIds") Collection<Long> cardIds);
}
