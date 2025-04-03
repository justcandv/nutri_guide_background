package org.example.nutri_guide_background.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 地址数据传输对象
 */
@Data
public class AddressDTO {
    
    private Long id;
    
    @NotBlank(message = "收货人不能为空")
    private String receiver;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "省份不能为空")
    private String province;
    
    @NotBlank(message = "城市不能为空")
    private String city;
    
    @NotBlank(message = "区/县不能为空")
    private String district;
    
    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;
    
    private BigDecimal lat;
    
    private BigDecimal lng;
    
    private Boolean isDefault;
} 