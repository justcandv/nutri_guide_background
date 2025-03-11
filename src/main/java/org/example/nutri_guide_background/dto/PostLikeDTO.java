package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 帖子点赞DTO
 */
@Data
public class PostLikeDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
} 