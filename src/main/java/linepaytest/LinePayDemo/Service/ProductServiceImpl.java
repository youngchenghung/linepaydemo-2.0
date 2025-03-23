package linepaytest.LinePayDemo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Dao.ProductDao;
import linepaytest.LinePayDemo.Model.Product;

@Component
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductDao productDao;

    // 取得商品
    @Override
    public List<Product> getProducts() {
        return productDao.getProducts();
    }
    
}
