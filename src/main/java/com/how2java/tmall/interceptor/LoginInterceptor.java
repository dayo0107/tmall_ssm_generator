package com.how2java.tmall.interceptor;

import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.OrderItemService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * @author DayoWong on 2018/8/22
 * preHandle在业务处理器处理请求之前被调用
 * 如果返回false
 *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
 * 如果返回true
 *    执行下一个拦截器,直到所有的拦截器都执行完毕
 *    再执行被拦截的Controller
 *    然后进入拦截器链,
 *    从最后一个拦截器往回执行所有的postHandle()
 *    接着再从最后一个拦截器往回执行所有的afterCompletion()
 */
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String[] noNeedAuthPage = new String[]{
                "home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"};//不需要登陆也能访问的路径
        HttpSession session = httpServletRequest.getSession();
        String uri = httpServletRequest.getRequestURI();
        uri = StringUtils.remove(uri,session.getServletContext().getContextPath());//去掉/tmall_ssm
//        System.out.println("uri:"+uri);
//        System.out.println("httpServletRequest.getContextPath():"+httpServletRequest.getContextPath());
//        System.out.println("session.getServletContext():"+session.getServletContext());
//        System.out.println("session.getServletContext().getContextPath():"+session.getServletContext().getContextPath());
//        System.out.println("realPath:"+httpServletRequest.getServletContext().getRealPath("/"));

        if(uri.startsWith("/fore")){
            String method = StringUtils.substringAfterLast(uri,"/fore" );
            if(!Arrays.asList(noNeedAuthPage).contains(method)){
                User user = (User) session.getAttribute("user");
                if(null == user){
                    httpServletResponse.sendRedirect("loginPage");
                    return  false;
                }
            }
        }

        return true;
    }
    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
