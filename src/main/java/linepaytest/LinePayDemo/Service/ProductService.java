package linepaytest.LinePayDemo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import linepaytest.LinePayDemo.Dao.ProductDao;
import linepaytest.LinePayDemo.Model.Product;



@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao){
        this.productDao = productDao;
    }

    // 根據分類獲取商品 (分頁 & 排序)
    public List<Product> getProductsByCategory(Integer categoryId, String sortBy, String sortOrder, int page, int size){
        int offset = page * size;
        return productDao.getProductsByCategory(categoryId, sortBy, sortOrder, size, offset);
    }

    // 查詢商品
    public Integer countProductsByCategory(Integer categoryId){
        return productDao.countProductsByCategory(categoryId);
    }

    // 根據名稱搜尋商品
    public List<Product> searchProductsByName(String productName, String sortBy, String sortOrder, int page, int size){
        int offset = page * size;
        return productDao.searchProductsByName(productName, sortBy, sortOrder, size, offset);
    }

    // 根據價格範圍查詢商品
    public List<Product> searchProductsByPriceRange(Integer minPrice, Integer maxPrice, String sortBy, String sortOrder, int page, int size){
        int offset = page * size;
        return productDao.searchProductsByPriceRange(minPrice, maxPrice, sortBy, sortOrder, size, offset);
    }

    // 查詢單一商品
    public Product searchProductById(Integer productId){
        return productDao.searchProductById(productId);
    }
}
