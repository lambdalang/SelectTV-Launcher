package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 5/18/2016.
 */
public class ListenGridBean {
    int id;
    String name,image;

    public ListenGridBean(int id,String name,String image)
    {
        this.id=id;
        this.name=name;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
