package com.ecommerce.Controller;

import com.ecommerce.ApiResponse.ApiResponse;
import com.ecommerce.Dto.PaymentResponseDto;
import com.ecommerce.Service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/{orderNumber}")
    public ResponseEntity<ApiResponse> pay(@PathVariable String orderNumber, @AuthenticationPrincipal String userId){
        log.info("Pay order request. userId={}, orderNumber={}",userId, orderNumber);

        PaymentResponseDto paymentResponseDto = paymentService.pay(orderNumber,userId);

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Payment processed")
                .data(paymentResponseDto)
                .build();
        return ResponseEntity.ok(response);
    }

}
