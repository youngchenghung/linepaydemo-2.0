package linepaytest.LinePayDemo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import linepaytest.LinePayDemo.Model.CartItem;

import java.util.stream.Collectors;
import java.util.Map;

@Service
public class GuestCartService {
    
    private static final Logger logger = LoggerFactory.getLogger(GuestCartService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, CartItem> hashOperations;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GuestCartService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    // 取得訪客購物車 sessionId 
    private String getCartKey(String sessionId){
        return sessionId;
    }

    // 加入商品到購物車
    public void addProductToCart(String sessionId, CartItem cartItem){
        try {
            String cartSession = "cart:" + getCartKey(sessionId);
            String productKey ="productId:" + cartItem.getProductId();

            // 取得目前購物車內容
            Map<Object, Object> cartData = redisTemplate.opsForHash().entries(cartSession);

            // 檢查 Redis 購物車內已存在該商品，有此商品則更新商品數量跟價格
            CartItem existingItem = (CartItem) hashOperations.get(cartSession, productKey);
            if(existingItem != null){
                existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                existingItem.setPrice(cartItem.getPrice());
                hashOperations.put(cartSession, productKey, existingItem);
                logger.info("Update product in cart: {}, productId: {}, new quantity: {}",
                            sessionId, cartItem.getProductId(), existingItem.getQuantity());
            }
            // Redis 無此商品則加入到購物車列表
            else{
                // 計算 cartItemId 最大值 +1
                int incCartItemId = cartData.values().stream()
                    .map(obj -> objectMapper.convertValue(obj, CartItem.class))
                    .mapToInt(CartItem::getCartItemId)
                    .max()
                    .orElse(0)+1;

                cartItem.setCartItemId(incCartItemId);
                hashOperations.put(cartSession, productKey, cartItem);
                logger.info("Added produnct to cart: {}, productId: {}, new cartItemId: {}",
                            cartSession, cartItem.getProductId(), incCartItemId);
            }
        }
        catch (Exception e){
            logger.error("Failed to add product to cart: {}, cartIdKey: {}", sessionId, cartItem.getCartId() ,e);
            throw new RuntimeException("failed to added product to cart", e);
        }
    }

    // 取得購物車內容
    public Map<String, CartItem> getCart(String sessionId){
        try {
            String cartKey = "cart:" + sessionId;
        
            // 取得 Redis 存的資料
            Map<Object, Object> cartData = redisTemplate.opsForHash().entries(cartKey);
            logger.info("Cart from Redis: {}", cartData);
    
            return cartData.entrySet().stream().collect(Collectors.toMap(
                // 確保 key 轉換為 String
                entry -> String.valueOf(entry.getKey()),
                // 確保 value 轉換為 CartItem 物件
                entry -> objectMapper.convertValue(entry.getValue(), CartItem.class))
        );
        } catch (Exception e){
            logger.error("Failed to get cart: {}", sessionId, e);
            throw new RuntimeException("Failed to get cart", e);
        }
    }

    // 移除購物車商品(單一)
    public void deleteFromCart(String sessionId, Integer productId){
        try {
            String cartSession = "cart:" + getCartKey(sessionId);
            String productKey = "productId:" + productId;

            // Redis 刪除資料
            hashOperations.delete(cartSession, productKey);
            logger.info("Product deleted from cart: {}, productId {}", sessionId, productId);
        }
        catch (Exception e){
            logger.error("Failed to delete cart: {}, productId: {}", sessionId, productId, e);
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    // 清空購物車內容
    public void clearCart(String sessionId){
        try{
            String cartSession = "cart:" + getCartKey(sessionId);

            // Redis 回傳刪除布林值
            boolean result = redisTemplate.delete(cartSession);
            logger.info("Cart clear operation result: {}", result);
        }
        catch (Exception e){
            logger.error("Failed to clear cart: {}", sessionId, e);
            throw new RuntimeException("Failed to clear cart", e);
        }
    }
}
