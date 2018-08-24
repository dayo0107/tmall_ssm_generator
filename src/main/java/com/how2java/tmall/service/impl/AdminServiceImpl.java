package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.AdminMapper;
import com.how2java.tmall.pojo.Admin;
import com.how2java.tmall.pojo.AdminExample;
import com.how2java.tmall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DayoWong on 2018/8/22
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;
    @Override
    public void add(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public void delete(int id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public Admin get(int id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public Admin get(String name, String password) {
        AdminExample example = new AdminExample();
        example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
        List<Admin> admins = adminMapper.selectByExample(example);
        if(!admins.isEmpty()){
            return  admins.get(0);
        }
        return null;
    }


}
