package org.example.nutri_guide_background.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.ProductDTO;
import org.example.nutri_guide_background.dto.ProductDetailVO;
import org.example.nutri_guide_background.entity.Product;
import org.example.nutri_guide_background.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<Page<Product>> getProductList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getProductList(categoryId, keyword, page, size, sort);
    }

    @GetMapping("/{id}")
    public Result<ProductDetailVO> getProductDetail(@PathVariable Long id) {
        return productService.getProductDetail(id);
    }

    @PostMapping("/admin/add")
    public Result<Long> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @PostMapping("/admin/update")
    public Result<Void> updateProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productService.updateProduct(productDTO);
    }

    @PostMapping("/admin/{id}/delete")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/admin/{id}/status")
    public Result<Void> updateProductStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return productService.updateProductStatus(id, status);
    }
} 