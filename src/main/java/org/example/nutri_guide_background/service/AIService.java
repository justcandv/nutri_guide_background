package org.example.nutri_guide_background.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import org.example.nutri_guide_background.dto.AIResponse;

import java.util.List;
import java.util.Map;

/**
 * AI服务接口
 */
public interface AIService {
    
    /**
     * 与AI对话
     *
     * @param userInput 用户输入
     * @param conversationHistory 对话历史 (可为null，表示新对话)
     * @return AI响应
     */
    AIResponse chat(String userInput, String conversationHistory);
    
    /**
     * 解析对话历史记录
     *
     * @param conversationHistory 对话历史记录字符串
     * @return 解析后的消息列表
     */
    List<Map<String, String>> parseConversationHistory(String conversationHistory);
    
    /**
     * 将新的对话追加到历史记录中
     *
     * @param conversationHistory 现有对话历史
     * @param userInput 用户输入
     * @param aiResponse AI响应
     * @return 更新后的对话历史
     */
    String appendToConversationHistory(String conversationHistory, String userInput, String aiResponse);
} 