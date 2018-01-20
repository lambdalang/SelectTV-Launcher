package com.selecttvlauncher.BeanClass;

/**
 * Created by Ocs pl-79(17.2.2016) on 6/4/2016.
 */
public class AppFormatBean {

    private String source;
    private String link;
    private String app_name;
    private String app_download_link;
    private String formats_price;
    private String formats_type;
    private String formats_format;
    private String display_name;
    private String image;

    public String getSubscription_code() {
        return subscription_code;
    }

    public void setSubscription_code(String subscription_code) {
        this.subscription_code = subscription_code;
    }

    private String subscription_code;
    private boolean app_required,app_link;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_download_link() {
        return app_download_link;
    }

    public void setApp_download_link(String app_download_link) {
        this.app_download_link = app_download_link;
    }

    public String getFormats_price() {
        return formats_price;
    }

    public void setFormats_price(String formats_price) {
        this.formats_price = formats_price;
    }

    public String getFormats_type() {
        return formats_type;
    }

    public void setFormats_type(String formats_type) {
        this.formats_type = formats_type;
    }

    public String getFormats_format() {
        return formats_format;
    }

    public void setFormats_format(String formats_format) {
        this.formats_format = formats_format;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public boolean isApp_required() {
        return app_required;
    }

    public void setApp_required(boolean app_required) {
        this.app_required = app_required;
    }

    public boolean isApp_link() {
        return app_link;
    }

    public void setApp_link(boolean app_link) {
        this.app_link = app_link;
    }

    public AppFormatBean(String source, String link, String app_name, String app_download_link, String formats_price, String formats_type, String formats_format, String display_name, boolean app_required, boolean app_link,String image,String subscription_code) {
        this.source = source;
        this.link = link;
        this.app_name = app_name;
        this.app_download_link = app_download_link;
        this.formats_price = formats_price;

        this.formats_type = formats_type;
        this.formats_format = formats_format;
        this.display_name = display_name;
        this.app_required = app_required;
        this.app_link = app_link;
        this.image = image;
        this.subscription_code = subscription_code;
    }
}
