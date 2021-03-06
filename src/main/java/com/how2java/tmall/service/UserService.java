package com.how2java.tmall.service;

import com.how2java.tmall.pojo.User;

import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
public interface UserService {
    void add(User user);
    void delete(int id);
    void update(User user);
    User get(int id);
    List<User> list();
    /*前台功能*/
    boolean isExist(String name);
    User get(String name, String password);


}
