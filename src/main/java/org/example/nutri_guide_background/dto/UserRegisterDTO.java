package org.example.nutri_guide_background.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    private String email;
    private String avatarUrl;
    private Integer gender;
    private Double height;
    private Double weight;
} 