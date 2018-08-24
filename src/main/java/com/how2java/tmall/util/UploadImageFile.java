package com.how2java.tmall.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author DayoWong
 */
public class UploadImageFile {

    MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
