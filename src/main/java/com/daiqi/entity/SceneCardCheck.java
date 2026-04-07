package com.daiqi.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("scene_card_check")
public class SceneCardCheck {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("scene_id")
    private Long sceneId;

    @TableField("card_id")
    private Long cardId;

    private boolean checked;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
