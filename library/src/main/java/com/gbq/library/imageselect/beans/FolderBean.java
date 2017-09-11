package com.gbq.library.imageselect.beans;

import com.gbq.library.utils.StringUtils;

import java.util.ArrayList;

/**
 * 类说明：图片文件夹
 * Author: Kuzan
 * Date: 2017/5/25 14:58.
 */
public class FolderBean {

    private String name;    // 名称
    private ArrayList<String> images;   // 图片地址

    public FolderBean(String name) {
        this.name = name;
    }

    public FolderBean(String name, ArrayList<String> images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void addImage(String image) {
        if (StringUtils.isNotEmptyString(image)) {
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(image);
        }
    }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                ", images=" + images +
                '}';
    }
}
