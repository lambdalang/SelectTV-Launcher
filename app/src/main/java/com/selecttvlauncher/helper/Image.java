package com.selecttvlauncher.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Appsolute dev on 25-Sep-17.
 */

public class Image {
    public static void loadImage(Context context, String imagepath, ImageView imageView) {
        Glide.with(imageView).load(imagepath).thumbnail(0.2f).into(imageView);
    }
}
