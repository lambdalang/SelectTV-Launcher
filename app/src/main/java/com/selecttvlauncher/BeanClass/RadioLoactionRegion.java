package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 5/18/2016.
 */
public class RadioLoactionRegion {
    int id,region_id;
    String name,slug;
public RadioLoactionRegion(int id,int region_id,String name,String slug)
{
    this.id=id;
    this.region_id=region_id;
    this.name=name;
    this.slug=slug;

}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
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
}
