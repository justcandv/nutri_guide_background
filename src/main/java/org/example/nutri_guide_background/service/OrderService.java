package org.example.nutri_guide_background.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nutri_guide_background.dto.OrderDTO;
import org.example.nutri_guide_background.dto.OrderQueryDTO;
import org.example.nutri_guide_background.entity.Order;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDTO 订单信息
     * @param userId 用户ID
     * @return 订单ID
     */
    Long createOrder(OrderDTO orderDTO, Long userId);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否取消成功
     */
    boolean cancelOrder(Long orderId, Long userId);

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否支付成功
     */
    boolean payOrder(Long orderId, Long userId);

    /**
     * 分页查询用户订单
     *
     * @param queryDTO 查询条件
     * @param userId 用户ID
     * @return 订单分页数据
     */
    Page<Order> pageUserOrders(OrderQueryDTO queryDTO, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 订单详情
     */
    Order getOrderDetail(Long orderId, Long userId);

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否确认成功
     */
    boolean confirmReceive(Long orderId, Long userId);
} 