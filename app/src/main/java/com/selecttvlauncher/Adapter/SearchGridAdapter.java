package com.selecttvlauncher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/6/2016.
 */
public class SearchGridAdapter extends RecyclerView.Adapter<SearchGridAdapter.DataObjectHolder> {
    ArrayList<GridBean> list_data;
    Context context;
SearchGridClickListener searchGridClickListener;
    public SearchGridAdapter(ArrayList<GridBean> list_data, Context context, SearchGridClickListener searchGridClickListener) {
        try {
            this.list_data = list_data;
            this.context = context;
            this.searchGridClickListener=searchGridClickListener;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public SearchGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchGridAdapter.DataObjectHolder holder, final int position) {
        try {
            String image_url = list_data.get(position).getImage();
            final String type = list_data.get(position).getType();
            Log.d("categoryimage", "===>" + image_url);

            holder.gridview_item.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);

            if (image_url != null) {
                holder.imageView.loadImage(image_url);

            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("live.schedullerReted::", "grid selected");
                    if (type.equalsIgnoreCase(Constants.SEARCH_STATIONS)) {
                        Log.d("live.schedullerRated::", "selected"+list_data.get(position).getId());

                    }
                    searchGridClickListener.onsearchGriditemSelected(type,list_data.get(position).getId(), type, list_data.get(position).getName());

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private final DynamicImageView imageView;
        GridViewItem gridview_item;

        public DataObjectHolder(View itemView) {
            super(itemView);


            imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
            gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);

            Utilities.setViewFocus(context,imageView);
            Utilities.setViewFocus(context,gridview_item);
        }
    }



    public interface SearchGridClickListener {
        public void onsearchGriditemSelected(String search, String id, String type, String name);
    }
}
