package com.example.demo.controller;


import com.example.demo.dto.req.OrderRequest;
import com.example.demo.dto.res.ApiResponse;

import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createOrder(
            @Valid @RequestBody OrderRequest request) {

        orderService.createOrder(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(HttpServletResponse.SC_OK)
                        .message("Đặt hàng flash sale thành công")
                        .data(null)
                        .build()
        );
    }
}