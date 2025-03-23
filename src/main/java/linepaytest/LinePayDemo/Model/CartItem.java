package linepaytest.LinePayDemo.Model;

public class CartItem {
    private String id;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private Integer price;
    
    public String getId() {
        return id;
    }
    public void setProductId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setProductName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
}
