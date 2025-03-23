package linepaytest.LinePayDemo.Dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Model.Product;

@Component
public class ProductDaoImpl implements ProductDao {

    // 取得商品
    @Override
    public List<Product> getProducts() {
        return Arrays.asList(new Product("1", "杯子", "https://i.ibb.co/Ndm001Q4/image.png", 100),
                            new Product("2", "盤子", "https://i.ibb.co/W4YQcMzL/image.png", 200));
    }
    
}
