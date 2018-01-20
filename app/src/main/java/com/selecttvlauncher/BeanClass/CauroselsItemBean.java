package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/18/2016.
 */
public class CauroselsItemBean {
    private String carousel_image,type,name;
    private int id;

    public String getCarousel_image() {
        return carousel_image;
    }

    public void setCarousel_image(String carousel_image) {
        this.carousel_image = carousel_image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CauroselsItemBean(String carousel_image, String type, int id,String name) {
        this.carousel_image = carousel_image;
        this.type = type;

        this.name = name;
        this.id = id;
    }
}
