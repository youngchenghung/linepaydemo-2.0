package linepaytest.LinePayDemo.Service;

import java.util.List;

import linepaytest.LinePayDemo.Model.CartItem;

public interface CartService {
    
    // 增加商品到購物車
    void addItemToCart(CartItem items);

    // 取得購物車內容
    List<CartItem> getCartItems();

    // 清除購物車內容
    void clearCart();
}
