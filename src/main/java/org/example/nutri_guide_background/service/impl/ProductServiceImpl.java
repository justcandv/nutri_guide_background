package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.ProductDTO;
import org.example.nutri_guide_background.dto.ProductDetailVO;
import org.example.nutri_guide_background.entity.Product;
import org.example.nutri_guide_background.entity.ProductCategory;
import org.example.nutri_guide_background.entity.ProductImage;
import org.example.nutri_guide_background.mapper.ProductCategoryMapper;
import org.example.nutri_guide_background.mapper.ProductImageMapper;
import org.example.nutri_guide_background.mapper.ProductMapper;
import org.example.nutri_guide_background.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private ProductCategoryMapper categoryMapper;
    
    @Autowired
    private ProductImageMapper productImageMapper;

    @Override
    public List<Product> listByCategoryId(Long categoryId) {
        return list(new LambdaQueryWrapper<Product>()
                .eq(Product::getCategoryId, categoryId)
                .eq(Product::getStatus, 1)  // 上架状态
                .orderByDesc(Product::getCreateTime));
    }

    @Override
    public Page<Product> pageProducts(Integer pageNum, Integer pageSize, Long categoryId, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<Product>()
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .like(StringUtils.hasText(keyword), Product::getName, keyword)
                .eq(Product::getStatus, 1)  // 上架状态
                .orderByDesc(Product::getCreateTime);
        
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean decreaseStock(Long productId, Integer quantity) {
        Product product = getById(productId);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }
        
        if (product.getStock() < quantity) {
            throw new IllegalStateException("商品库存不足");
        }
        
        boolean result = update(new LambdaUpdateWrapper<Product>()
                .eq(Product::getId, productId)
                .ge(Product::getStock, quantity)
                .setSql("stock = stock - " + quantity)
                .setSql("sales = sales + " + quantity));
        
        if (!result) {
            throw new IllegalStateException("更新库存失败，可能库存不足");
        }
        
        return true;
    }

    // 获取商品主图，用于订单显示
    public String getMainImage(Long productId) {
        LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductImage::getProductId, productId);
        wrapper.orderByAsc(ProductImage::getSortOrder);
        wrapper.last("LIMIT 1");
        
        ProductImage image = productImageMapper.selectOne(wrapper);
        return image != null ? image.getImageUrl() : null;
    }

    @Override
    public Result<Page<Product>> getProductList(Long categoryId, String keyword, Integer page, Integer size, String sort) {
        Page<Product> pageParam = new Page<>(page, size);
        
        if (sort != null) {
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            if ("price_asc".equals(sort)) {
                queryWrapper.orderByAsc("price");
            } else if ("price_desc".equals(sort)) {
                queryWrapper.orderByDesc("price");
            } else if ("sales_desc".equals(sort)) {
                queryWrapper.orderByDesc("sales");
            }
        }
        
        IPage<Product> productIPage = productMapper.selectPageWithCategory(pageParam, categoryId, keyword);
        Page<Product> productPage = new Page<>(productIPage.getCurrent(), productIPage.getSize(), productIPage.getTotal());
        productPage.setRecords(productIPage.getRecords());
        
        return Result.success(productPage);
    }

    @Override
    public Result<ProductDetailVO> getProductDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        ProductCategory category = categoryMapper.selectById(product.getCategoryId());
        
        LambdaQueryWrapper<ProductImage> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(ProductImage::getProductId, id);
        imageWrapper.orderByAsc(ProductImage::getSortOrder);
        List<ProductImage> images = productImageMapper.selectList(imageWrapper);
        
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);
        
        if (category != null) {
            productDetailVO.setCategoryName(category.getName());
        }
        
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = images.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
            productDetailVO.setImages(imageUrls);
        } else {
            productDetailVO.setImages(new ArrayList<>());
        }
        
        return Result.success(productDetailVO);
    }

    @Override
    @Transactional
    public Result<Long> addProduct(ProductDTO productDTO) {
        // 保存商品基本信息
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setSales(0); // 新商品销量为0
        productMapper.insert(product);
        
        // 保存商品图片
        if (productDTO.getImages() != null && !productDTO.getImages().isEmpty()) {
            int sortOrder = 0;
            for (String imageUrl : productDTO.getImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setProductId(product.getId());
                productImage.setImageUrl(imageUrl);
                productImage.setSortOrder(sortOrder++);
                productImageMapper.insert(productImage);
            }
        }
        
        return Result.success(product.getId());
    }

    @Override
    @Transactional
    public Result<Void> updateProduct(ProductDTO productDTO) {
        if (productDTO.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        
        Product existingProduct = productMapper.selectById(productDTO.getId());
        if (existingProduct == null) {
            return Result.error("商品不存在");
        }
        
        // 更新商品基本信息
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productMapper.updateById(product);
        
        // 如果有图片更新，先删除原有图片，再保存新图片
        if (productDTO.getImages() != null) {
            LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductImage::getProductId, productDTO.getId());
            productImageMapper.delete(wrapper);
            
            int sortOrder = 0;
            for (String imageUrl : productDTO.getImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setProductId(productDTO.getId());
                productImage.setImageUrl(imageUrl);
                productImage.setSortOrder(sortOrder++);
                productImageMapper.insert(productImage);
            }
        }
        
        return Result.success(null);
    }

    @Override
    @Transactional
    public Result<Void> deleteProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        // 逻辑删除商品
        productMapper.deleteById(id);
        
        // 逻辑删除商品图片
        LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductImage::getProductId, id);
        productImageMapper.delete(wrapper);
        
        return Result.success(null);
    }

    @Override
    public Result<Void> updateProductStatus(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        product.setStatus(product.getStatus() == 1 ? 0 : 1);
        productMapper.updateById(product);
        
        return Result.success(null);
    }
} 