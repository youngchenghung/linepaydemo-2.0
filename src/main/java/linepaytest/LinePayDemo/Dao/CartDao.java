package linepaytest.LinePayDemo.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import linepaytest.LinePayDemo.Model.Cart;

@Mapper
public interface CartDao {
    
    // 新增購物車
    @Insert("INSERT INTO cart (memberId) VALUES (#{memberId})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    void insertCart(Cart cart);
}
