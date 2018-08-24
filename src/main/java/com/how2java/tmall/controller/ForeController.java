package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.how2java.tmall.comparator.*;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author DayoWong on 2018/8/20
 */
@Controller
@RequestMapping("")
public class ForeController {

    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;
    @RequestMapping("/forehome")
    public String productByRow(Model model){
        List<Category> categories = categoryService.list();
        productService.fill(categories);
        productService.fillByRow(categories);

        model.addAttribute("cs",categories);

        return "fore/home";
    }

    @RequestMapping("/foreregister")
    public String register(User user, Model model){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);//转义字符
        user.setName(name);
        if(userService.isExist(name)) {
            String m = "用户名已经被使用,不能使用";
            model.addAttribute("msg", m);
            model.addAttribute("user", null);
            return "fore/register";
        }
        userService.add(user);
        return"redirect:/registerSuccessPage";
    }
   @RequestMapping("/forelogin")
    public String login(String name , String password, Model model, HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name ,password);
        if(null == user){
            model.addAttribute("msg","用户货密码错误");
            return "fore/login";
        }
        session.setAttribute("user",user);
        return "redirect:/forehome";
   }

   @RequestMapping("/forelogout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/forehome";
   }

   @RequestMapping("/foreproduct") //单个产品页面
    public String product(int pid,Model model){
        Product p = productService.get(pid);

       List<ProductImage> productSingleImages = productImageService.list(p.getId(), ProductImageService.type_single);
       List<ProductImage> productDetailImages = productImageService.list(p.getId(), ProductImageService.type_detail);
       p.setProductSingleImages(productSingleImages);
       p.setProductDetailImages(productDetailImages);

       List<PropertyValue> pvs = propertyValueService.list(p.getId());
       List<Review> reviews = reviewService.list(p.getId());
       productService.setSaleAndReviewNumber(p);

       model.addAttribute("reviews", reviews);
       model.addAttribute("p", p);
       model.addAttribute("pvs", pvs);
       return "fore/product";
   }

   @RequestMapping("/forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(null != user)
            return "success";
        return  "fail";
   }
   @RequestMapping("/foreloginAjax")
    @ResponseBody
    public String loginAjax(String name ,String password ,HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);
        if(null != user){
            session.setAttribute("user",user);
            return "success";
        }
        return "fail";
   }

   @RequestMapping("/forecategory") //某分类下所有产品页
    public String category(int cid ,String sort , Model model){
       Category category = categoryService.get(cid);
       productService.fill(category);
       productService.setSaleAndReviewNumber(category.getProducts());
       if(null != sort){
           switch (sort){
               case "review":
                   category.getProducts().sort(new ProductReviewComparator());
                   break;
               case "date":
                   category.getProducts().sort(new ProductDateComparator());
                   break;
               case "saleCount":
                   category.getProducts().sort(new ProductSaleCountComparator());
                   break;
               case "price":
                   category.getProducts().sort(new ProductPriceComparator());
                   break;
               case "all":
                   category.getProducts().sort(new ProductAllComparator());
                   break;
               default:
                   break;
           }
       }
       else
           category.getProducts().sort(new ProductAllComparator());

       model.addAttribute("c",category);
       return "fore/categoryPage";
   }

   @RequestMapping("/foresearch")
    public String search(String keyword ,Model model){
        PageHelper.offsetPage(0,20);
        List<Product> products = productService.search(keyword);//搜索
        List<Category> categories = categoryService.list();

        model.addAttribute("cs",categories);//给search.jsp的搜索栏下列表链接传值
        model.addAttribute("ps",products);
        return "fore/searchResult";
   }

   @RequestMapping("/forebuyone")
    public String buyOne(int pid, int num ,  HttpSession session){

        User user = (User) session.getAttribute("user");
        int oiid = 0;
        boolean found = false;

       List<OrderItem> orderItems = orderItemService.listByUser(user);//查询未生成订单的的订单项,即购物车状态的订单项
       for (OrderItem oi: orderItems
            ) {
           if(oi.getPid() == pid ){
               oi.setNumber(oi.getNumber()+num);
               orderItemService.update(oi);
               found = true;
               oiid = oi.getId();
               break;
           }
       }

        if( !found){
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItem.setUid(user.getId());
            orderItemService.add(orderItem);//mybatis自动给对象的id赋值
            oiid = orderItem.getId();
        }

        return "redirect:/forebuy?oiid="+oiid;
   }
   @RequestMapping("/forebuy")
    public String buy(String[] oiid , Model model,HttpSession session){
       List<OrderItem> orderItems = new ArrayList<>();
        int total = 0;
       for (String strid :oiid
               ) {
           int id = Integer.parseInt(strid); //兼容购物车选中结算操作,多个订单项
           OrderItem orderItem = orderItemService.get(id);
           total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
           orderItems.add(orderItem);
       }

        model.addAttribute("total",total);
        session.setAttribute("ois",orderItems);

        return "fore/buy";
   }
    @RequestMapping("/foreaddCart")
    @ResponseBody
    public String addCart(int pid , int num ,HttpSession session){

        User user = (User) session.getAttribute("user");
        boolean found = false;

        List<OrderItem> orderItems = orderItemService.listByUser(user);//查询未生成订单的的订单项,即购物车状态的订单项
        for (OrderItem oi: orderItems) {
            if(oi.getPid() == pid ){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                break;
            }
        }

        if( !found){
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItem.setUid(user.getId());
            orderItemService.add(orderItem);//mybatis自动给对象的id赋值
        }

        return "success";
    }

    @RequestMapping("/forecart")
    public String cart(Model model , HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user);//无订单的订单项集合 已关联产品

        model.addAttribute("ois",orderItems);
        return "fore/cart";
    }

    @RequestMapping("/forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(int pid ,int number ,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(null == user){
            return "fail";
        }
        List<OrderItem> orderItems = orderItemService.listByUser(user);
        for (OrderItem oi:orderItems
             ) {
            if(oi.getPid()==pid){
                oi.setNumber(number);
                orderItemService.update(oi);
                break;
            }
        }
        return "success";
    }

    @RequestMapping("/foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem( HttpSession session,int oiid){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return "fail";
        orderItemService.delete(oiid);
        return "success";
    }
    @RequestMapping("/forecreateOrder")
    public String createOrder(Order order , HttpSession session){
        User user = (User) session.getAttribute("user");
        order.setUid(user.getId());
        order.setOrderCode(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+RandomUtils.nextInt(10000));
        order.setCreateDate(new Date());
        order.setStatus(OrderService.waitPay);
        List<OrderItem> ois= (List<OrderItem>) session.getAttribute("ois");
        float total = orderService.add(order,ois);
        return "redirect:/forealipay?oid="+order.getId() +"&total="+total;
    }
    @RequestMapping("/forepayed")
    public String payed(int oid, float total ,Model model){

        Order order = orderService.get(oid);
        order.setPayDate(new Date());
        order.setStatus(OrderService.waitDelivery);
        orderService.update(order);
        model.addAttribute("o",order);

        return "fore/payed";
    }
    @RequestMapping("/forebought")
    public String bought(HttpSession session ,Model model){
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.list(user.getId(), OrderService.delete);
        orderItemService.fill(orders);
        model.addAttribute("os",orders);

        return "fore/bought";
    }

    @RequestMapping("/foreconfirmPay")
    public String confirmPay(int oid, Model model){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        model.addAttribute("o",order);
        return "fore/confirmPay";
    }

    @RequestMapping("/foreorderConfirmed")
    public String orderConfirmed(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitReview);
        orderService.update(order);

        return "fore/orderConfirmed";
    }
    @RequestMapping("/foredeleteOrder")
    @ResponseBody
    public String deleteOrder(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.delete);
        orderService.update(order);
        return "success";
    }
    @RequestMapping("forereview")
    public String review(int oid ,Model model){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        Product product = order.getOrderItems().get(0).getProduct();
        productService.setSaleAndReviewNumber(product);
        List<Review> reviews = reviewService.list(product.getId());
        model.addAttribute("p",product);
        model.addAttribute("o",order);
        model.addAttribute("reviews",reviews);
        return "fore/review";
    }
    @RequestMapping("/foredoreview")
    public  String doReview(HttpSession session,int oid , int pid ,String content){
        User user = (User) session.getAttribute("user");

        content = HtmlUtils.htmlEscape(content);//转义 防止恶意输入

        Review review = new Review();
        review.setContent(content);
        review.setUid(user.getId());
        review.setPid(pid);
        review.setCreateDate(new Date());
        reviewService.add(review);

        Order order = orderService.get(oid);
        order.setStatus(OrderService.finish);
        orderService.update(order);


        return "redirect:/forereview?oid="+oid+"&showonly=true";
    }
}
