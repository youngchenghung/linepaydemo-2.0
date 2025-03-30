package linepaytest.LinePayDemo.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import linepaytest.LinePayDemo.Model.CartItem;

@Mapper
public interface CartItemDao {
    
    // 插入購物車商品
    @Insert("""
            INSERT INTO cartItem (cartId, productId, quantity, price)
            VALUES (#{cartId}, #{productId}, #{quantity}, #{price})
            """)
    void insertToCartItem(CartItem cartItem);

}
