package linepaytest.LinePayDemo.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Model.CartItem;

@Component
public class CartDaoImpl implements CartDao {

    private final List<CartItem> cart = new ArrayList<>();

    @Override
    public void addItem(CartItem items){
        cart.add(items);
    }

    // 取得購物車內容
    @Override
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cart);
    }

    // 清除購物車內容
    @Override
    public void clearCart() {
        cart.clear();
    }
    
}
