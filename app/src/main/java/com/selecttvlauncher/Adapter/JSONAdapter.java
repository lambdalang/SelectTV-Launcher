package com.selecttvlauncher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONAdapter extends BaseAdapter {

    private JSONArray m_data;
    private Context m_context;
    private LayoutInflater m_inflater = null;
    private ImageLoader m_imageLoader;

    public JSONAdapter(Context ctx, JSONArray data) {
        m_context = ctx;
        m_data = data;
        m_inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_imageLoader = new ImageLoader(m_context.getApplicationContext());
    }

    public int getCount() {
        return m_data.length();
    }

    public Object getItem(int position) {
        try {
            return m_data.getJSONObject(position);
        } catch (JSONException e) {

            return null;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView txtVideoName;
        public TextView txtStartTime;
        public DynamicImageView imgVideoThumbnail;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null) {
            vi = m_inflater.inflate(R.layout.ui_row_channel_details, parent,false);
            holder = new ViewHolder();
            holder.txtVideoName = (TextView) vi.findViewById(R.id.ui_video_row_txt_tittle);
            holder.txtStartTime = (TextView) vi.findViewById(R.id.ui_video_row_txt_time);
            holder.imgVideoThumbnail = (DynamicImageView) vi.findViewById(R.id.ui_video_row_image);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        try {
            JSONObject stream = m_data.getJSONObject(position);

            holder.txtVideoName.setText(stream.getString("title"));
            holder.txtStartTime.setText(stream.getString("time"));
            String strThumbUrl = "http://img.youtube.com/vi/" + stream.getString("url") + "/default.jpg";
            holder.imgVideoThumbnail.loadImage(strThumbUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vi;
    }

    public void stopImageLoading() {
        m_imageLoader.stopThread();
    }
}
