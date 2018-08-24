package com.how2java.tmall.mapper;

import com.how2java.tmall.pojo.Admin;
import com.how2java.tmall.pojo.AdminExample;

import java.util.List;

/**
 * @author DayoWong on 2018/8/22
 */
public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}
