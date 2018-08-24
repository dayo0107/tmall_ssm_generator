package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;

import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
public interface ProductService {

    void add(Product product);
    void delete(int id);
    void update(Product product);
    Product get (int id);
    List<Product> list(int cid);
    void setFirstProductImage(Product p);

    void fill(Category category);

    void fill(List<Category> categories);
    //为多个分类填充产品集合

    void fillByRow(List<Category> Categories);
    //.为多个分类填充推荐产品集合，即把分类下的产品集合，按照8个为一行，拆成多行，以利于后续页面上进行显示

    void setSaleAndReviewNumber(Product p);//为产品设置销量和评价数量的方法：
    void setSaleAndReviewNumber(List<Product> ps);

    List<Product> search(String keyword);
}

