package com.example.demo.crons;

import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StockSyncScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;

    @Scheduled(cron = "*/30 * * * * *")
    public void syncStockToDatabase() {
        Set<String> keys = redisTemplate.keys("flashsale:stock:*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            try {
                Long productId = Long.parseLong(key.replace("flashsale:stock:", ""));
                Object stockValue = redisTemplate.opsForValue().get(key);

                if (stockValue == null) {
                    continue;
                }

                Integer stock = Integer.parseInt(stockValue.toString());
                productRepository.updateStock(productId, stock);

            } catch (Exception e) {
                System.out.println("Sync stock failed for key: " + key + ", error: " + e.getMessage());
            }
        }
    }
}