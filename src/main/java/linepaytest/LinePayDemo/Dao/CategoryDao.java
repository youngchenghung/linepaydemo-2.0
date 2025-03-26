package linepaytest.LinePayDemo.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import linepaytest.LinePayDemo.Model.Category;

@Mapper
public interface CategoryDao {
    // 取得所有商品分類
    @Select("""
            SELECT * FROM category
            """)
    List<Category> getAllCategories();
}
