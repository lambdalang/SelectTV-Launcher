package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/31/2016.
 */
public class GenreBean {
    private int id;

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

    private String name;

    public GenreBean(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
