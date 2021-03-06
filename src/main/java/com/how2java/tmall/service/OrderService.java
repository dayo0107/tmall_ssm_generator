package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;

import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
public interface OrderService {

        String waitPay = "waitPay";
        String waitDelivery = "waitDelivery";
        String waitConfirm = "waitConfirm";
        String waitReview = "waitReview";
        String finish = "finish";
        String delete = "delete";

        void add(Order c);
        void delete(int id);
        void update(Order c);
        Order get(int id);
        List<Order> list();

        float add(Order order , List<OrderItem> orderItems);//新增订单 同时给订单项设置oid。

        List<Order> list(int uid, String excludedStatus);

}
