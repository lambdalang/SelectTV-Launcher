package com.selecttvlauncher.BeanClass;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/12/2016.
 */
public class ChannelScheduler {
    private int id;

    public ArrayList<VideoBean> getVideolist() {
        return videolist;
    }

    public void setVideolist(ArrayList<VideoBean> videolist) {
        this.videolist = videolist;
    }

    private ArrayList<VideoBean> videolist;

public ChannelScheduler(){

}


    public ChannelScheduler(int id, String big_logo_url, String description, String logo_url, String name,ArrayList<VideoBean> Videolist) {
        this.id = id;
        this.big_logo_url = big_logo_url;
        this.description = description;
        this.logo_url = logo_url;
        this.name = name;
        this.videolist=Videolist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    private String big_logo_url,description,logo_url,name;
}
