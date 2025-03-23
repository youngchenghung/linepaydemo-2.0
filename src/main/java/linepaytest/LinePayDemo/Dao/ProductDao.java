package linepaytest.LinePayDemo.Dao;

import java.util.List;

import linepaytest.LinePayDemo.Model.Product;

public interface ProductDao {
    
    // 取得商品
    List<Product> getProducts();
}
