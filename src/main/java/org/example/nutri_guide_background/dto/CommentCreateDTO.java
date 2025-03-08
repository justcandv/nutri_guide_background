package org.example.nutri_guide_background.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建评论DTO
 */
@Data
public class CommentCreateDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    /**
     * 父评论ID，可选（0表示顶级评论）
     */
    private Long parentId = 0L;
} 