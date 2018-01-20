package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/20/2016.
 */
public class NetworkDetailBean {

    private String slogan,name,headquarters,start_time,image,description,slug;
    private int id;

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NetworkDetailBean(String slogan, String name, String headquarters, String start_time, String image, String description, String slug, int id) {
        this.slogan = slogan;
        this.name = name;
        this.headquarters = headquarters;
        this.start_time = start_time;
        this.image = image;
        this.description = description;
        this.slug = slug;
        this.id = id;
    }
}
