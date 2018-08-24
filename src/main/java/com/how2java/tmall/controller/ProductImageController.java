package com.how2java.tmall.controller;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.ProductImageService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.UploadImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author DayoWong on 2018/8/19
 */
@Controller
@RequestMapping("")
public class ProductImageController {

    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;


    @RequestMapping("/admin_productImage_add")
    public String add(ProductImage productImage, UploadImageFile uploadImageFile, HttpSession session){
        productImageService.add(productImage);

        String fileName = productImage.getId()+".jpg";
        String fileFolder;
        String fileFolder_small = null;
        String fileFolder_middle = null;

        if(ProductImageService.type_single.equals(productImage.getType())){
            fileFolder = session.getServletContext().getRealPath("img/productSingle");
            fileFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
            fileFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
        }
        else {
            fileFolder = session.getServletContext().getRealPath("img/productDetail");
        }
        File file = new File(fileFolder,fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {

            uploadImageFile.getImage().transferTo(file);
            BufferedImage bufferedImage = ImageUtil.change2jpg(file);
            ImageIO.write(bufferedImage,"jpg",file);

            if (ProductImageService.type_single.equals(productImage.getType())){
                File f_small = new File(fileFolder_small,fileName);
                File f_middle = new File(fileFolder_middle,fileName);

                ImageUtil.resizeImage(file,56,65,f_small);
                ImageUtil.resizeImage(file,217,190,f_middle);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin_productImage_list?pid="+productImage.getPid();
    }

    @RequestMapping("/admin_productImage_delete")
    public String delete(int id , HttpSession session){
        ProductImage productImage =productImageService.get(id);

        String fileName = id+".jpg";
        String fileFolder;
        String fileFolder_small = null;
        String fileFolder_middle = null;

        if (ProductImageService.type_single.equals(productImage.getType())) {
            fileFolder = session.getServletContext().getRealPath("img/productSingle");
            fileFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
            fileFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");

            File file = new File(fileFolder,fileName);
            File f_small = new File(fileFolder_small,fileName);
            File f_middle = new File(fileFolder_middle,fileName);

            file.delete();
            f_small.delete();
            f_middle.delete();
        }
        else {
            fileFolder =session.getServletContext().getRealPath("img/productDetail");
            File file = new File(fileFolder,fileName);
            file.delete();
        }
        productImageService.delete(id);
        return "redirect:/admin_productImage_list?pid="+productImage.getPid();
    }

    @RequestMapping("/admin_productImage_list")
    public String list(int pid, Model model){

        Product product = productService.get(pid);
        List<ProductImage> pisSingle = productImageService.list(pid,"type_single");
        List<ProductImage> pisDetail = productImageService.list(pid,"type_detail");

        model.addAttribute("pisSingle",pisSingle);
        model.addAttribute("pisDetail",pisDetail);
        model.addAttribute("p",product);

        return "admin/listProductImage";
    }
}
