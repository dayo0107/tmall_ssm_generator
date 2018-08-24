package com.how2java.tmall.interceptor;

import com.how2java.tmall.pojo.Admin;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author DayoWong on 2018/8/22
 * 拦截未登录的管理员进入任何一个后台页面
 */
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String uri = httpServletRequest.getRequestURI();
        uri = StringUtils.remove(uri,session.getServletContext().getContextPath());
        if (uri.startsWith("/admin")){
            Admin admin = (Admin) session.getAttribute("admin");
            if(null == admin){
                httpServletResponse.sendRedirect("back");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
