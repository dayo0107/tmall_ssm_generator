package com.how2java.tmall.controller;

import com.how2java.tmall.pojo.Admin;
import com.how2java.tmall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

/**
 * @author DayoWong on 2018/8/22
 */
@Controller
@RequestMapping("")
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping("/back")
    public String back(){
        return "admin/login";
    }
    @RequestMapping("/backLogin")
    public String backLogin(String name, String password, HttpSession session , Model model){
        name = HtmlUtils.htmlEscape(name);
        Admin admin = adminService.get(name, password);
        if(null == admin){
            model.addAttribute("msg","用户名或密码错误");
            return "admin/login";
        }
        session.setAttribute("admin",admin);
        return "redirect:/admin_category_list";
    }
    @RequestMapping("/backLogout")
    public String backLogout(HttpSession session){
        session.setAttribute("admin",null);
        return "redirect:/back";
    }
}
