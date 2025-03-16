package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductDTO {
    private Long id;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @NotBlank(message = "商品名称不能为空")
    private String name;
    
    private String description;
    
    @NotNull(message = "商品价格不能为空")
    @Positive(message = "商品价格必须大于0")
    private BigDecimal price;
    
    @NotNull(message = "库存不能为空")
    @PositiveOrZero(message = "库存不能小于0")
    private Integer stock;
    
    private String coverImageUrl;
    
    private List<String> images;
    
    private BigDecimal weight;
    
    private Map<String, Object> nutritionInfo;
    
    private Integer status = 1;
} 