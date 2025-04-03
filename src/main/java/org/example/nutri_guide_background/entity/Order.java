package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String orderNo;
    
    private BigDecimal totalAmount;
    
    private BigDecimal actualAmount;
    
    /**
     * 订单状态：0-待支付，1-已支付待配送，2-配送中，3-已完成，4-已取消
     */
    private Integer status;
    
    private Long addressId;
    
    private LocalDateTime paymentTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String remark;
    
    @TableLogic
    private Integer deleted;
} 