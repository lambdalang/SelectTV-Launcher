package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/12/2016.
 */
public class VideoBean {

    private int seq,id;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(Long unix_time) {
        this.unix_time = unix_time;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public VideoBean(){

    }

    public VideoBean(int seq, int id, String title, String url, String time, Long unix_time, Long duration) {
        this.seq = seq;
        this.id = id;
        this.title = title;
        this.url = url;
        this.time = time;

        this.unix_time = unix_time;
        this.duration = duration;
    }

    private String title,url,time;
    private Long unix_time,duration;
}
