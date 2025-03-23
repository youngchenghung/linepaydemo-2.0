package linepaytest.LinePayDemo.Controller;

import org.springframework.web.bind.annotation.RestController;

import linepaytest.LinePayDemo.Model.CartItem;
import linepaytest.LinePayDemo.Model.Product;
import linepaytest.LinePayDemo.Service.CartService;
import linepaytest.LinePayDemo.Service.ProductService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class ShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    // 取得商品列表
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }
    
    // 加入購物車
    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart (@RequestBody List<CartItem> items) {
        items.forEach(cartService::addItemToCart);
        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cart is empty"));
        }
        return ResponseEntity.ok(Map.of("message", "Add to cart successfully"));
    }
    
    // 取得購物車商品列表
    @GetMapping("/cart/items")
    public List<CartItem> getCartItems() {
        return cartService.getCartItems();
    }
    
    // 清空購物車
    @PostMapping("/cart/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart is cleared");
    }
    
}
