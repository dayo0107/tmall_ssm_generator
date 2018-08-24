package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.mapper.PropertyMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductExample;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;
    @Override
    public void add(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product product) {
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public Product get(int id) {
        Product product = productMapper.selectByPrimaryKey(id);

        Category category = categoryService.get(product.getCid());
        product.setCategory(category); //产品关联类别
        setFirstProductImage(product); //产品设置第一张图片

        return product;
    }

    @Override
    public List<Product> list(int cid) {
        ProductExample example =new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List<Product> products = productMapper.selectByExample(example);//查询分类下所有product

        Category category = categoryService.get(cid);
        for (Product p:products
             ) {
           p.setCategory(category);   //产品关联类别
            setFirstProductImage(p); //产品设置第一张图片
        }

        return products;
    }

    @Override
    public void setFirstProductImage(Product p) {
        List<ProductImage> productImages = productImageService.list(p.getId(),ProductImageService.type_single);
        if(!productImages.isEmpty()){
            ProductImage productImage = productImages.get(0);
            p.setFirstProductImage(productImage);
        }
    }

    public void setFirstProductImage(List<Product> products){
        for (Product p:products
                ) {
            setFirstProductImage(p);
        }
    }

    @Override
    public void fill(Category category) {
        List<Product> products = list(category.getId());
        category.setProducts(products);
    }

    @Override
    public void fill(List<Category> categories) {
        for (Category c : categories) {
            fill(c);
        }
    }

    @Override
    public void fillByRow(List<Category> Categories) {
        int productNumberEachRow = 8;
        for (Category c: Categories
             ) {
            List<Product> products = list(c.getId());
            c.setProducts(products);
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size >products.size() ? products.size():size;
                List<Product> productsByEach = products.subList(i,size);
                productsByRow.add(productsByEach);
            }
            c.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product p) {
        int saleCount = orderItemService.getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(p.getId());
        p.setReviewCount(reviewCount);

    }

    @Override
    public void setSaleAndReviewNumber(List<Product> ps) {
        for (Product p : ps) {
            setSaleAndReviewNumber(p);
        }
    }

    @Override
    public List<Product> search(String keyword) {
        ProductExample example = new ProductExample();
        example.createCriteria().andNameLike("%"+keyword+"%");
        example.setOrderByClause("id desc");
        List<Product> products = productMapper.selectByExample(example);//模糊查询

        setFirstProductImage(products);//填入首照片
        setCategory(products);//填入分类
        setSaleAndReviewNumber(products);//评价和销量
        return products;
    }

    public void setCategory(Product product){
        Category category = categoryService.get(product.getCid());
        product.setCategory(category);
    }

    public void setCategory(List<Product> products){
        for (Product p : products
                ) {
            setCategory(p);
        }
    }

}
