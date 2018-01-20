package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 4/19/2016.
 */
public class SeasonsBean {
    private int id,season_id,season_number;

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    private String description,air_date,poster_url,name;

    public SeasonsBean(int id, String description,String air_date,String poster_url,String name,int season_id,int season_number)
    {
        this.id=id;
        this.description=description;
        this.air_date=air_date;
        this.poster_url=poster_url;
        this.name=name;
        this.season_id=season_id;
        this.season_number=season_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
