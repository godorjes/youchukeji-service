package com.daiqi.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiqi.dto.TagCountRow;
import com.daiqi.entity.Tag;

public interface TagMapper extends BaseMapper<Tag> {

    List<TagCountRow> countCardsByTag();

    Long countCardsByTagId(Long tagId);
}
