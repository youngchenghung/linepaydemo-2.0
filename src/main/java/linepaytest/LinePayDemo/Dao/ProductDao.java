package linepaytest.LinePayDemo.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.Product;

@Mapper
public interface ProductDao {
    // 根據分類獲取商品 (分頁 & 排序)
    @Select("""
            SELECT * FROM product WHERE categoryId = #{categoryId} 
            ORDER BY 
                CASE WHEN ${sortBy} = 'price' THEN price END ${sortOrder} 
            LIMIT #{size} 
            OFFSET #{offset}
            """)
    List<Product> getProductsByCategory(@Param("categoryId") Integer categoryId,
                                            @Param("sortBy") String sortBy,
                                            @Param("sortOrder") String sortOrder,
                                            @Param("size") int size,
                                            @Param("offset") int offset);
    
    // 查詢商品
    @Select("""
            SELECT COUNT(*) FROM product WHERE categoryId = #{categoryId}
            """)
    Integer countProductsByCategory(@Param("categoryId") Integer categoryId);
    
    // 收尋商品名稱                                        
    @Select("""
            SELECT * FROM product WHERE productName LIKE CONCAT('%', #{productName}, '%') 
            ORDER BY 
                CASE WHEN ${sortBy} = 'productName' THEN productName END ${sortOrder} 
            LIMIT #{size} 
            OFFSET #{offset}
            """)
    List<Product> searchProductsByName(@Param("productName") String productName,
                                    @Param("sortBy") String sortBy,
                                    @Param("sortOrder") String sortOrder,
                                    @Param("size") int size,
                                    @Param("offset") int offset);
    
    // 根據價格範圍查詢商品
    @Select("""
            SELECT * FROM product WHERE price BETWEEN #{minPrice} AND #{maxPrice}
            ORDER By ${sortBy} ${sortOrder}
            LIMIT #{size}
            OFFSET #{offset}
            """)
    List<Product> searchProductsByPriceRange(@Param("minPrice") Integer minPrice,
                                                @Param("maxPrice") Integer maxPrice,
                                                @Param("sortBy") String sortBy,
                                                @Param("sortOrder") String sortOrder,
                                                @Param("size") int size,
                                                @Param("offset") int offset);

    // 查詢單一商品
    @Select("""
            SELECT * from product WHERE productId = #{productId}
            """)
    Product searchProductById(@Param("productId") Integer productId);
}
