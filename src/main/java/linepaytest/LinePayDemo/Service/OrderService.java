package linepaytest.LinePayDemo.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import linepaytest.LinePayDemo.Dao.CartDao;
import linepaytest.LinePayDemo.Dao.CartItemDao;
import linepaytest.LinePayDemo.Dao.OrderDao;
import linepaytest.LinePayDemo.Dao.OrderItemDao;
import linepaytest.LinePayDemo.Dao.ProductDao;
import linepaytest.LinePayDemo.Enum.OrderStatus;
import linepaytest.LinePayDemo.Enum.PaymentMethod;
import linepaytest.LinePayDemo.Model.Order;
import linepaytest.LinePayDemo.Model.OrderItem;
import linepaytest.LinePayDemo.Model.Product;
import linepaytest.LinePayDemo.Model.CartItem;

@Service
public class OrderService {
    
    private final ProductDao productDao;
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderService(ProductDao productDao, 
                        CartDao cartDao, 
                        CartItemDao cartItemDao, 
                        OrderDao orderDao, 
                        OrderItemDao orderItemDao){
        this.productDao = productDao;
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    // 建立購物單
    @Transactional
    public Integer createOrder(Integer memberId, PaymentMethod paymentMethod){
        // 取得使用者購物車編號 cartId
        Integer cartId = cartDao.getCartIdByMemberId(memberId);
        if (cartId == null){
            throw new IllegalArgumentException("Member's cart is empty");
        }

        // 購物車編號取得購物車商品
        List<CartItem> cartItems = cartItemDao.getCartItemByCartId(cartId);
        if (cartItems == null){
            throw new IllegalArgumentException("Cart's item is empty");
        }

        // 計算訂單總金額 (cartItem -> quantity * price)
        int totalAmount = cartItems.stream()
                                .mapToInt(item -> item.getQuantity() * item.getPrice())
                                .sum();

        // 建立訂單
        Order orderData = new Order();
        orderData.setMemberId(memberId);
        orderData.setTotalAmount(totalAmount);
        orderData.setOrderStatus(OrderStatus.PENDING); //預設為 PENDING
        orderData.setPaymentMethod(paymentMethod);
        orderData.setTransactionId(null);
        orderDao.createOrder(orderData);

        // 回傳建立完成的訂單編號
        return orderData.getOrderId();
    }

    @Transactional
    // 建立購物單明細
    public void createOrderItem(Integer orderId, Integer cartId){
        // 取得訂單編號
        if (orderId == null){
            throw new IllegalArgumentException("Order ID is empty");
        }
        
        // 購物車編號取得購物車商品
        List<CartItem> cartItems = cartItemDao.getCartItemByCartId(cartId);
 
        // 建立購物訂單明細
        for (CartItem cartItem : cartItems){
            // 購物車商品編號取得商品資料
            Product products = productDao.getProductItemByCartItem(cartItem.getProductId());

            OrderItem orderItemData = new OrderItem();
            orderItemData.setOrderId(orderId);
            orderItemData.setProductId(cartItem.getProductId());
            orderItemData.setProductName(products.getProductName());
            orderItemData.setImageUrl(products.getImageUrl());
            orderItemData.setQuantity(cartItem.getQuantity());
            orderItemData.setPrice(products.getPrice());
            orderItemDao.insertOrderItem(orderItemData);
        }
    }
}
