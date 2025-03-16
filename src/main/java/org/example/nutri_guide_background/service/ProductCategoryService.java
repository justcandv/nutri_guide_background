package org.example.nutri_guide_background.service;

import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CategoryVO;
import org.example.nutri_guide_background.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    Result<List<CategoryVO>> getCategoryList();
} 