package linepaytest.LinePayDemo.Model;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer categoryId;
    private String categoryName;
    private String description;
    private List<Product> product;
}
