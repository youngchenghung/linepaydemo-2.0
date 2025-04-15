package linepaytest.LinePayDemo.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import linepaytest.LinePayDemo.Model.Product;

@Mapper
public interface ProductDao {
    // 根據分類獲取商品 (分頁 & 排序)
    List<Product> getProductsByCategory(@Param("categoryId") Integer categoryId,
                                            @Param("sortBy") String sortBy,
                                            @Param("sortOrder") String sortOrder,
                                            @Param("size") int size,
                                            @Param("offset") int offset);
    
    // 查詢商品
    Integer countProductsByCategory(@Param("categoryId") Integer categoryId);
    
    // 收尋商品名稱                                        
    List<Product> searchProductsByName(@Param("productName") String productName,
                                    @Param("sortBy") String sortBy,
                                    @Param("sortOrder") String sortOrder,
                                    @Param("size") int size,
                                    @Param("offset") int offset);
    
    // 根據價格範圍查詢商品
    List<Product> searchProductsByPriceRange(@Param("minPrice") Integer minPrice,
                                                @Param("maxPrice") Integer maxPrice,
                                                @Param("sortOrder") String sortOrder,
                                                @Param("size") int size,
                                                @Param("offset") int offset);

    // 查詢單一商品
    Product searchProductById(@Param("productId") Integer productId);

    // 購物車商片編號查詢商品資料
    Product getProductItemByCartItem(@Param("productId")Integer productId);
}
