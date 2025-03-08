package org.example.nutri_guide_background.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 创建AI交互DTO
 */
@Data
@ToString
public class AIInteractionCreateDTO {
    
    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 用户输入
     */
    @JsonProperty("user_input")
    @NotBlank(message = "用户输入不能为空")
    private String userInput;
    
    /**
     * AI响应
     */
    @JsonProperty("ai_response")
    @NotBlank(message = "AI响应不能为空")
    private String aiResponse;
    
    /**
     * 会话时间
     */
    @JsonProperty("session_time")
    private LocalDateTime sessionTime;
} 