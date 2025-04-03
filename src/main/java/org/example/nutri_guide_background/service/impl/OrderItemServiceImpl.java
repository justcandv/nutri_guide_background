package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.entity.OrderItem;
import org.example.nutri_guide_background.mapper.OrderItemMapper;
import org.example.nutri_guide_background.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单项服务实现类
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Override
    public List<OrderItem> listByOrderId(Long orderId) {
        return list(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, orderId));
    }
} 