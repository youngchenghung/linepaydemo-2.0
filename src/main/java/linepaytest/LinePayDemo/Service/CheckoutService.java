package linepaytest.LinePayDemo.Service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import linepaytest.LinePayDemo.Dao.CartDao;
import linepaytest.LinePayDemo.Dao.CartItemDao;
import linepaytest.LinePayDemo.Model.Cart;
import linepaytest.LinePayDemo.Model.CartItem;

@Service
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final GuestCartService guestCartService;
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;

    public CheckoutService(GuestCartService guestCartService,
                            CartDao cartDao,
                            CartItemDao cartItemDao){
        this.guestCartService = guestCartService;
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;
    }
    
    // 結帳訪客購物車
    @Transactional
    public void checkout(String sessionId, int memberId){
        Map<String, CartItem> cartItems = guestCartService.getCart(sessionId);
        
        // 檢查購物車有商品
        if (cartItems.isEmpty()){
            throw new RuntimeException("Cart is empty, unable to checkout");
        }

        // 轉移資料(Redis -> MySQL) 建立購物車
        logger.info("String checkout for Redis cart: {} to MySQL memberId: {}", sessionId, memberId);
        Cart cart = new Cart();
        cart.setMemberId(memberId);
        cartDao.insertCart(cart);

        // 插入購物車商品
        for (CartItem item : cartItems.values()){
            item.setCartId(cart.getCartId());
            cartItemDao.insertToCartItem(item);
        }

        // 完成資料轉移到 MySQL， 刪除 Redis 該筆 sessionId 資料
        guestCartService.clearCart(sessionId);
        logger.info("Successfully transferred data to MySSQL cart memberId: {}", memberId);
    }
}
