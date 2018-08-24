package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;

import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
public interface OrderItemService {
    void add(OrderItem orderItem);
    void delete(int id);
    void update(OrderItem orderItem);
    OrderItem get(int id);
    List  list();

    void fill(List<Order> orders);
    void fill(Order oder); //关联订单项,订单项再关联产品.计算订单总价和数目

    int getSaleCount(int  pid);

    List<OrderItem> listByUser(User user);
}
