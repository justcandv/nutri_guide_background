package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CategoryVO;
import org.example.nutri_guide_background.entity.ProductCategory;
import org.example.nutri_guide_background.mapper.ProductCategoryMapper;
import org.example.nutri_guide_background.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Result<List<CategoryVO>> getCategoryList() {
        // 查询所有分类
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSortOrder);
        List<ProductCategory> allCategories = productCategoryMapper.selectList(wrapper);
        
        // 转换为VO对象
        List<CategoryVO> allCategoryVOs = allCategories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());
        
        // 构建分类树
        List<CategoryVO> rootCategories = new ArrayList<>();
        Map<Long, List<CategoryVO>> childrenMap = allCategoryVOs.stream()
                .filter(category -> category.getParentId() != null && category.getParentId() != 0)
                .collect(Collectors.groupingBy(CategoryVO::getParentId));
        
        for (CategoryVO category : allCategoryVOs) {
            if (category.getParentId() == null || category.getParentId() == 0) {
                rootCategories.add(category);
            }
        }
        
        // 递归设置子分类
        setChildren(rootCategories, childrenMap);
        
        return Result.success(rootCategories);
    }
    
    private void setChildren(List<CategoryVO> categories, Map<Long, List<CategoryVO>> childrenMap) {
        for (CategoryVO category : categories) {
            List<CategoryVO> children = childrenMap.get(category.getId());
            if (children != null && !children.isEmpty()) {
                category.setChildren(children);
                setChildren(children, childrenMap);
            }
        }
    }
} 