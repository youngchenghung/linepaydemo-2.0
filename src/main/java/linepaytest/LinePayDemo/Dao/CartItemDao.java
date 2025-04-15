package linepaytest.LinePayDemo.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.CartItem;

@Mapper
public interface CartItemDao {
    
    // 插入購物車商品
    @Insert("""
            INSERT INTO cartItem (cartId, productId, quantity, price)
            VALUES (#{cartId}, #{productId}, #{quantity}, #{price})
            """)
    void insertToCartItem(CartItem cartItem);

    // 購物車編號查詢購物車商品
    @Select("""
            SELECT * FROM cartItem WHERE cartId = #{cartId}
            """)
    List<CartItem> getCartItemByCartId(Integer cartId);
}
