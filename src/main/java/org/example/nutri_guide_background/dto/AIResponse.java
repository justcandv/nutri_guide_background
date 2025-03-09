package org.example.nutri_guide_background.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    
    /**
     * AI的回复内容
     */
    private String content;
    
    /**
     * AI的思考过程（可选）
     */
    private String reasoningContent;
    
    /**
     * 完整的对话历史
     */
    private String conversationHistory;
} 