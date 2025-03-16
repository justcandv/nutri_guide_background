package org.example.nutri_guide_background.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nutri_guide_background.entity.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    IPage<Product> selectPageWithCategory(Page<Product> page, 
                                         @Param("categoryId") Long categoryId, 
                                         @Param("keyword") String keyword);
} 