package com.selecttvlauncher.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.selecttvlauncher.R;
import com.squareup.picasso.Picasso;

/**
 * Custom ImageView class which has interface callbacks to load images from rest
 *
 * @author nine3_marks
 */
public class DynamicImageView extends ImageView {

    private String strUrl;
    private Context mContext;

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    public void loadRoundedImage(String imageUrl) {
        try {
            Picasso.with(getContext()
                    .getApplicationContext())
                    .load(imageUrl)
                    .fit().centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .transform(new RoundedTransformation(20, 0)).into(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(final String imageUrl) {

        try {
            strUrl = imageUrl;
            ImageView ivv=this;
            if(!TextUtils.isEmpty(strUrl)) {
                Picasso.with(getContext()
                        .getApplicationContext())
                        .load(strUrl)
                        .fit().centerInside()
                        .placeholder(R.drawable.loader_show).into(this);
            }else{
                ivv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.loader_show));
            }



            /*Picasso.with(getContext()
                    .getApplicationContext())
                    .load(imageUrl).placeholder(R.drawable.thumbnail_loading)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(this, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            reloadImage(imageUrl);
                        }
                    });*/



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadImage(String imageUrl) {

        try {
            strUrl = imageUrl;

                            Picasso.with(getContext()
                                    .getApplicationContext())
                                    .load(imageUrl)
                                    .fit()
                                    .placeholder(R.drawable.thumbnail_loading).into(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadSliderImage(String imageUrl) {

        try {
            strUrl = imageUrl;

            Picasso.with(getContext()
                    .getApplicationContext())
                    .load(imageUrl)
                    .fit()
                    .into(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMovieImage(String imageUrl) {

        strUrl = imageUrl;
        ImageView ivv=this;
        if(!TextUtils.isEmpty(strUrl)) {
            Picasso.with(getContext()
                    .getApplicationContext())
                    .load(imageUrl)
                    .fit().centerInside()
                    .placeholder(R.drawable.loader_movie).into(this);
        }else{
            ivv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.loader_show));
        }
    }

    public String getImageurl(){
        return strUrl;
    }
    public class RoundedTransformation implements
            com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin; // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            try {
                Bitmap output = Bitmap.createBitmap(source.getWidth(),
                        source.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
                        - margin, source.getHeight() - margin), radius, radius, paint);

                if (source != output) {
                    source.recycle();
                }

                return output;
            } catch (Exception e) {
                e.printStackTrace();
                return source;
            }
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
