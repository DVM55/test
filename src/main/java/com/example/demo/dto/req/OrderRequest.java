package com.example.demo.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull(message = "Product ID không được để trống")
    @Positive(message = "Product ID phải là số dương")
    private Long productId;

    @NotNull(message = "Quantity không được để trống")
    @Min(value = 1, message = "Quantity phải lớn hơn hoặc bằng 1")
    private Integer quantity;

    @NotNull(message = "User ID không được để trống")
    @Positive(message = "User ID phải là số dương")
    private Long userId;
}