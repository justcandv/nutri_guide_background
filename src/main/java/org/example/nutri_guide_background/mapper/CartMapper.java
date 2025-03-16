package org.example.nutri_guide_background.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nutri_guide_background.dto.CartItemVO;
import org.example.nutri_guide_background.entity.Cart;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    List<CartItemVO> selectCartItemsByUserId(@Param("userId") Long userId);
} 