package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.entity.Delivery;
import org.example.nutri_guide_background.entity.Order;
import org.example.nutri_guide_background.mapper.DeliveryMapper;
import org.example.nutri_guide_background.mapper.OrderMapper;
import org.example.nutri_guide_background.service.DeliveryService;
import org.example.nutri_guide_background.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 配送服务实现类
 */
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {

    private final OrderService orderService;
    
    @Autowired
    private OrderMapper orderMapper;

    public DeliveryServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public String createDelivery(Long orderId) {
        // 检查订单是否存在
        Order order = orderService.getOrderDetail(orderId, null); // 管理端可以传null todo
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        
        // 检查订单状态，只有已支付待配送的订单可以创建配送
        if (order.getStatus() != 1) {
            throw new IllegalStateException("当前订单状态不可创建配送");
        }
        
        // 检查是否已经存在配送单
        Delivery existingDelivery = getByOrderId(orderId);
        if (existingDelivery != null) {
            return existingDelivery.getDeliveryNo();
        }
        
        // 创建配送单
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setDeliveryNo(generateDeliveryNo());
        delivery.setStatus(0); // 待分配
        
        // 保存配送单
        save(delivery);
        
        // 更新订单状态为配送中
        orderMapper.update(null, new LambdaUpdateWrapper<Order>()
                .eq(Order::getId, orderId)
                .set(Order::getStatus, 2));
        
        return delivery.getDeliveryNo();
    }

    @Override
    public boolean updateDeliveryStatus(Long deliveryId, Integer status) {
        Delivery delivery = getById(deliveryId);
        if (delivery == null) {
            return false;
        }
        
        delivery.setStatus(status);
        return updateById(delivery);
    }

    @Override
    public boolean updateDeliveryLocation(Long deliveryId, BigDecimal lat, BigDecimal lng) {
        Delivery delivery = getById(deliveryId);
        if (delivery == null) {
            return false;
        }
        
        delivery.setLat(lat);
        delivery.setLng(lng);
        return updateById(delivery);
    }

    @Override
    @Transactional
    public boolean confirmDelivery(Long deliveryId) {
        Delivery delivery = getById(deliveryId);
        if (delivery == null) {
            return false;
        }
        
        // 只有配送中状态可以确认送达
        if (delivery.getStatus() != 2) {
            throw new IllegalStateException("当前配送状态不可确认送达");
        }
        
        // 更新配送状态为已送达
        delivery.setStatus(3); // 已送达
        delivery.setActualTime(LocalDateTime.now());
        
        // 更新订单状态为已完成
        orderMapper.update(null, new LambdaUpdateWrapper<Order>()
                .eq(Order::getId, delivery.getOrderId())
                .set(Order::getStatus, 3));
        
        return updateById(delivery);
    }

    @Override
    public Delivery getByOrderId(Long orderId) {
        return getOne(new LambdaQueryWrapper<Delivery>()
                .eq(Delivery::getOrderId, orderId));
    }
    
    /**
     * 生成配送单号
     */
    private String generateDeliveryNo() {
        return "D" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }
} 