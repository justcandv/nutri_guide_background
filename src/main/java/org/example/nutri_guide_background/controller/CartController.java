package org.example.nutri_guide_background.controller;

import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.CartDTO;
import org.example.nutri_guide_background.dto.CartListVO;
import org.example.nutri_guide_background.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@Validated
public class CartController {

    @Autowired
    private CartService cartService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return 0L;
    }

    @PostMapping("/add")
    public Result<Void> addToCart(@RequestBody @Valid CartDTO cartDTO) {
        return cartService.addToCart(cartDTO, getCurrentUserId());
    }

    @GetMapping("/list")
    public Result<CartListVO> getCartList() {
        return cartService.getCartList(getCurrentUserId());
    }

    @PostMapping("/update")
    public Result<Void> updateCart(@RequestBody @Valid CartDTO cartDTO) {
        return cartService.updateCart(cartDTO, getCurrentUserId());
    }

    @PostMapping("/delete")
    public Result<Void> deleteCart(@RequestBody List<Long> ids) {
        return cartService.deleteCart(ids, getCurrentUserId());
    }

    @PostMapping("/select")
    public Result<Void> selectCart(
            @RequestParam Long id,
            @RequestParam Boolean selected) {
        return cartService.selectCart(id, selected, getCurrentUserId());
    }

    @PostMapping("/clear")
    public Result<Void> clearCart() {
        return cartService.clearCart(getCurrentUserId());
    }
} 