package linepaytest.LinePayDemo.Dao;

import java.util.List;

import linepaytest.LinePayDemo.Model.CartItem;

public interface CartDao {
    
    void addItem(CartItem items);

    // 取得購物車內容
    List<CartItem> getCartItems();

    // 清除購物車內容
    void clearCart();
}
