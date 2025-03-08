package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI交互实体类
 */
@Data
@TableName("ai_session")
public class AIInteraction {
    
    /**
     * 交互记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户输入
     */
    private String userInput;
    
    /**
     * AI响应
     */
    private String aiResponse;
    
    /**
     * 会话时间
     */
    private LocalDateTime sessionTime;
} 