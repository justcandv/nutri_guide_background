package org.example.nutri_guide_background.controller;

import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CategoryVO;
import org.example.nutri_guide_background.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public Result<List<CategoryVO>> getCategoryList() {
        return productCategoryService.getCategoryList();
    }
} 