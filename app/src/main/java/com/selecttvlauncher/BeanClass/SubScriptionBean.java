package com.selecttvlauncher.BeanClass;

import java.util.ArrayList;

/**
 * Created by ${Madhan} on 5/12/2016.
 */
public class SubScriptionBean {


    int id;
    String name;

    ArrayList<SubScriptionsubList> subScriptionSubLists;

    public SubScriptionBean(int id,String name,ArrayList<SubScriptionsubList> subScriptionSubLists)
    {
        this.id=id;
        this.name=name;
        this.subScriptionSubLists=subScriptionSubLists;
    }

    public ArrayList<SubScriptionsubList> getSubScriptionSubLists() {
        return subScriptionSubLists;
    }

    public void setSubScriptionSubLists(ArrayList<SubScriptionsubList> subScriptionSubLists) {
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
