package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@TableName(value = "product", autoResultMap = true)
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("category_id")
    private Long categoryId;
    
    @TableField("name")
    private String name;
    
    @TableField("description")
    private String description;
    
    @TableField("price")
    private BigDecimal price;
    
    @TableField("stock")
    private Integer stock;
    
    @TableField("cover_image_url")
    private String coverImageUrl;
    
    @TableField("sales")
    private Integer sales;
    
    @TableField("status")
    private Integer status;
    
    @TableField("weight")
    private BigDecimal weight;
    
    @TableField(value = "nutrition_info", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> nutritionInfo;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 