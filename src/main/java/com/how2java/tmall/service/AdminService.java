package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Admin;

/**
 * @author DayoWong on 2018/8/22
 */
public interface AdminService {
    void add(Admin admin);
    void delete(int id);
    void update(Admin  admin);
    Admin  get(int id);
    Admin get(String name, String password);

}
