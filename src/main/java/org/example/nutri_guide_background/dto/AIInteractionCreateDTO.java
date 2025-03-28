package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;


/**
 * 创建AI交互DTO
 */
@Data
@ToString
public class AIInteractionCreateDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 用户输入
     */
    @NotBlank(message = "用户输入不能为空")
    private String userInput;
} 