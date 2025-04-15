package linepaytest.LinePayDemo.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.OrderItem;

public interface OrderItemDao {
    
    // 建立購物訂單明細
    @Insert("""
            INSERT INTO orderItem (orderId, productId, productName, imageUrl, quantity, price)
            VALUES (#{orderId}, #{productId}, #{productName}, #{imageUrl}, #{quantity}, #{price})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "orderItemId")
    void insertOrderItem(OrderItem orderItem);

    // 查詢購物清單的商品名稱
    @Select("""
            SELECT productName FROM product WHERE productId = #{productId}
            """)
    String getProductNameByProductId(Integer productId);

    // 查詢購物清單的商品圖片
    @Select("""
            SELECT imageUrl FROM product WHERE productId = #{productId}
            """)
    String getImageUrlByProductId(Integer productId);
}
