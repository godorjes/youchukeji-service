package com.daiqi.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiqi.entity.SceneCardCheck;
import org.apache.ibatis.annotations.Param;

public interface SceneCardCheckMapper extends BaseMapper<SceneCardCheck> {

    SceneCardCheck selectByUserSceneCard(@Param("userId") Long userId,
                                         @Param("sceneId") Long sceneId,
                                         @Param("cardId") Long cardId);

    List<SceneCardCheck> selectByUserSceneCardIds(@Param("userId") Long userId,
                                                  @Param("sceneId") Long sceneId,
                                                  @Param("cardIds") Collection<Long> cardIds);

    Long countCheckedByUserSceneCardIds(@Param("userId") Long userId,
                                        @Param("sceneId") Long sceneId,
                                        @Param("cardIds") Collection<Long> cardIds);

    int deleteByUserScene(@Param("userId") Long userId, @Param("sceneId") Long sceneId);
}
