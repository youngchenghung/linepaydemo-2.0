package linepaytest.LinePayDemo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import linepaytest.LinePayDemo.Dao.CategoryDao;
import linepaytest.LinePayDemo.Model.Category;

@Service
public class CategoryService {
    
    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao){
        this.categoryDao = categoryDao;
    }

    // 取得所有商品分類
    public List<Category> getAllCategories(){
        return categoryDao.getAllCategories();
    } 
}
