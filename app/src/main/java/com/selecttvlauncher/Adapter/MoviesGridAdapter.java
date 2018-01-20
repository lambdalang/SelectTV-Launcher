package com.selecttvlauncher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;

import java.util.ArrayList;

/**
 * Created by ${Madhan} on 8/3/2016.
 */
public class MoviesGridAdapter  extends RecyclerView.Adapter<MoviesGridAdapter.DataObjectHolder> {
    ArrayList<GridBean> list_data;
    Context context;
    onGridSelectedListener listener;

    public MoviesGridAdapter(ArrayList<GridBean> list_data, Context context, onGridSelectedListener listener) {
        try {
            this.list_data = list_data;
            this.context = context;
            this.listener = listener;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setData(ArrayList<GridBean> list_data)
    {
        this.list_data = list_data;
    }


    @Override
    public MoviesGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesGridAdapter.DataObjectHolder holder, final int position) {
        try {
            final String image_url = list_data.get(position).getImage();
            final String type = list_data.get(position).getType();


         /*   switch (type.toLowerCase())
            {
                case "m":*/
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);

                    holder.imageView.loadMovieImage(image_url);


                        /*Picasso.with(context).load(image_url).fit().centerInside()
                                .networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.loader_movie).into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(image_url)
                                        .placeholder(R.drawable.loader_movie)
                                        .into(holder.imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                            }
                        });
*/




                  /*  break;
                case "s":
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {
                        holder.imageView.loadMovieImage(image_url);

                    } else {
                        if (image_url != null) {
                            holder.imageView.loadImage(image_url);

                        }}

                    break;
                case "n":
                    holder.imageView.setVisibility(View.GONE);
                    holder.gridview_item.setVisibility(View.VISIBLE);

                    holder.gridview_item.setVisibility(View.VISIBLE);
                    Picasso.with(context
                            .getApplicationContext())
                            .load(image_url)
                            .fit()
                            .placeholder(R.drawable.loader_network).into(holder.gridview_item);
                    break;
                case "l":
                    holder.imageView.setVisibility(View.GONE);
                    holder.gridview_item.setVisibility(View.VISIBLE);

                    holder.gridview_item.setVisibility(View.VISIBLE);
                    Picasso.with(context
                            .getApplicationContext())
                            .load(image_url)
                            .fit()
                            .placeholder(R.drawable.loader_network).into(holder.gridview_item);
                    break;
            }*/


            Log.d("categoryimage", "===>" + image_url);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("type","all"+type);
                    listener.onShowGridItemSelected(Integer.parseInt(list_data.get(position).getId()), type, list_data.get(position).getName());

                }
            });


            holder.gridview_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("type", "all" + type);
                    listener.onShowGridItemSelected(Integer.parseInt(list_data.get(position).getId()), type, list_data.get(position).getName());
                }
            });



           /* if(type.equalsIgnoreCase("m"))
            {

                holder.gridview_item.setVisibility(View.GONE);

                holder.imageView.setVisibility(View.VISIBLE);


                if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {
                    holder.imageView.loadMovieImage(image_url);

                } else {
                    if (image_url != null) {
                        holder.imageView.loadImage(image_url);

                    }


                }
            }



            if (type.equalsIgnoreCase("n")||type.equalsIgnoreCase("l")) {
                holder.imageView.setVisibility(View.GONE);
                holder.gridview_item.setVisibility(View.VISIBLE);

                holder.gridview_item.setVisibility(View.VISIBLE);
                Picasso.with(context
                        .getApplicationContext())
                        .load(image_url)
                        .fit()
                        .placeholder(R.drawable.loader_network).into(holder.gridview_item);


            }*/

            /*else {

                holder.gridview_item.setVisibility(View.GONE);

                holder.imageView.setVisibility(View.VISIBLE);


                if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {
                    holder.imageView.loadMovieImage(image_url);

                } else {
                    if (image_url != null) {
                        holder.imageView.loadImage(image_url);

                    }


                }







            }*/


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

            Utilities.setViewFocus(context, imageView);
            Utilities.setViewFocus(context,gridview_item);
        }
    }

    public interface onGridSelectedListener {
        public void onShowGridItemSelected(int id, String type, String name);
    }
}
