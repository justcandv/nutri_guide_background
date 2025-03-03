package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子实体类
 */
@Data
@TableName("post")
public class Post {
    
    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 帖子标题
     */
    private String title;
    
    /**
     * 帖子内容
     */
    private String content;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 是否删除（0-正常 1-删除）
     */
    @TableLogic
    private Integer isDeleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String avatarUrl;
    
    /**
     * 用户名
     */
    @TableField(exist = false)
    private String userName;
} 