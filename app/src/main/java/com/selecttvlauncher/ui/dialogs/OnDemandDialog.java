package com.selecttvlauncher.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Utils;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by panda on 5/4/15.
 */
public class OnDemandDialog extends Dialog {

    private Button butWatch;
    private Button butCancel;
    private TextView textTitle;
    private DynamicImageView imageThumbnail;

    public static String strTitle;
    public static String strUrl;
    public static int nID;

    private Context mainContext;
    private Activity mainActivity;

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    public static String THUMBNAIL_URL = "http://img.youtube.com/vi/";


    private File m_cacheDir;
    public OnDemandDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);

        mainContext = context;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ondemand_splash);

        butWatch = (Button) findViewById(R.id.butWatch);
        butCancel = (Button) findViewById(R.id.butCancel);
        textTitle = (TextView) findViewById(R.id.textVideoTitle);
        imageThumbnail = (DynamicImageView) findViewById(R.id.imageShowThumbnail);

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            m_cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "TESTVIDEO");
        else
            m_cacheDir = context.getCacheDir();
        if (!m_cacheDir.exists())
            m_cacheDir.mkdirs();


        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        butWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo();
            }
        });
    }

    private void watchVideo()
    {

       // ((HomeActivity)mainActivity).getmChannelFragment().watchvideo(strUrl,strTitle);
        ((HomeActivity)mainActivity).getmChannelTotalFragment().watchvideo(strUrl,strTitle);
            /*Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                    mainActivity, Constants.DEVELOPER_KEY, strUrl, 0, true, false);
            if (intent != null) {
                if (canResolveIntent(intent)) {
                    mainActivity.startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                } else {
                    // Could not resolve the intent - must need to install or update the YouTube API service.
                    YouTubeInitializationResult.SERVICE_MISSING
                            .getErrorDialog(mainActivity, REQ_RESOLVE_SERVICE_MISSING).show();
                }
            }*/
        dismiss();
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = mainActivity.getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    public void setInfo(String videoTitle, String videoUrl, int videoId)
    {
        strTitle = videoTitle;
        strUrl = videoUrl;
        nID = videoId;

        textTitle.setText(strTitle);

        String strImageUrl = THUMBNAIL_URL + strUrl +"/0.jpg";

        imageThumbnail.loadImage(strImageUrl);
        //imageThumbnail.setImageURI(Uri.parse(strImageUrl));
        //imageThumbnail.setImageBitmap(getBitmap(strImageUrl));
        //Bitmap bm = getBitmap(strImageUrl);
    }

    public void setActivity(Activity activity)
    {
        mainActivity = activity;
    }

    private Bitmap getBitmap(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        String filename = String.valueOf(url.hashCode());
        File f = new File(m_cacheDir, filename);

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        // from web
        try {
            Bitmap bitmap = null;
            InputStream is = new URL(url).openStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}
