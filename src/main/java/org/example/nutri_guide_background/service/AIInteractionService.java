package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.dto.AIInteractionCreateDTO;
import org.example.nutri_guide_background.dto.AIInteractionUpdateDTO;
import org.example.nutri_guide_background.entity.AIInteraction;

import java.util.List;

/**
 * AI交互服务接口
 */
public interface AIInteractionService extends IService<AIInteraction> {
    
    /**
     * 创建AI交互记录
     *
     * @param dto AI交互创建DTO
     * @return 创建的AI交互记录
     */
    AIInteraction createAIInteraction(AIInteractionCreateDTO dto);
    
    /**
     * 获取所有AI交互记录
     *
     * @return AI交互记录列表
     */
    List<AIInteraction> getAllAIInteractions();
    
    /**
     * 根据ID获取AI交互记录
     *
     * @param id AI交互记录ID
     * @return AI交互记录
     */
    AIInteraction getAIInteractionById(Long id);
    
    /**
     * 更新AI交互记录
     *
     * @param id AI交互记录ID
     * @param dto 更新AI交互DTO
     * @return 更新后的AI交互记录
     */
    AIInteraction updateAIInteraction(Long id, AIInteractionUpdateDTO dto);
    
    /**
     * 删除AI交互记录
     *
     * @param id AI交互记录ID
     * @return 是否删除成功
     */
    boolean deleteAIInteraction(Long id);

    List<AIInteraction> getAIInteractionsByUserId(Long userId);
} 