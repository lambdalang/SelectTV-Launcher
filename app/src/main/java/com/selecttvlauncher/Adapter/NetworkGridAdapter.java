package com.selecttvlauncher.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/8/2016.
 */
public class NetworkGridAdapter extends RecyclerView.Adapter<NetworkGridAdapter.DataObjectHolder> {
    ArrayList<HorizontalListitemBean> images;
    ArrayList<GridBean> list_data;
    Context context;
    GridClickListener listener;
    GridAdapter.onGridSelectedListener onGridSelectedListener;
    int width;
    String name;

    public NetworkGridAdapter(ArrayList<HorizontalListitemBean> images, Context context, int width, GridAdapter.onGridSelectedListener onGridSelectedListener, GridClickListener listener, String name) {
        try {
            this.images = images;
            this.context = context;
            this.width = width;
            this.listener = listener;
            this.onGridSelectedListener=onGridSelectedListener;
            this.name = name;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public NetworkGridAdapter(ArrayList<HorizontalListitemBean> images, Context context, GridClickListener listener) {
        try {
            this.images = images;
            this.context = context;
            this.listener = listener;

            this.width = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public NetworkGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.fragment_network_grid_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(NetworkGridAdapter.DataObjectHolder holder, final int position) {
        try {


            String image_url = images.get(position).getImage();
            final int id = images.get(position).getId();
            final String type = images.get(position).getType();

            holder.gridview_item.setFocusable(true);
            holder.horizontal_imageView.setFocusable(true);

            Utilities.setViewFocus(context, holder.horizontal_imageView);
            Utilities.setViewFocus(context, holder.gridview_item);

            if(type.equalsIgnoreCase("g"))
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.horizontal_imageView.setLayoutParams(layoutParams);
                holder.horizontal_imageView.setVisibility(View.VISIBLE);
                holder.gridview_item.setVisibility(View.GONE);

                //holder.horizontal_imageView.loadImage(image_url);

                if(!TextUtils.isEmpty(image_url)){

                    Picasso.with(context.getApplicationContext()).load(image_url).fit().centerInside().placeholder(R.drawable.loader_show).into(holder.horizontal_imageView);

                }else{
                    holder.horizontal_imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.loader_show));
                }


                holder.horizontal_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGridSelectedListener.onShowGridItemSelected(images.get(position).getId(), type, images.get(position).getName());

                    }
                });
            }else
            if (type.equalsIgnoreCase("s")) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.horizontal_imageView.setLayoutParams(layoutParams);
                holder.horizontal_imageView.setVisibility(View.VISIBLE);
                holder.gridview_item.setVisibility(View.GONE);

                //holder.horizontal_imageView.loadImage(image_url);

                if(!TextUtils.isEmpty(image_url)){

                    Picasso.with(context.getApplicationContext()).load(image_url).fit().centerInside().placeholder(R.drawable.loader_show).into(holder.horizontal_imageView);

                }else{
                    holder.horizontal_imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.loader_show));
                }


                holder.horizontal_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGridSelectedListener.onShowGridItemSelected(images.get(position).getId(), type, images.get(position).getName());

                    }
                });


            } else if (type.equalsIgnoreCase("m"))
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.horizontal_imageView.setLayoutParams(layoutParams);
                holder.horizontal_imageView.setVisibility(View.VISIBLE);
                holder.gridview_item.setVisibility(View.GONE);
                holder.horizontal_imageView.loadMovieImage(image_url);
                holder.horizontal_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGridSelectedListener.onShowGridItemSelected(images.get(position).getId(), type, images.get(position).getName());

                    }
                });
            }




            else {

                if(width!=0)
                {
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
                    holder.gridview_item.setLayoutParams(vp);
                }

               /* Log.d("array", "values" + images.get(5).getType());
                Log.d("array2", "values" + images.get(7).getType());*/

                holder.horizontal_imageView.setVisibility(View.GONE);
                holder.gridview_item.setVisibility(View.VISIBLE);
                Picasso.with(context
                        .getApplicationContext())
                        .load(image_url)

                        .placeholder(R.drawable.loader_network).into(holder.gridview_item);

                holder.gridview_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Selected Network::::", "id:::" + String.valueOf(id));
                        listener.onGriditemSelected(Constants.NETWORK, String.valueOf(id), type, images.get(position).getName());
                    }
                });



            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private final GridViewItem gridview_item;
        DynamicImageView horizontal_imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);

            gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);
            horizontal_imageView = (DynamicImageView) itemView.findViewById(R.id.horizontal_imageView);

            Utilities.setViewFocus(context,gridview_item);
            Utilities.setViewFocus(context,horizontal_imageView);
        }
    }

    public interface GridClickListener {
        public void onGriditemSelected(String network, String id, String type, String name);
    }


}