package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 12/8/2016.
 */
public class UserSubscriptionBean {

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

    public UserSubscriptionBean(String code, String image_url, String name, String slug,String gray_image_url,boolean isSelected) {
        this.code = code;
        this.image_url = image_url;
        this.name = name;
        this.slug = slug;
        this.isSelected=isSelected;
        this.gray_image_url=gray_image_url;
    }
}
