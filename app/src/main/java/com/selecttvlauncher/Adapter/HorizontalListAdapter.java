package com.selecttvlauncher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.views.DynamicImageView;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/8/2016.
 */
public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.DataObjectHolder> {
    ArrayList<HorizontalListitemBean> images;
    Context context;
    int width;
    GridAdapter.onGridSelectedListener listener;
    String name;
    public HorizontalListAdapter(ArrayList<HorizontalListitemBean> images, Context context, int width, GridAdapter.onGridSelectedListener listener, String name)
    {
        this.images=images;
        this.context=context;
        this.width=width;
        this.listener=listener;
this.name=name;
    }

public void setCategoryList(ArrayList<HorizontalListitemBean> images)
{
    this.images=images;

}
    @Override
    public HorizontalListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.horizontal_list_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalListAdapter.DataObjectHolder holder, final int position) {
        try
        {
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(width/4, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.horizontal_imageView.setLayoutParams(vp);
            String image_url=images.get(position).getImage();

            final String type=images.get(position).getType();
//            holder.horizontal_imageView.setImageResource(images.get(position));

            holder.horizontal_imageView.setImageDrawable(null);
if(type.equalsIgnoreCase("s"))
{
    holder.horizontal_imageView.loadImage(image_url);
}else if(type.equalsIgnoreCase("n"))
{
    holder.horizontal_imageView.loadImage(image_url);

}



            holder.horizontal_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShowGridItemSelected(images.get(position).getId(), type,images.get(position).getName());

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private final DynamicImageView horizontal_imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);


            horizontal_imageView = (DynamicImageView) itemView.findViewById(R.id.horizontal_imageView);
            Utilities.setViewFocus(context,horizontal_imageView);
        }
    }
}
