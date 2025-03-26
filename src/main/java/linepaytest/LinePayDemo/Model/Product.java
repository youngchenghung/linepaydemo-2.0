package linepaytest.LinePayDemo.Model;
import java.sql.Timestamp;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer productId;
    private String productName;
    private String imageUrl;
    private Integer price;
    private String description;
    private Integer stockQuantity;
    private Integer categoryId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
