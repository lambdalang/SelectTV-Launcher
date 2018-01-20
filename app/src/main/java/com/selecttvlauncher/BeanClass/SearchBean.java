package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 5/3/2016.
 */
public class SearchBean {

    String name,type;
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SearchBean(String name,String type,int count)
    {
        this.name=name;
        this.type=type;
        this.count=count;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
