package com.pucese.bookingmicroservice.controller;

import com.pucese.bookingmicroservice.client.StockClient;
import com.pucese.bookingmicroservice.dto.OrderDTO;
import com.pucese.bookingmicroservice.entity.Order;
import com.pucese.bookingmicroservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockClient stockClient;

    @PostMapping("/order")
    @CircuitBreaker(name = "stockService", fallbackMethod = "fallbackToStockService")
    public String SaveOrder(@RequestBody OrderDTO orderDTO){
        boolean inStock = orderDTO.getOrderItems().stream()
                .allMatch(orderItem -> stockClient.stockAvailable(orderItem.getCode()));
        if(inStock){
            Order order = new Order();
            order.setOrderNo(UUID.randomUUID().toString());
            order.setOrderItems(orderDTO.getOrderItems());
            orderRepository.save(order);
            return "Order Saved";
        }
        return "Order Cannot Be Saved";
    }

    private String fallbackToStockService(){
        return "Something went wrong, please try after some time";
    }
}
