package com.selecttvlauncher.BeanClass;

import java.util.ArrayList;

/**
 * Created by babin on 1/11/2017.
 */

public class UserAllSubscriptionBean {
    private String code;
    private String image_url;
    private String name;
    private String slug;

    public String getGray_image_url() {
        return gray_image_url;
    }

    public void setGray_image_url(String gray_image_url) {
        this.gray_image_url = gray_image_url;
    }

    private String gray_image_url;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<HorizontalListitemBean> getData_list() {
        return data_list;
    }

    public void setData_list(ArrayList<HorizontalListitemBean> data_list) {
        this.data_list = data_list;
    }

    private ArrayList<HorizontalListitemBean> data_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public UserAllSubscriptionBean(int id,String code, String image_url, String name, String slug,String gray_image_url,boolean isSelected, ArrayList<HorizontalListitemBean> data_list) {
        this.id=id;
        this.code = code;
        this.image_url = image_url;
        this.name = name;
        this.slug = slug;
        this.isSelected=isSelected;
        this.gray_image_url=gray_image_url;
        this.data_list=data_list;
    }
}
