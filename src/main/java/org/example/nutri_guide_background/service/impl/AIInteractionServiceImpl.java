package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.AIInteractionCreateDTO;
import org.example.nutri_guide_background.dto.AIInteractionUpdateDTO;
import org.example.nutri_guide_background.dto.AIResponse;
import org.example.nutri_guide_background.entity.AIInteraction;
import org.example.nutri_guide_background.mapper.AIInteractionMapper;
import org.example.nutri_guide_background.service.AIInteractionService;
import org.example.nutri_guide_background.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI交互服务实现类
 */
@Service
public class AIInteractionServiceImpl extends ServiceImpl<AIInteractionMapper, AIInteraction> implements AIInteractionService {

    @Autowired
    private AIService aiService;
    
    @Override
    public AIInteraction createAIInteraction(AIInteractionCreateDTO dto) {
        // 创建AI交互记录
        AIInteraction aiInteraction = new AIInteraction();
        aiInteraction.setUserId(dto.getUserId());
        aiInteraction.setUserInput(dto.getUserInput());
        aiInteraction.setSessionTime(LocalDateTime.now());
        
        // 调用AI服务获取响应
        AIResponse aiResponse = aiService.chat(dto.getUserInput(), null);
        
        // 设置AI响应和对话历史
        aiInteraction.setAiResponse(aiResponse.getContent());
        aiInteraction.setConversationHistory(aiResponse.getConversationHistory());
        
        // 保存到数据库
        save(aiInteraction);
        return aiInteraction;
    }

    @Override
    public List<AIInteraction> getAllAIInteractions() {
        LambdaQueryWrapper<AIInteraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AIInteraction::getSessionTime);
        return list(wrapper);
    }

    @Override
    public AIInteraction getAIInteractionById(Long id) {
        return getById(id);
    }

    @Override
    public AIInteraction updateAIInteraction(Long id, AIInteractionUpdateDTO dto) {
        AIInteraction aiInteraction = getById(id);
        if (aiInteraction == null) {
            throw new RuntimeException("AI交互记录不存在");
        }
        
        // 获取用户输入
        String userInput = dto.getUserInput();
        if (userInput == null || userInput.isEmpty()) {
            throw new RuntimeException("用户输入不能为空");
        }
        
        // 调用AI服务获取响应，传入历史记录
        AIResponse aiResponse = aiService.chat(userInput, aiInteraction.getConversationHistory());
        
        // 更新记录
        aiInteraction.setUserInput(userInput);
        aiInteraction.setAiResponse(aiResponse.getContent());
        aiInteraction.setConversationHistory(aiResponse.getConversationHistory());
        aiInteraction.setSessionTime(LocalDateTime.now());
        
        // 保存到数据库
        updateById(aiInteraction);
        return aiInteraction;
    }

    @Override
    public boolean deleteAIInteraction(Long id) {
        AIInteraction aiInteraction = getById(id);
        if (aiInteraction == null) {
            throw new RuntimeException("AI交互记录不存在");
        }
        
        return removeById(id);
    }

    @Override
    public List<AIInteraction> getAIInteractionsByUserId(Long userId) {
        LambdaQueryWrapper<AIInteraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AIInteraction::getUserId, userId);
        return list(wrapper);
    }
} 