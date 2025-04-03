package org.example.nutri_guide_background.service;

import org.example.nutri_guide_background.dto.AddressDTO;
import org.example.nutri_guide_background.entity.Address;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {

    /**
     * 添加收货地址
     *
     * @param addressDTO 地址信息
     * @param userId 用户ID
     * @return 新增地址ID
     */
    Long addAddress(AddressDTO addressDTO, Long userId);

    /**
     * 更新收货地址
     *
     * @param addressDTO 地址信息
     * @param userId 用户ID
     * @return 是否更新成功
     */
    boolean updateAddress(AddressDTO addressDTO, Long userId);

    /**
     * 删除收货地址
     *
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteAddress(Long addressId, Long userId);

    /**
     * 查询用户的收货地址列表
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<Address> listAddressByUserId(Long userId);

    /**
     * 设置默认收货地址
     *
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 是否设置成功
     */
    boolean setDefaultAddress(Long addressId, Long userId);

    /**
     * 获取地址详情
     *
     * @param addressId 地址ID
     * @param userId 用户ID
     * @return 地址详情
     */
    Address getAddressDetail(Long addressId, Long userId);
} 