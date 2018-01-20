package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 8/24/2016.
 */
public class ChannelNavigation {
    private int id, selected_pos;
    private String type, slug = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelected_pos() {
        return selected_pos;
    }

    public void setSelected_pos(int selected_pos) {
        this.selected_pos = selected_pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData_object() {
        return data_object;
    }

    public void setData_object(Object data_object) {
        this.data_object = data_object;
    }

    private Object data_object;

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public ChannelNavigation(int id, int selected_pos, String type, Object data_object) {
        this.id = id;
        this.selected_pos = selected_pos;
        this.type = type;
        this.data_object = data_object;
    }

    public ChannelNavigation(String slug, int selected_pos, String type, Object data_object) {
        this.slug = slug;
        this.selected_pos = selected_pos;
        this.type = type;
        this.data_object = data_object;
    }
}
