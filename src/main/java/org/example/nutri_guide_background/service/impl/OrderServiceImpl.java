package org.example.nutri_guide_background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nutri_guide_background.dto.OrderDTO;
import org.example.nutri_guide_background.dto.OrderItemDTO;
import org.example.nutri_guide_background.dto.OrderQueryDTO;
import org.example.nutri_guide_background.entity.Address;
import org.example.nutri_guide_background.entity.Order;
import org.example.nutri_guide_background.entity.OrderItem;
import org.example.nutri_guide_background.entity.Product;
import org.example.nutri_guide_background.mapper.OrderMapper;
import org.example.nutri_guide_background.service.AddressService;
import org.example.nutri_guide_background.service.OrderItemService;
import org.example.nutri_guide_background.service.OrderService;
import org.example.nutri_guide_background.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final AddressService addressService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final ProductServiceImpl productServiceImpl; // 用于获取商品主图

    public OrderServiceImpl(AddressService addressService, 
                           ProductService productService, 
                           OrderItemService orderItemService,
                           ProductServiceImpl productServiceImpl) {
        this.addressService = addressService;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.productServiceImpl = productServiceImpl;
    }

    @Override
    @Transactional
    public Long createOrder(OrderDTO orderDTO, Long userId) {
//        // 1. 校验地址
//        Address address = addressService.getAddressDetail(orderDTO.getAddressId(), userId);
//        if (address == null) {
//            throw new IllegalArgumentException("收货地址不存在");
//        }

        // 2. 校验商品
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productService.getById(itemDTO.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("商品不存在: " + itemDTO.getProductId());
            }

            // 计算商品总价
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // 构建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(productServiceImpl.getMainImage(product.getId())); // 使用定义的方法获取主图
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setTotalPrice(itemTotal);
            orderItems.add(orderItem);
            
            // 更新商品库存和销量
            productService.decreaseStock(product.getId(), itemDTO.getQuantity());
        }

        // 3. 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(generateOrderNo());
        order.setTotalAmount(totalAmount);
        order.setActualAmount(totalAmount); // 实际金额暂时等于总金额，后续可扩展优惠券等
        order.setStatus(0); // 待支付
//        order.setAddressId(address.getId());
        order.setRemark(orderDTO.getRemark());
        
        // 4. 保存订单
        save(order);
        
        // 5. 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemService.saveBatch(orderItems);
        
        return order.getId();
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long orderId, Long userId) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId));
        
        if (order == null) {
            return false;
        }
        
        // 只有待支付状态的订单可以取消
        if (order.getStatus() != 0) {
            throw new IllegalStateException("当前订单状态不可取消");
        }
        
        order.setStatus(4); // 已取消
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean payOrder(Long orderId, Long userId) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId));
        
        if (order == null) {
            return false;
        }
        
        // 只有待支付状态的订单可以支付
        if (order.getStatus() != 0) {
            throw new IllegalStateException("当前订单状态不可支付");
        }
        
        order.setStatus(1); // 已支付待配送
        order.setPaymentTime(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    public Page<Order> pageUserOrders(OrderQueryDTO queryDTO, Long userId) {
        Page<Order> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(queryDTO.getStatus() != null, Order::getStatus, queryDTO.getStatus())
                .eq(StringUtils.hasText(queryDTO.getOrderNo()), Order::getOrderNo, queryDTO.getOrderNo())
                .orderByDesc(Order::getCreateTime);
        
        return page(page, queryWrapper);
    }

    @Override
    public Order getOrderDetail(Long orderId, Long userId) {
        return getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId));
    }

    @Override
    @Transactional
    public boolean confirmReceive(Long orderId, Long userId) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId));
        
        if (order == null) {
            return false;
        }
        
        // 只有配送中状态的订单可以确认收货
        if (order.getStatus() != 2) {
            throw new IllegalStateException("当前订单状态不可确认收货");
        }
        
        order.setStatus(3); // 已完成
        return updateById(order);
    }
    
    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }
} 