package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.OrderItemMapper;
import com.how2java.tmall.mapper.OrderMapper;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductService productService;
    @Override
    public void add(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(id);
        setProduct(orderItem);//关联产品
        return orderItem;
    }

    @Override
    public List list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(example);
    }

    @Override
    public void fill(Order oder) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria()
                .andOidEqualTo(oder.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(example);//得到订单内的所有项目

        int total = 0;
        int total_number = 0;
        for (OrderItem oi: orderItemList
             ) {
            Product p = productService.get(oi.getPid());
            oi.setProduct(p);//关联产品

            total_number += oi.getNumber();        //计算总数目
            total+=(oi.getNumber() * oi.getProduct().getPromotePrice()); //计算总价
        }

        oder.setTotal(total);
        oder.setTotalNumber(total_number);
        oder.setOrderItems(orderItemList);
    }

    @Override
    public void fill(List<Order> orders) {
        for (Order o:orders
             ) {
            fill(o);
        }
    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid).andOidIsNotNull();
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);

        int saleCount = 0;
        for (OrderItem oi: orderItems
             ) {
            saleCount += oi.getNumber();
        }
        return saleCount;
    }

    @Override
    public List<OrderItem> listByUser(User user) {
        OrderItemExample example =new OrderItemExample();
        example.createCriteria().andUidEqualTo(user.getId()).andOidIsNull();
        List<OrderItem> result =orderItemMapper.selectByExample(example);
        setProduct(result);//关联产品
        return result;
    }

    public void setProduct(List<OrderItem> ois){
        for (OrderItem oi: ois) {
            setProduct(oi);
        }
    }

    private void setProduct(OrderItem oi) {
        Product p = productService.get(oi.getPid());
        oi.setProduct(p);
    }

}
