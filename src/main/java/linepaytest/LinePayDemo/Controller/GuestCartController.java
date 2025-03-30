package linepaytest.LinePayDemo.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import linepaytest.LinePayDemo.Model.CartItem;
import linepaytest.LinePayDemo.Service.CheckoutService;
import linepaytest.LinePayDemo.Service.GuestCartService;

@RestController
@RequestMapping("/api/cart")
public class GuestCartController {

    private static final Logger logger = LoggerFactory.getLogger(GuestCartController.class);
    private final GuestCartService guestCartService;
    private final CheckoutService checkoutService;

    public GuestCartController(GuestCartService guestCartService, CheckoutService checkoutService){
        this.guestCartService = guestCartService;
        this.checkoutService = checkoutService;
    }

    // 加入商品到購物車
    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestParam String sessionId, @RequestBody CartItem cartItem){
        logger.info("Adding product for cart: {}, product: {}, quantity: {}", sessionId, cartItem.getProductId(), cartItem.getProductId());
        guestCartService.addProductToCart(sessionId, cartItem);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // 取得購物車內容
    @GetMapping("/{sessionId}")
    public ResponseEntity<Map<String, CartItem>> getCart(@PathVariable String sessionId){
        logger.info("Get Cart for sessionId: {}", sessionId);
        sessionId = sessionId.split("=")[1];  
        return ResponseEntity.ok(guestCartService.getCart(sessionId));
    }

    // 移除購物車商品(單一)
    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteFromCart(@RequestParam String sessionId, @RequestParam Integer productId){
        logger.info("Removing product cart: sessionId: {}, productId: {}", sessionId, productId);
        guestCartService.deleteFromCart(sessionId, productId);
        return ResponseEntity.ok("Product has been deleted from cart");
    }

    // 清空購物車內容
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam String sessionId){
        logger.info("Clearing cart for sessionId: {}", sessionId);
        guestCartService.clearCart(sessionId);
        return ResponseEntity.ok("Cart has been cleared");
    }

    // 結帳訪客購物車
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam String sessionId, @RequestParam Integer memberId){
        logger.info("Checkout cart: {}, memberId: {}", sessionId, memberId);
        checkoutService.checkout(sessionId, memberId);
        return ResponseEntity.ok("Checkout process successfully");
    }
}