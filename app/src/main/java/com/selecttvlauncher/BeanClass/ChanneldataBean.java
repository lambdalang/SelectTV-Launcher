package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/11/2016.
 */
public class ChanneldataBean {
    private String seq,title,url,time,id;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public ChanneldataBean(){

    }

    public ChanneldataBean(String seq, String title, String url, String time, Long duration, String id, Long unix_time) {
        this.seq = seq;
        this.title = title;
        this.url = url;
        this.time = time;
        this.duration = duration;

        this.id = id;
        this.unix_time = unix_time;
    }

    private Long unix_time,duration;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(Long unix_time) {
        this.unix_time = unix_time;
    }
}
