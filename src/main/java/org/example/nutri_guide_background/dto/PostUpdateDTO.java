package org.example.nutri_guide_background.dto;

import lombok.Data;

/**
 * 更新帖子DTO
 */
@Data
public class PostUpdateDTO {
    
    /**
     * 帖子标题
     */
    private String title;
    
    /**
     * 帖子内容
     */
    private String content;
    
    /**
     * 是否删除
     */
    private Boolean isDeleted;
} 