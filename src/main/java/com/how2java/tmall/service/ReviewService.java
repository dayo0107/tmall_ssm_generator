package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Review;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DayoWong on 2018/8/20
 */
public interface ReviewService {

    void add(Review review);
    void delete(int id);
    void update(Review review);
    Review get(int  id);

    List<Review> list(int pid); //产品页下的评价和评价数量
    int getCount(int pid);

}
