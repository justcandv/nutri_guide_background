package org.example.nutri_guide_background.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单项数据传输对象
 */
@Data
public class OrderItemDTO {
    
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量至少为1")
    private Integer quantity;
} 