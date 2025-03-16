package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.ProductDTO;
import org.example.nutri_guide_background.dto.ProductDetailVO;
import org.example.nutri_guide_background.entity.Product;

import java.util.Map;

public interface ProductService {
    Result<Page<Product>> getProductList(Long categoryId, String keyword, Integer page, Integer size, String sort);
    
    Result<ProductDetailVO> getProductDetail(Long id);
    
    Result<Long> addProduct(ProductDTO productDTO);
    
    Result<Void> updateProduct(ProductDTO productDTO);
    
    Result<Void> deleteProduct(Long id);
    
    Result<Void> updateProductStatus(Long id, Integer status);
} 