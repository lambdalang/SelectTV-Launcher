package com.selecttvlauncher.BeanClass;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 7/22/2016.
 */
public class HorizontalListAppManager {

    private int id,network_id;
    private String name,source,network_image,network_package,network_link,network_name;
    private ArrayList<HorizontalListitemBean> data_list;

    public HorizontalListAppManager(int id, int network_id, String name, String source, String network_image, String network_package,String network_link,String network_name,ArrayList<HorizontalListitemBean> data_list) {
        this.id = id;
        this.network_id = network_id;
        this.name = name;
        this.source = source;
        this.network_image = network_image;
        this.data_list = data_list;
        this.network_package = network_package;
        this.network_link = network_link;
        this.network_name = network_name;
    }

    public int getId() {
        return id;
    }

    public String getNetwork_package() {
        return network_package;
    }

    public void setNetwork_package(String network_package) {
        this.network_package = network_package;
    }

    public String getNetwork_link() {
        return network_link;
    }

    public void setNetwork_link(String network_link) {
        this.network_link = network_link;
    }

    public String getNetwork_name() {
        return network_name;
    }

    public void setNetwork_name(String network_name) {
        this.network_name = network_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNetwork_id() {
        return network_id;
    }

    public void setNetwork_id(int network_id) {
        this.network_id = network_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNetwork_image() {
        return network_image;
    }

    public void setNetwork_image(String network_image) {
        this.network_image = network_image;
    }

    public ArrayList<HorizontalListitemBean> getData_list() {
        return data_list;
    }

    public void setData_list(ArrayList<HorizontalListitemBean> data_list) {
        this.data_list = data_list;
    }
}
