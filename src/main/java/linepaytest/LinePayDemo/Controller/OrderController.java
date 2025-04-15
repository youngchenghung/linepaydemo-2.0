package linepaytest.LinePayDemo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import linepaytest.LinePayDemo.Enum.PaymentMethod;
import linepaytest.LinePayDemo.Service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // 建立購物單
    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestParam Integer memberId, @RequestParam PaymentMethod paymentMethod){
        logger.info("Create Order by memberId: {}, paymentMehod: {}", memberId, paymentMethod);

        try{
            Integer orderId = orderService.createOrder(memberId, paymentMethod);
            return ResponseEntity.ok(String.format("Order ID : %d , has been created successfully", orderId));
        }
        catch (Exception e){
            logger.error("Failed to create order by memberId: {}", memberId, e);
            return ResponseEntity.badRequest().body("Failed to create order");
        }
    }

    // 建立訂單明細
    @PostMapping("/createOrderItem")
    public ResponseEntity<String> createOrderItem(@RequestParam Integer orderId, @RequestParam Integer cartId){
        logger.info("Order Item ID : {}", orderId);

        try{
            orderService.createOrderItem(orderId, cartId);
            return ResponseEntity.ok("Order Item has been created successfully");
        }
        catch (Exception e){
            logger.error("Failed to create order Item by order ID : {}", orderId, e);
            return ResponseEntity.badRequest().body("Failed to create order Item");
        }
    }
}
