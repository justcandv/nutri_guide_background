package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CartDTO;
import org.example.nutri_guide_background.dto.CartItemVO;
import org.example.nutri_guide_background.dto.CartListVO;
import org.example.nutri_guide_background.entity.Cart;
import org.example.nutri_guide_background.entity.Product;
import org.example.nutri_guide_background.mapper.CartMapper;
import org.example.nutri_guide_background.mapper.ProductMapper;
import org.example.nutri_guide_background.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public Result<Void> addToCart(CartDTO cartDTO, Long userId) {
        // 检查商品是否存在
        Product product = productMapper.selectById(cartDTO.getProductId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        // 检查商品库存
        if (product.getStock() < cartDTO.getQuantity()) {
            return Result.error("商品库存不足");
        }
        
        // 检查购物车中是否已存在该商品
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, cartDTO.getProductId());
        Cart existingCart = cartMapper.selectOne(wrapper);
        
        if (existingCart != null) {
            // 更新数量
            existingCart.setQuantity(existingCart.getQuantity() + cartDTO.getQuantity());
            existingCart.setSelected(true);
            cartMapper.updateById(existingCart);
        } else {
            // 添加新记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(cartDTO.getProductId());
            cart.setQuantity(cartDTO.getQuantity());
            cart.setSelected(true);
            cartMapper.insert(cart);
        }
        
        return Result.success(null);
    }

    @Override
    public Result<CartListVO> getCartList(Long userId) {
        List<CartItemVO> cartItems = cartMapper.selectCartItemsByUserId(userId);
        
        // 计算总价和选中数量
        BigDecimal totalAmount = BigDecimal.ZERO;
        int selectedCount = 0;
        
        for (CartItemVO item : cartItems) {
            if (item.getSelected()) {
                totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                selectedCount += item.getQuantity();
            }
        }
        
        CartListVO cartListVO = new CartListVO();
        cartListVO.setItems(cartItems);
        cartListVO.setTotalAmount(totalAmount);
        cartListVO.setSelectedCount(selectedCount);
        
        return Result.success(cartListVO);
    }

    @Override
    @Transactional
    public Result<Void> updateCart(CartDTO cartDTO, Long userId) {
        if (cartDTO.getId() == null) {
            return Result.error("购物车ID不能为空");
        }
        
        // 检查购物车项是否存在
        Cart cart = cartMapper.selectById(cartDTO.getId());
        if (cart == null || !cart.getUserId().equals(userId)) {
            return Result.error("购物车项不存在");
        }
        
        // 检查商品库存
        Product product = productMapper.selectById(cart.getProductId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        if (product.getStock() < cartDTO.getQuantity()) {
            return Result.error("商品库存不足");
        }
        
        // 更新购物车
        cart.setQuantity(cartDTO.getQuantity());
        if (cartDTO.getSelected() != null) {
            cart.setSelected(cartDTO.getSelected());
        }
        cartMapper.updateById(cart);
        
        return Result.success(null);
    }

    @Override
    @Transactional
    public Result<Void> deleteCart(List<Long> ids, Long userId) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("购物车ID不能为空");
        }
        
        // 检查购物车项是否属于当前用户
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Cart::getId, ids)
               .eq(Cart::getUserId, userId);
        cartMapper.delete(wrapper);
        
        return Result.success(null);
    }

    @Override
    @Transactional
    public Result<Void> selectCart(Long id, Boolean selected, Long userId) {
        if (id == null) {
            return Result.error("购物车ID不能为空");
        }
        
        // 检查购物车项是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null || !cart.getUserId().equals(userId)) {
            return Result.error("购物车项不存在");
        }
        
        // 更新选中状态
        cart.setSelected(selected);
        cartMapper.updateById(cart);
        
        return Result.success(null);
    }

    @Override
    @Transactional
    public Result<Void> clearCart(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        cartMapper.delete(wrapper);
        
        return Result.success(null);
    }
} 