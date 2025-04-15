package linepaytest.LinePayDemo.Model;
import java.sql.Timestamp;

import linepaytest.LinePayDemo.Enum.OrderStatus;
import linepaytest.LinePayDemo.Enum.PaymentMethod;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer orderId;
    private Integer memberId;
    private Integer totalAmount;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
