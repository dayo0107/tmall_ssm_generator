package com.how2java.tmall.service;

import com.how2java.tmall.pojo.ProductImage;

import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
public interface ProductImageService {
    String type_single = "type_single";
    String type_detail = "type_detail";
    void add(ProductImage productImage);
    void delete(int id);
    void update(ProductImage productImage);
    ProductImage get(int id);
    List<ProductImage> list(int pid,String type);
}
