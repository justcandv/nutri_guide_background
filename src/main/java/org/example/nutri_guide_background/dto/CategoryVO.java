package org.example.nutri_guide_background.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private String iconUrl;
    private List<CategoryVO> children;
} 