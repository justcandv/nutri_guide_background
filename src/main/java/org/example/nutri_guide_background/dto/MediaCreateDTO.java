package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建媒体DTO
 */
@Data
public class MediaCreateDTO {
    
    /**
     * 媒体URL
     */
    @NotBlank(message = "媒体URL不能为空")
    private String url;
    
    /**
     * 媒体类型（1-图片 2-视频）
     */
    @NotNull(message = "媒体类型不能为空")
    private Integer type;
} 