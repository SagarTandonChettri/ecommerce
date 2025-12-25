package com.ecommerce.Controller;

import com.ecommerce.ApiResponse.ApiResponse;
import com.ecommerce.ApiResponse.OrderResponse;
import com.ecommerce.Entity.Order;
import com.ecommerce.Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@AuthenticationPrincipal String userId){
        log.info("Order request received for userId={}", userId);

        OrderResponse orderResponse = orderService.createOrder(userId);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(201)
                .message("Order created successfully")
                .data(orderResponse)
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse>fetchAllUserOrder(@AuthenticationPrincipal String userId){
        log.info("Fetching All User Order by UserID={}",userId);


        List<OrderResponse> orders = orderService.getOrderHistory(userId);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Order Fetch successfully")
                .data(orders)
                .build();

        return ResponseEntity.status(200).body(response);

    }

}
