package com.selecttvlauncher.BeanClass;

import java.util.ArrayList;

/**
 * Created by ${Madhan} on 5/12/2016.
 */
public class SubScriptionsubList {

    int id;
    String name;

    ArrayList<SubScriptionListBean> subScriptionSubLists;

    public SubScriptionsubList(int id, String name, ArrayList<SubScriptionListBean> subScriptionSubLists)
    {
        this.id=id;
        this.name=name;
        this.subScriptionSubLists=subScriptionSubLists;
    }

    public ArrayList<SubScriptionListBean> getSubScriptionSubLists() {
        return subScriptionSubLists;
    }

    public void setSubScriptionSubLists(ArrayList<SubScriptionListBean> subScriptionSubLists) {
        this.subScriptionSubLists = subScriptionSubLists;
    }



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

}
