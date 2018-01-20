package com.selecttvlauncher.Adapter;

/**
 * Created by Ocs pl-79(17.2.2016) on 8/1/2016.
 */
public class Video {
    private String title;
    private String mediaPlayerUrl;

    public Video(String title, String mediaPlayerUrl){
        this.setTitle(title);
        this.setMediaPlayerUrl(mediaPlayerUrl);
    }

    public Video() {
        // TODO Auto-generated constructor stub
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMediaPlayerUrl(String mediaPlayerUrl) {
        this.mediaPlayerUrl = mediaPlayerUrl;
    }

    public String getMediaPlayerUrl() {
        return mediaPlayerUrl;
    }

    public String getVideoId() {
        // TODO Auto-generated method stub
        return QueryUtility.getQueryParams(getMediaPlayerUrlQuery()).get("v");
    }

    private String getMediaPlayerUrlQuery(){
        return mediaPlayerUrl.split("\\?")[1];
    }


}
