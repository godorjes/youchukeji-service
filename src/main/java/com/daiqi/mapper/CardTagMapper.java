package com.daiqi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CardTagMapper {

    int deleteByCardId(@Param("cardId") Long cardId);

    int insertCardTags(@Param("cardId") Long cardId, @Param("tagIds") List<Long> tagIds);
}
