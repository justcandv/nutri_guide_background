package org.example.nutri_guide_background.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrderDTO {
    
    private Long id;
    
    @NotNull(message = "地址ID不能为空")
    private Long addressId;
    
    @Valid
    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemDTO> orderItems;
    
    private String remark;
    
    // 生成订单时自动计算
    private BigDecimal totalAmount;
} 