package linepaytest.LinePayDemo.Model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L; // 建議加入 serialVersionUID
    private Integer cartItemId;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private Integer price;
}