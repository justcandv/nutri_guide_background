package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.AddressDTO;
import org.example.nutri_guide_background.entity.Address;
import org.example.nutri_guide_background.mapper.AddressMapper;
import org.example.nutri_guide_background.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址服务实现类
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    @Transactional
    public Long addAddress(AddressDTO addressDTO, Long userId) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setUserId(userId);
        
        // 如果设置为默认地址，则先将其他地址的默认状态取消
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            cancelDefaultAddress(userId);
        }
        
        save(address);
        return address.getId();
    }

    @Override
    @Transactional
    public boolean updateAddress(AddressDTO addressDTO, Long userId) {
        // 查询原地址信息，验证所有权
        Address existingAddress = getOne(new LambdaQueryWrapper<Address>()
                .eq(Address::getId, addressDTO.getId())
                .eq(Address::getUserId, userId));
        
        if (existingAddress == null) {
            return false;
        }
        
        // 如果设置为默认地址，则先将其他地址的默认状态取消
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            cancelDefaultAddress(userId);
        }
        
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setUserId(userId);
        
        return updateById(address);
    }

    @Override
    public boolean deleteAddress(Long addressId, Long userId) {
        return remove(new LambdaQueryWrapper<Address>()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, userId));
    }

    @Override
    public List<Address> listAddressByUserId(Long userId) {
        return list(new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getUpdateTime));
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long addressId, Long userId) {
        // 验证地址是否存在且属于当前用户
        Address address = getOne(new LambdaQueryWrapper<Address>()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, userId));
        
        if (address == null) {
            return false;
        }
        
        // 取消其他默认地址
        cancelDefaultAddress(userId);
        
        // 设置新的默认地址
        return update(new LambdaUpdateWrapper<Address>()
                .eq(Address::getId, addressId)
                .set(Address::getIsDefault, true));
    }

    @Override
    public Address getAddressDetail(Long addressId, Long userId) {
        return getOne(new LambdaQueryWrapper<Address>()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, userId));
    }
    
    /**
     * 取消用户的所有默认地址
     *
     * @param userId 用户ID
     */
    private void cancelDefaultAddress(Long userId) {
        update(new LambdaUpdateWrapper<Address>()
                .eq(Address::getUserId, userId)
                .eq(Address::getIsDefault, true)
                .set(Address::getIsDefault, false));
    }
} 