package com.selecttvlauncher.BeanClass;

/**
 * Created by ${Madhan} on 4/28/2016.
 */
public class ChannelCategoryBean {

    int mChannelcategoryId;
    String mChannelCategoryParentId,mChannelCategoryImage,mChannelCategoryName;


    public ChannelCategoryBean(int mChannelcategoryId,String mChannelCategoryParentId,String mChannelCategoryImage,String mChannelCategoryName)
    {
        this.mChannelcategoryId=mChannelcategoryId;
        this.mChannelCategoryParentId=mChannelCategoryParentId;
        this.mChannelCategoryImage=mChannelCategoryImage;
        this.mChannelCategoryName=mChannelCategoryName;

    }

    public int getmChannelcategoryId() {
        return mChannelcategoryId;
    }

    public void setmChannelcategoryId(int mChannelcategoryId) {
        this.mChannelcategoryId = mChannelcategoryId;
    }

    public String getmChannelCategoryParentId() {
        return mChannelCategoryParentId;
    }

    public void setmChannelCategoryParentId(String mChannelCategoryParentId) {
        this.mChannelCategoryParentId = mChannelCategoryParentId;
    }

    public String getmChannelCategoryImage() {
        return mChannelCategoryImage;
    }

    public void setmChannelCategoryImage(String mChannelCategoryImage) {
        this.mChannelCategoryImage = mChannelCategoryImage;
    }

    public String getmChannelCategoryName() {
        return mChannelCategoryName;
    }

    public void setmChannelCategoryName(String mChannelCategoryName) {
        this.mChannelCategoryName = mChannelCategoryName;
    }
}
