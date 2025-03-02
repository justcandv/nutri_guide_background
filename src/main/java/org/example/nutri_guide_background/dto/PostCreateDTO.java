package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建帖子DTO
 */
@Data
public class PostCreateDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 帖子标题
     */
    @NotBlank(message = "帖子标题不能为空")
    private String title;
    
    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    private String content;
} 