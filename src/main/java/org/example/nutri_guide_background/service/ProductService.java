package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.ProductDTO;
import org.example.nutri_guide_background.dto.ProductDetailVO;
import org.example.nutri_guide_background.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {

    /**
     * 根据分类ID查询商品列表
     *
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Product> listByCategoryId(Long categoryId);
    
    /**
     * 分页查询商品
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param categoryId 分类ID
     * @param keyword 关键词
     * @return 分页结果
     */
    Page<Product> pageProducts(Integer pageNum, Integer pageSize, Long categoryId, String keyword);

    /**
     * 更新商品库存
     *
     * @param productId 商品ID
     * @param quantity 减少的数量
     * @return 是否更新成功
     */
    boolean decreaseStock(Long productId, Integer quantity);

    Result<Page<Product>> getProductList(Long categoryId, String keyword, Integer page, Integer size, String sort);
    
    Result<ProductDetailVO> getProductDetail(Long id);
    
    Result<Long> addProduct(ProductDTO productDTO);
    
    Result<Void> updateProduct(ProductDTO productDTO);
    
    Result<Void> deleteProduct(Long id);
    
    Result<Void> updateProductStatus(Long id, Integer status);
} 