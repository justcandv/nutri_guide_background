package org.example.nutri_guide_background.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("user_input")
    private String userInput;
    
    /**
     * AI响应
     */
    @JsonProperty("ai_response")
    private String aiResponse;
    
    /**
     * 会话时间
     */
    @JsonProperty("session_time")
    private LocalDateTime sessionTime;
} 