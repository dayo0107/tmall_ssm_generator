package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page;
import com.how2java.tmall.util.UploadImageFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author DayoWong
 */

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/admin_category_list")
    public ModelAndView list(Page page){
        ModelAndView mav = new ModelAndView("admin/listCategory");

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Category> categories = categoryService.list();
        int total = (int) new PageInfo<>(categories).getTotal();
        page.setTotal(total);
        mav.addObject("cs",categories);
        mav.addObject("page",page);

        return  mav;
    }
    /**
     * CategoryController新增add方法
     * 1. add方法映射路径admin_category_add的访问
     * 1.1 参数 Category c接受页面提交的分类名称
     * 1.2 参数 session 用于在后续获取当前应用的路径
     * 1.3 UploadedImageFile 用于接受上传的图片
     * 2. 通过categoryService保存c对象
     * 3. 通过session获取ControllerContext,再通过getRealPath定位存放分类图片的路径。
     * 如果严格按照本教程的做法，使用idea中的tomcat部署的话，那么图片就会存放在:E:\project\tmall_ssm\target\tmall_ssm\img\category 这里
     * 4. 根据分类id创建文件名
     * 5. 如果/img/category目录不存在，则创建该目录，否则后续保存浏览器传过来图片，会提示无法保存
     * 6. 通过UploadedImageFile 把浏览器传递过来的图片保存在上述指定的位置
     * 7. 通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
     * 8. 客户端跳转到admin_category_list
     */
    @RequestMapping("/admin_category_add")
    public ModelAndView add(Category c, HttpSession session, UploadImageFile uploadImageFile) throws IOException {
        ModelAndView mav = new ModelAndView("redirect:/admin_category_list");

        categoryService.add(c);
        File imageFoledr = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFoledr,c.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        uploadImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
        return mav;
    }

    @RequestMapping("/admin_category_delete")
    public ModelAndView delete(Category c ,HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/admin_category_list");
        categoryService.delete(c.getId());

        File fileFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(fileFolder,c.getId()+".jpg");
        file.delete();

        return mav;
    }

    @RequestMapping("/admin_category_edit")
    public ModelAndView edit(int id){
        ModelAndView mav = new ModelAndView("admin/editCategory");

        Category c = categoryService.get(id);
        mav.addObject("c",c);

        return  mav;

    }
    @RequestMapping("/admin_category_update")
    public  ModelAndView update(Category category ,HttpSession session ,UploadImageFile uploadImageFile) throws IOException {
        ModelAndView mav  = new ModelAndView( "redirect:/admin_category_list");

        categoryService.update(category);
        MultipartFile image = uploadImageFile.getImage();
        if(null != image && !image.isEmpty()){
            File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,category.getId()+".jpg");
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }

        return mav;
    }

}
