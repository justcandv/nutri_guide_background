package org.example.nutri_guide_background.service;

import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CartDTO;
import org.example.nutri_guide_background.dto.CartListVO;

import java.util.List;

public interface CartService {
    Result<Void> addToCart(CartDTO cartDTO, Long userId);
    
    Result<CartListVO> getCartList(Long userId);
    
    Result<Void> updateCart(CartDTO cartDTO, Long userId);
    
    Result<Void> deleteCart(List<Long> ids, Long userId);
    
    Result<Void> selectCart(Long id, Boolean selected, Long userId);
    
    Result<Void> clearCart(Long userId);
} 