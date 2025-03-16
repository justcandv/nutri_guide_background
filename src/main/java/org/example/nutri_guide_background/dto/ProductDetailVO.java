package org.example.nutri_guide_background.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ProductDetailVO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;
    private String coverImageUrl;
    private List<String> images;
    private Integer sales;
    private Integer status;
    private BigDecimal weight;
    private Map<String, Object> nutritionInfo;
    private LocalDateTime createTime;
} 