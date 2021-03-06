package com.selecttvlauncher.channels;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by babin on 7/4/2017.
 */

public class ChannelScheduler implements Serializable {
    private String description, logo, name, type, slug, featured;
    private String parentcategorySlug;

    private Programs programs=new Programs();

    public ArrayList<Streams> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<Streams> streams) {
        this.streams = streams;
    }

    private ArrayList<Streams> streams=new ArrayList<>();

    public Programs getPrograms() {
        return programs;
    }

    public void setPrograms(Programs programs) {
        this.programs = programs;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getParentcategorySlug() {
        return parentcategorySlug;
    }

    public void setParentcategorySlug(String parentcategorySlug) {
        this.parentcategorySlug = parentcategorySlug;
    }


}
