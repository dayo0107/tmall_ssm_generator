package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.OrderService;
import com.how2java.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
@Controller
@RequestMapping("")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("/admin_order_list")
    public String list(Model model,Page page){

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Order> orders =  orderService.list();

        int total = (int) new PageInfo<>(orders).getTotal();
        page.setTotal(total);

        orderItemService.fill(orders); //给订单填充对应项目

        model.addAttribute("os",orders);
        model.addAttribute("page",page);

        return "admin/listOrder";
    }

    @RequestMapping("admin_order_delivery")
    public String delivery(Order order){

        Date date = new Date();
        order.setDeliveryDate(date);
        order.setStatus(OrderService.waitConfirm);
        orderService.update(order);

        return "redirect:/admin_order_list";
    }
}
