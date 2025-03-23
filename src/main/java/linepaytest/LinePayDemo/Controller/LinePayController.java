package linepaytest.LinePayDemo.Controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import linepaytest.LinePayDemo.Model.CartItem;
import linepaytest.LinePayDemo.Model.LinePayResponse;
import linepaytest.LinePayDemo.Service.CartService;
import linepaytest.LinePayDemo.Service.LinePayService;


@RestController
public class LinePayController {

    @Autowired
    private LinePayService linePayService;

    @Autowired
    private CartService cartService;

    // LinePay API #1付款請求
    @PostMapping("/request")
    public ResponseEntity<LinePayResponse> requestPayment(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("LinePay API #1付款請求");

        // 取得 JWT token
        String jwtToken = authorizationHeader.replace("Bearer ", "");

        // 取得購物車商品，建立購物付款初始化
        List<CartItem> cartItems = cartService.getCartItems(); // 取得購物車商品
        LinePayResponse linePayResponse = linePayService.initiatePayment(cartItems, jwtToken); // 呼叫 initiatePayment API
        return ResponseEntity.ok(linePayResponse);
    } 

    // LinePay API #2付款確認 (callback transactionId 參數)
    // LinePay API #3查詢付款狀態
    @GetMapping("/redirect")
    public ResponseEntity<?> handleRedirect(@RequestParam String transactionId, @RequestParam String token) {
        System.out.println("LinePay API #2付款確認");
        System.out.println("Transaction ID: " + transactionId); // 取得 transaction ID

        if (transactionId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid transaction ID"));
        }

        // 呼叫 confirmPayment API 確認付款
        List<CartItem> cartItems = cartService.getCartItems(); // 取得購物車商品
        String ConfirmResult = linePayService.confirmPayment(transactionId, cartItems);
        System.out.println("Confirm result: " + ConfirmResult);

        // 判斷 LinePay server 回傳碼是否為 0000
        if (ConfirmResult.contains("\"returnCode\":\"0000\"")) {
            System.out.println("LinePay API #3查詢付款狀態");
            String StatusResult = linePayService.getPaymentStatus(transactionId);
            System.out.println("Status result: " + StatusResult);
            
            return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", "/order_success?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8))
            .body("Payment status success.\nResult: " + StatusResult);
        }

        else {
            return ResponseEntity.badRequest().body(Map.of("error", "Payment status failed.\nResult: " + ConfirmResult));
        }
    }
}
