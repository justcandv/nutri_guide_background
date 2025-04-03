package org.example.nutri_guide_background.controller;

import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.AddressDTO;
import org.example.nutri_guide_background.entity.Address;
import org.example.nutri_guide_background.entity.User;
import org.example.nutri_guide_background.service.AddressService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址控制器
 */
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return 0L;
    }

    /**
     * 添加收货地址
     */
    @PostMapping
    public Result<Long> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        Long addressId = addressService.addAddress(addressDTO,getCurrentUserId());
        return Result.success(addressId);
    }

    /**
     * 更新收货地址
     */
    @PutMapping("/{id}")
    public Result<Void> updateAddress(@PathVariable Long id,
                                     @Valid @RequestBody AddressDTO addressDTO) {
        addressDTO.setId(id);
        boolean success = addressService.updateAddress(addressDTO, getCurrentUserId());
        return success ? Result.success() : Result.error("更新地址失败，地址不存在或无权限");
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id) {
        boolean success = addressService.deleteAddress(id, getCurrentUserId());
        return success ? Result.success() : Result.error("删除地址失败，地址不存在或无权限");
    }

    /**
     * 获取用户的收货地址列表
     */
    @GetMapping
    public Result<List<Address>> listAddresses() {
        List<Address> addresses = addressService.listAddressByUserId(getCurrentUserId());
        return Result.success(addresses);
    }

    /**
     * 设置默认收货地址
     */
    @PutMapping("/{id}/default")
    public Result<Void> setDefaultAddress(@PathVariable Long id) {
        boolean success = addressService.setDefaultAddress(id, getCurrentUserId());
        return success ? Result.success() : Result.error("设置默认地址失败，地址不存在或无权限");
    }

    /**
     * 获取地址详情
     */
    @GetMapping("/{id}")
    public Result<Address> getAddressDetail(@PathVariable Long id) {
        Address address = addressService.getAddressDetail(id, getCurrentUserId());
        return address != null ? Result.success(address) : Result.error("地址不存在或无权限");
    }
} 