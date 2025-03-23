package linepaytest.LinePayDemo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Dao.CartDao;
import linepaytest.LinePayDemo.Model.CartItem;

@Component
public class CartServiceImpl implements CartService {

    @Autowired 
    private CartDao cartDao;

    // 增加商品到購物車
    @Override
    public void addItemToCart(CartItem items) {
        cartDao.addItem(items);
    }
    
    // 取得購物車內容
    @Override
    public void clearCart() {
        cartDao.clearCart();
    }

    // 清除購物車內容
    @Override
    public List<CartItem> getCartItems() {
        return cartDao.getCartItems();
    }
}
