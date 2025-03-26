package linepaytest.LinePayDemo.Controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.*;
import linepaytest.LinePayDemo.Model.Category;
import linepaytest.LinePayDemo.Model.Product;
import linepaytest.LinePayDemo.Service.CategoryService;
import linepaytest.LinePayDemo.Service.ProductService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@Validated
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final CategoryService categoryService;
    private final ProductService productService;
    
    public ProductController(
                            CategoryService categoryService, 
                            ProductService productService
                            ){
        this.categoryService = categoryService;
        this.productService = productService;
    }
    
    // 取得所有商品分類
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        logger.info("Get all product categoies :" + categories);
        return ResponseEntity.ok(categories);
    }

    // 根據分類獲取商品 (分頁 & 排序)
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable Integer categoryId,
                                                @RequestParam(defaultValue = "0") @Min(0) int page,
                                                @RequestParam(defaultValue = "3") @Min(1) int size,
                                                @RequestParam(defaultValue = "productId") String sortBy,
                                                @RequestParam(defaultValue = "ASC") String sortOrder
                                                ){
        logger.info("Search product by categoryId={}", categoryId);
        List<Product> products = productService.getProductsByCategory(categoryId, sortBy, sortOrder, page, size);
        logger.info("Get product={}", products);
        return ResponseEntity.ok(products);
    }

    // 查詢商品數量
    @GetMapping("/productCount/{categoryId}")
    public ResponseEntity <Integer> countProductsByCategory(@PathVariable Integer categoryId){
        logger.info("Product count by categoryId={} ", categoryId);
        Integer produnctCount = productService.countProductsByCategory(categoryId);
        logger.info("Product count={} ", produnctCount);
        return ResponseEntity.ok(produnctCount);
    }

    // 根據名稱搜尋商品
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String productName,
                                                @RequestParam(defaultValue = "0") @Min(0) int page,
                                                @RequestParam(defaultValue = "3") @Min(1) int size,
                                                @RequestParam(defaultValue = "productId") String sortBy,
                                                @RequestParam(defaultValue = "ASC") String sortOrder
                                                ){
        logger.info("Search product name={} ", productName);
        List<Product> products = productService.searchProductsByName(productName, sortBy, sortOrder, page, size);
        logger.info("Get product={} ", products);
        return ResponseEntity.ok(products);
    }

    // 根據價格範圍查詢商品
    @GetMapping("/search/priceRange")
    public ResponseEntity<List<Product>> searchProductsByPriceRange(@RequestParam Integer minPrice,
                                                                    @RequestParam Integer maxPrice,
                                                                    @RequestParam(defaultValue = "price") String sortBy,
                                                                    @RequestParam(defaultValue = "ASC") String sortOrder,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "3") int size){
        logger.info("Search product by price minPrice={}, maxPrice={} ", minPrice, maxPrice);
        List<Product> products = productService.searchProductsByPriceRange(minPrice, maxPrice, sortBy, sortOrder, page, size);
        logger.info("Search product={} ", products);
        return ResponseEntity.ok(products);
    }

    // 查詢單一商品
    @GetMapping("/search/{productId}")
    public ResponseEntity <Product> searchProductById(@PathVariable Integer productId){
        logger.info("Search product id={} ", productId);
        Product product = productService.searchProductById(productId);
        logger.info("Search product={} ", product);
        return ResponseEntity.ok(product);
    }

    // 全域異常處理 - 捕捉錯誤
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        logger.error("Error message: " + e.getMessage(), e);
        return ResponseEntity.internalServerError().body("Error message: " + e.getMessage());
    }
}
