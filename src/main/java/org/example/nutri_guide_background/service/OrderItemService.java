package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.nutri_guide_background.entity.OrderItem;

import java.util.List;

/**
 * 订单项服务接口
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID查询订单项
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> listByOrderId(Long orderId);
} 