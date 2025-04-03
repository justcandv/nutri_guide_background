package org.example.nutri_guide_background.dto;

import lombok.Data;

/**
 * 订单查询数据传输对象
 */
@Data
public class OrderQueryDTO {
    
    private Integer status;
    
    private String orderNo;
    
    private Integer pageNum = 1;
    
    private Integer pageSize = 10;
} 