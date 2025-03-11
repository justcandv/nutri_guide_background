package org.example.nutri_guide_background.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 更新AI交互DTO
 */
@Data
@ToString
public class AIInteractionUpdateDTO {
    
    /**
     * 用户输入
     */
    private String userInput;
    
    /**
     * AI响应
     */
    private String aiResponse;
    
    /**
     * 会话时间
     */
    private LocalDateTime sessionTime;
} 