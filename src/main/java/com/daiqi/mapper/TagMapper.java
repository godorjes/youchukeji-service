package com.daiqi.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiqi.dto.TagCountRow;
import com.daiqi.entity.Tag;
import org.apache.ibatis.annotations.Param;

public interface TagMapper extends BaseMapper<Tag> {

    List<TagCountRow> countCardsByTag();

    Long countCardsByTagId(@Param("tagId") Long tagId);
}
