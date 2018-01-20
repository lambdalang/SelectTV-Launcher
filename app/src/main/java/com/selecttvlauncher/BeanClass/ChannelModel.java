package com.selecttvlauncher.BeanClass;

import org.json.JSONArray;

/**
 * Created by Administrator on 2/15/2016.
 */
public class ChannelModel
{
    public String channelName;
    public int channelId;
    public String channelLogo;
    public JSONArray channelVideos;

    public ChannelModel() {
    }

    public ChannelModel(String channelName, int channelId, String channelLogo, JSONArray channelVideos) {
        this.channelName = channelName;
        this.channelId = channelId;
        this.channelLogo = channelLogo;
        this.channelVideos = channelVideos;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelLogo() {
        return channelLogo;
    }

    public void setChannelLogo(String channelLogo) {
        this.channelLogo = channelLogo;
    }

    public JSONArray getChannelVideos() {
        return channelVideos;
    }

    public void setChannelVideos(JSONArray channelVideos) {
        this.channelVideos = channelVideos;
    }
}
