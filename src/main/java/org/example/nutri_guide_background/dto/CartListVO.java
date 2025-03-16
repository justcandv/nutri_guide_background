package org.example.nutri_guide_background.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartListVO {
    private List<CartItemVO> items;
    private BigDecimal totalAmount;
    private Integer selectedCount;
} 