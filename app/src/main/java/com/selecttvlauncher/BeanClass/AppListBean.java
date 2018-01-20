package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 6/10/2016.
 */
public class AppListBean {
    private String name,app_package,image,link,slug,net_id;
    private int id;

    public String getNet_id() {
        return net_id;
    }

    public void setNet_id(String net_id) {
        this.net_id = net_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp_package() {
        return app_package;
    }

    public void setApp_package(String app_package) {
        this.app_package = app_package;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public AppListBean(String name, String app_package, String image, String link, String slug, int id,String net_id) {
        this.name = name;
        this.app_package = app_package;
        this.image = image;
        this.link = link;
        this.slug = slug;
        this.id = id;
        this.net_id=net_id;

    }
}
