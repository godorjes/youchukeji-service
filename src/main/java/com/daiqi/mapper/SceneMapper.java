package com.daiqi.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiqi.dto.SceneTagRow;
import com.daiqi.entity.Scene;
import org.apache.ibatis.annotations.Param;

public interface SceneMapper extends BaseMapper<Scene> {

    Boolean existsByTagId(Long tagId);

    List<SceneTagRow> selectTagsBySceneIds(@Param("sceneIds") Collection<Long> sceneIds);
}
