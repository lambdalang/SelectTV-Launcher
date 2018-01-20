package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 6/1/2016.
 */
public class ChannelListBean {

    public ChannelListBean(String big_logo_url, String description, String logo_url, String name, int id) {
        this.big_logo_url = big_logo_url;
        this.description = description;
        this.logo_url = logo_url;
        this.name = name;
        this.id = id;
    }
    public ChannelListBean(){

    }

    private String big_logo_url,description,logo_url,name;
    private int id;

    public String getBig_logo_url() {
        return big_logo_url;
    }

    public void setBig_logo_url(String big_logo_url) {
        this.big_logo_url = big_logo_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
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
