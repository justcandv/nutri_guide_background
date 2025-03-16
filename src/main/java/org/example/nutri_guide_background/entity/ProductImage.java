package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "product_image", autoResultMap = true)
public class ProductImage {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("product_id")
    private Long productId;
    
    @TableField("image_url")
    private String imageUrl;
    
    @TableField("sort_order")
    private Integer sortOrder;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 