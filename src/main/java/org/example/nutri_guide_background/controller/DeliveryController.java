package org.example.nutri_guide_background.controller;

import jakarta.validation.Valid;
import org.example.nutri_guide_background.common.Result;
import org.example.nutri_guide_background.entity.Delivery;
import org.example.nutri_guide_background.entity.User;
import org.example.nutri_guide_background.service.DeliveryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 配送控制器
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * 管理员创建配送单
     */
    @PostMapping("/admin/create/{orderId}")
    public Result<String> createDelivery(@PathVariable Long orderId) {
        // 注意：此接口仅供管理员使用，需要添加权限控制
        String deliveryNo = deliveryService.createDelivery(orderId);
        return Result.success(deliveryNo);
    }

    /**
     * 管理员更新配送状态
     */
    @PutMapping("/admin/{id}/status/{status}")
    public Result<Void> updateDeliveryStatus(@PathVariable Long id, 
                                         @PathVariable Integer status) {
        // 注意：此接口仅供管理员使用，需要添加权限控制
        boolean success = deliveryService.updateDeliveryStatus(id, status);
        return success ? Result.success() : Result.error("更新配送状态失败");
    }

    /**
     * 管理员更新配送位置
     */
    @PutMapping("/admin/{id}/location")
    public Result<Void> updateDeliveryLocation(@PathVariable Long id,
                                          @RequestParam BigDecimal lat,
                                          @RequestParam BigDecimal lng) {
        // 注意：此接口仅供管理员使用，需要添加权限控制
        boolean success = deliveryService.updateDeliveryLocation(id, lat, lng);
        return success ? Result.success() : Result.error("更新配送位置失败");
    }

    /**
     * 管理员确认送达
     */
    @PostMapping("/admin/{id}/confirm")
    public Result<Void> confirmDelivery(@PathVariable Long id) {
        // 注意：此接口仅供管理员使用，需要添加权限控制
        boolean success = deliveryService.confirmDelivery(id);
        return success ? Result.success() : Result.error("确认送达失败");
    }

    /**
     * 用户查询配送信息
     */
    @GetMapping("/order/{orderId}")
    public Result<Delivery> getDeliveryByOrderId(@PathVariable Long orderId) {
        // 注意：此处应先验证用户是否有权限查看该订单的配送信息
        Delivery delivery = deliveryService.getByOrderId(orderId);
        return delivery != null ? Result.success(delivery) : Result.error("配送信息不存在");
    }
} 