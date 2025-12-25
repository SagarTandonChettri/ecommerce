package com.ecommerce.Service;

import com.ecommerce.Dto.PaymentResponseDto;
import com.ecommerce.Entity.Cart;
import com.ecommerce.Entity.Order;
import com.ecommerce.Exception.OrderNotFoundException;
import com.ecommerce.Exception.PaymentException;
import com.ecommerce.OrderStatus;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    public PaymentResponseDto pay(String orderNumber, String userId){

        log.info("Payment request received for orderNumber={}",orderNumber);

        Order order = orderRepository.findByOrderNumberAndUserId(orderNumber,userId)
                .orElseThrow(() -> {
                    log.warn("Order Not Found for orderNumber={}",orderNumber);
                    return new OrderNotFoundException(
                            "Order not found: "+ orderNumber
                    );
                });

        if(order.getStatus() != OrderStatus.CREATED){
            log.warn("Invalid payment attempt. orderNumber={}, status={}",orderNumber,order.getStatus());
            throw new PaymentException("Payment alredy processed ot order is invalid");
        }

        validateOrderStatusForPayment(order);

        // Simulate payment frontend not present so
        boolean paymentSuccess = true;
        String transactionId = "TXN-"+System.currentTimeMillis();

        if (paymentSuccess){
            order.setStatus(OrderStatus.PAID);
            try {
                cartRepository.deleteByUserId(userId);
                log.info("Cart cleared after successful payment. userId={}", userId);
            } catch (Exception e) {
                log.warn("Could not clear cart for userId={}, but payment succeeded. Error: {}",
                        userId, e.getMessage());
                // Continue - order is paid even if cart clearing fails
            }
            log.info("Payment SUCCESS for orderNumber={}",orderNumber);
        }else {
            order.setStatus(OrderStatus.FAILED);
            log.warn("Payment FAILED for orderNumber={}",orderNumber);
        }

        orderRepository.save(order);

        return PaymentResponseDto.builder()
                .orderNumber(orderNumber)
                .status(order.getStatus().name())
                .transactionId(transactionId)
                .message(paymentSuccess ? "Payment successful":"Payment failed")
                .build();
    }

    private void validateOrderStatusForPayment(Order order) {
        switch (order.getStatus()) {
            case CREATED -> { /* allowed */ }
            case PAID -> throw new PaymentException("Order already paid");
            case FAILED -> throw new PaymentException("Payment already failed");
            case CANCELLED -> throw new PaymentException("Order cancelled");
            default -> throw new PaymentException("Invalid order state");
        }
    }
}
