package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子点赞实体类
 */
@Data
@TableName("post_like")
public class PostLike {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 