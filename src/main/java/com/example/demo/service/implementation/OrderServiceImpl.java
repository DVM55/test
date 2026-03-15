package com.example.demo.service.implementation;

import com.example.demo.dto.req.OrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.exception.ConflictException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.RedisService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final RedisService redisService;
    private final OrderRepository orderRepository;

    private static final int LIMIT_PER_USER = 2;

    @Transactional
    public  void createOrder(OrderRequest request) {
        String stockKey = "flashsale:stock:" + request.getProductId();
        String userBoughtKey = "flashsale:user:" + request.getProductId() + ":" + request.getUserId();

        Long result = redisService.executeFlashSaleOrder(
                stockKey,
                userBoughtKey,
                request.getQuantity(),
                LIMIT_PER_USER
        );

        if (result == -1) {
            throw new EntityNotFoundException("Không tìm thấy tồn kho sản phẩm");
        }

        if (result == 0) {
            throw new ConflictException("Sản phẩm đã hết hàng");
        }

        if (result == 2) {
            throw new IllegalArgumentException("Bạn đã vượt quá giới hạn mua cho phép");
        }

        try {
            Order order = Order.builder()
                    .productId(request.getProductId())
                    .userId(request.getUserId())
                    .quantity(request.getQuantity())
                    .build();

            orderRepository.save(order);
        } catch (Exception e) {
            redisService.rollbackFlashSaleOrder(
                    stockKey,
                    userBoughtKey,
                    request.getQuantity()
            );
            throw new RuntimeException("Lưu đơn hàng thất bại!");
        }
    }
}
