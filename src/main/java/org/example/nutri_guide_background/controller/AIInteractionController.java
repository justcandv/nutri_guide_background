package org.example.nutri_guide_background.controller;

import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.AIContinueDTO;
import org.example.nutri_guide_background.dto.AIInteractionCreateDTO;
import org.example.nutri_guide_background.dto.AIInteractionUpdateDTO;
import org.example.nutri_guide_background.entity.AIInteraction;
import org.example.nutri_guide_background.service.AIInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AI交互控制器
 */
@RestController
@RequestMapping("/ai")
public class AIInteractionController {
    
    @Autowired
    private AIInteractionService aiInteractionService;
    
    /**
     * 创建AI交互记录并与AI对话
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<AIInteraction> createAIInteraction(@Valid @RequestBody AIInteractionCreateDTO dto) {
        try {
            AIInteraction aiInteraction = aiInteractionService.createAIInteraction(dto);
            return Result.success(aiInteraction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有AI交互记录
     */
    @GetMapping
    public Result<List<AIInteraction>> getAllAIInteractions() {
        List<AIInteraction> aiInteractions = aiInteractionService.getAllAIInteractions();
        return Result.success(aiInteractions);
    }

    /**
     * 根据用户ID获取AI交互记录
     */
    @GetMapping("/{userId}/userId")
    public Result<List<AIInteraction>> getAIInteractionsByUserId(@PathVariable Long userId) {
        List<AIInteraction> aiInteractions = aiInteractionService.getAIInteractionsByUserId(userId);
        return Result.success(aiInteractions);
    }

    
    /**
     * 获取单个AI交互记录
     */
    @GetMapping("/{id}")
    public Result<AIInteraction> getAIInteractionById(@PathVariable Long id) {
        try {
            AIInteraction aiInteraction = aiInteractionService.getAIInteractionById(id);
            if (aiInteraction == null) {
                return Result.error("AI交互记录不存在");
            }
            return Result.success(aiInteraction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 继续AI对话
     */
    @PutMapping("/{id}")
    public Result<AIInteraction> continueAIInteraction(@PathVariable Long id, @Valid @RequestBody AIContinueDTO dto) {
        try {
            // 创建更新DTO
            AIInteractionUpdateDTO updateDTO = new AIInteractionUpdateDTO();
            updateDTO.setUserInput(dto.getUserInput());
            
            // 更新交互并获取AI响应
            AIInteraction aiInteraction = aiInteractionService.updateAIInteraction(id, updateDTO);
            return Result.success(aiInteraction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除AI交互记录
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteAIInteraction(@PathVariable Long id) {
        try {
            boolean deleted = aiInteractionService.deleteAIInteraction(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 