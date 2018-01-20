package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 5/18/2016.
 */
public class RadioLocationBean {
    private String has_regions = "";
    private String slug = "";
    private String has_cities = "";
    private String id = "";
    private String name = "";

    public RadioLocationBean(String has_regions,
                             String slug,
                             String has_cities,
                             String id,
                             String name) {

        this.has_regions=has_regions;
        this.slug=slug;
        this.has_cities=has_cities;
        this.id=id;
        this.name=name;

    }

    public String getHas_regions() {
        return has_regions;
    }

    public void setHas_regions(String has_regions) {
        this.has_regions = has_regions;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getHas_cities() {
        return has_cities;
    }

    public void setHas_cities(String has_cities) {
        this.has_cities = has_cities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
