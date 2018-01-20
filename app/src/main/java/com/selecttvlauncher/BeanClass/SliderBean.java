package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/15/2016.
 */
public class SliderBean {

    private int id;
    private String description,title,image,name,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SliderBean(int id, String description, String title, String image, String name, String type) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.image = image;
        this.name = name;
        this.type = type;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
