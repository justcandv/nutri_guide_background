package org.example.nutri_guide_background.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.nutri_guide_background.entity.OrderItem;

/**
 * 订单项数据访问接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
} 