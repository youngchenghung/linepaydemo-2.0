package linepaytest.LinePayDemo.Model;
import java.sql.Timestamp;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer orderId;
    private Integer memberId;
    private Integer totalAmount;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
