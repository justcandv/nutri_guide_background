package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.AIInteractionCreateDTO;
import org.example.nutri_guide_background.dto.AIInteractionUpdateDTO;
import org.example.nutri_guide_background.entity.AIInteraction;
import org.example.nutri_guide_background.mapper.AIInteractionMapper;
import org.example.nutri_guide_background.service.AIInteractionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI交互服务实现类
 */
@Service
public class AIInteractionServiceImpl extends ServiceImpl<AIInteractionMapper, AIInteraction> implements AIInteractionService {

    @Override
    public AIInteraction createAIInteraction(AIInteractionCreateDTO dto) {
        AIInteraction aiInteraction = new AIInteraction();
        aiInteraction.setUserId(dto.getUserId());
        aiInteraction.setUserInput(dto.getUserInput());
        aiInteraction.setAiResponse(dto.getAiResponse());
        aiInteraction.setSessionTime(dto.getSessionTime());
        
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
        
        if (dto.getUserInput() != null) {
            aiInteraction.setUserInput(dto.getUserInput());
        }
        
        if (dto.getAiResponse() != null) {
            aiInteraction.setAiResponse(dto.getAiResponse());
        }
        
        if (dto.getSessionTime() != null) {
            aiInteraction.setSessionTime(dto.getSessionTime());
        }
        
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
} 