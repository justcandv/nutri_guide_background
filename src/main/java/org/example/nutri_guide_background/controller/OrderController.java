package org.example.nutri_guide_background.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.dto.OrderDTO;
import org.example.nutri_guide_background.dto.OrderQueryDTO;
import org.example.nutri_guide_background.entity.Order;
import org.example.nutri_guide_background.entity.OrderItem;
import org.example.nutri_guide_background.entity.User;
import org.example.nutri_guide_background.service.OrderItemService;
import org.example.nutri_guide_background.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return 0L;
    }

    /**
     * 创建订单
     */
    @PostMapping
    public Result<Long> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        Long orderId = orderService.createOrder(orderDTO, getCurrentUserId());
        return Result.success(orderId);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        boolean success = orderService.cancelOrder(id, getCurrentUserId());
        return success ? Result.success() : Result.error("取消订单失败，订单不存在或无权限");
    }

    /**
     * 支付订单
     */
    @PostMapping("/{id}/pay")
    public Result<Void> payOrder(@PathVariable Long id) {
        boolean success = orderService.payOrder(id, getCurrentUserId());
        return success ? Result.success() : Result.error("支付订单失败，订单不存在或无权限");
    }

    /**
     * 确认收货
     */
    @PostMapping("/{id}/confirm")
    public Result<Void> confirmReceive(@PathVariable Long id) {
        boolean success = orderService.confirmReceive(id, getCurrentUserId());
        return success ? Result.success() : Result.error("确认收货失败，订单不存在或无权限");
    }

    /**
     * 分页查询订单
     */
    @GetMapping
    public Result<Page<Order>> pageOrders(OrderQueryDTO queryDTO) {
        Page<Order> orderPage = orderService.pageUserOrders(queryDTO, getCurrentUserId());
        return Result.success(orderPage);
    }

    /**
     *
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id, getCurrentUserId());
        if (order == null) {
            return Result.error("订单不存在或无权限");
        }
        
        List<OrderItem> orderItems = orderItemService.listByOrderId(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItems", orderItems);
        
        return Result.success(result);
    }
} 