package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配送实体类
 */
@Data
@TableName("delivery")
public class Delivery {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long orderId;
    
    private String deliveryNo;
    
    /**
     * 配送状态：0-待分配，1-已分配，2-配送中，3-已送达
     */
    private Integer status;
    
    private BigDecimal lat;
    
    private BigDecimal lng;
    
    private LocalDateTime estimatedTime;
    
    private LocalDateTime actualTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 