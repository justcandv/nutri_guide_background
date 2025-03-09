package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 * 继续AI对话DTO
 */
@Data
@ToString
public class AIContinueDTO {
    
    /**
     * 用户输入
     */
    @NotBlank(message = "用户输入不能为空")
    private String userInput;
} 