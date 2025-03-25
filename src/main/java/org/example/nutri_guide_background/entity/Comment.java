package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment {
    
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String username;
    
    /**
     * 用户头像URL
     */
    @TableField(exist = false)
    private String avatarUrl;
    
    /**
     * 父评论ID（0表示顶级评论）
     */
    private Long parentId;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 