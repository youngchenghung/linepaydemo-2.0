package linepaytest.LinePayDemo.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.Order;

public interface OrderDao {

    // 建立購物訂單
    @Insert("""
            INSERT INTO `order` (memberId, totalAmount, orderStatus, paymentMethod, transactionId)
            VALUES (#{memberId}, #{totalAmount}, #{orderStatus}, #{paymentMethod}, #{transactionId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    void createOrder(Order order);
}
