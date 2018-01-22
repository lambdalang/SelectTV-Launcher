package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 7/13/2016.
 */
public class CategoryBean {
    private String slug,name;
    private int id;

    public CategoryBean(String slug, String name, int id) {
        this.slug = slug;
        this.name = name;
        this.id = id;
    }
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
}
