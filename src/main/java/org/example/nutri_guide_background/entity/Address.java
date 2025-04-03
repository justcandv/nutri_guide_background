package org.example.nutri_guide_background.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收货地址实体类
 */
@Data
@TableName("address")
public class Address {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String receiver;
    
    private String phone;
    
    private String province;
    
    private String city;
    
    private String district;
    
    private String detailAddress;
    
    private BigDecimal lat;
    
    private BigDecimal lng;
    
    private Boolean isDefault;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
} 