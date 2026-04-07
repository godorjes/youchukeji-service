package com.daiqi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SceneTagMapper {

    int deleteBySceneId(@Param("sceneId") Long sceneId);

    int insertSceneTags(@Param("sceneId") Long sceneId, @Param("tagIds") List<Long> tagIds);
}
