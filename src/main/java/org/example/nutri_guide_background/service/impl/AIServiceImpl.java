package org.example.nutri_guide_background.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.nutri_guide_background.dto.AIResponse;
import org.example.nutri_guide_background.service.AIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI服务实现类
 */
@Service
public class AIServiceImpl implements AIService {

    @Value("${dashscope.api.key}")
    private String apiKey;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public AIResponse chat(String userInput, String conversationHistory) {
        try {
            Generation gen = new Generation();
            List<Message> messages = new ArrayList<>();
            
            // 如果有历史记录，解析并添加到消息中
            if (conversationHistory != null && !conversationHistory.isEmpty()) {
                List<Map<String, String>> historyMessages = parseConversationHistory(conversationHistory);
                for (Map<String, String> msg : historyMessages) {
                    String role = msg.get("role");
                    String content = msg.get("content");
                    
                    Message message = Message.builder()
                            .role(role)
                            .content(content)
                            .build();
                    messages.add(message);
                }
            }
            
            // 添加用户当前的输入
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(userInput)
                    .build();
            messages.add(userMsg);
            
            // 调用AI服务
            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey.isEmpty() ? System.getenv("DASHSCOPE_API_KEY") : apiKey)
                    .model("deepseek-r1")
                    .messages(messages)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();
            
            GenerationResult result = gen.call(param);
            
            // 获取AI响应
            String aiContent = result.getOutput().getChoices().get(0).getMessage().getContent();
            String reasoningContent = result.getOutput().getChoices().get(0).getMessage().getReasoningContent();
            
            // 将AI响应添加到历史记录
            String updatedHistory = conversationHistory;
            updatedHistory = appendToConversationHistory(updatedHistory, userInput, aiContent);
            
            return new AIResponse(aiContent, reasoningContent, updatedHistory);
            
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException("调用AI服务时发生错误: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Map<String, String>> parseConversationHistory(String conversationHistory) {
        if (conversationHistory == null || conversationHistory.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(conversationHistory, new TypeReference<List<Map<String, String>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析对话历史记录失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String appendToConversationHistory(String conversationHistory, String userInput, String aiResponse) {
        List<Map<String, String>> historyMessages = new ArrayList<>();
        
        // 解析现有历史记录
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            historyMessages = parseConversationHistory(conversationHistory);
        }
        
        // 添加用户输入
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", Role.USER.getValue());
        userMessage.put("content", userInput);
        historyMessages.add(userMessage);
        
        // 添加AI响应
        Map<String, String> aiMessage = new HashMap<>();
        aiMessage.put("role", Role.ASSISTANT.getValue());
        aiMessage.put("content", aiResponse);
        historyMessages.add(aiMessage);
        
        // 序列化为JSON
        try {
            return objectMapper.writeValueAsString(historyMessages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化对话历史记录失败: " + e.getMessage(), e);
        }
    }
} 