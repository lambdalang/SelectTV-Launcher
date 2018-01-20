package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 5/12/2016.
 */
public class SubScriptionListBean {

int id;

    String name,image,type;



    public SubScriptionListBean(int id,String name,String image,String type)
    {
        this.id=id;
        this.name=name;
        this.image=image;

        this.type=type;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
