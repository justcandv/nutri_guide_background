package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体实体类
 */
@Data
@TableName("media")
public class Media {
    
    /**
     * 媒体ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 文件存储路径
     */
    private String url;
    
    /**
     * 媒体类型（1-图片 2-视频）
     */
    private Integer type;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 