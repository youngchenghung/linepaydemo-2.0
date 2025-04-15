package linepaytest.LinePayDemo.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.Cart;

@Mapper
public interface CartDao {
    
    // 新增購物車
    @Insert("INSERT INTO cart (memberId) VALUES (#{memberId})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    void insertCart(Cart cart);

    // 查詢購物車編號
    @Select("""
            SELECT cartId FROM cart WHERE memberId = (#{memberId}) 
            ORDER BY createdAt DESC 
            LIMIT 1
            """)
    Integer getCartIdByMemberId(@Param("memberId")Integer memberId);
}
