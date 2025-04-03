package org.example.nutri_guide_background.service;

import org.example.nutri_guide_background.entity.Delivery;

import java.math.BigDecimal;

/**
 * 配送服务接口
 */
public interface DeliveryService {

    /**
     * 创建配送订单
     *
     * @param orderId 订单ID
     * @return 配送单号
     */
    String createDelivery(Long orderId);

    /**
     * 更新配送状态
     *
     * @param deliveryId 配送ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateDeliveryStatus(Long deliveryId, Integer status);

    /**
     * 更新配送员位置
     *
     * @param deliveryId 配送ID
     * @param lat 纬度
     * @param lng 经度
     * @return 是否更新成功
     */
    boolean updateDeliveryLocation(Long deliveryId, BigDecimal lat, BigDecimal lng);

    /**
     * 确认送达
     *
     * @param deliveryId 配送ID
     * @return 是否确认成功
     */
    boolean confirmDelivery(Long deliveryId);

    /**
     * 根据订单ID查询配送信息
     *
     * @param orderId 订单ID
     * @return 配送信息
     */
    Delivery getByOrderId(Long orderId);
} 