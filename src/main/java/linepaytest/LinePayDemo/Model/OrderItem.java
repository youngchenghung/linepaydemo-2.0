package linepaytest.LinePayDemo.Model;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer orderItemId;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private String imageUrl;
    private Integer quantity;
    private Integer price;
}
