package linepaytest.LinePayDemo.Service;

import java.io.Serializable;

public class CartItem implements Serializable{
    private Integer productId;
    private Integer quantity;
    private Integer price;

    public CartItem(Integer productId, Integer quantity, Integer price){
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
}
