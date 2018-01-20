package com.selecttvlauncher.BeanClass;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/18/2016.
 */
public class CauroselBean {
    private String title,name;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ArrayList<CauroselsItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(ArrayList<CauroselsItemBean> item_list) {
        this.item_list = item_list;
    }

    public CauroselBean(String title, String name, int id, ArrayList<CauroselsItemBean> item_list) {
        this.title = title;
        this.name = name;
        this.id = id;

        this.item_list = item_list;
    }

    private ArrayList<CauroselsItemBean> item_list;
}
