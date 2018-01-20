package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DemandNetworkDialogFragment extends android.support.v4.app.DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private DynamicImageView network_imageView;
    private TextView network_desc_textView,overview_textView;
    private HashMap<String,String> networkDetails=new HashMap<>();
    private RecyclerView show_list;
    private GridLayoutManager layoutManager;
    private ProgressHUD mProgressHUD;
    private ImageView close;
    private ProgressBar center_progressBar;

    public DemandNetworkDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DemandNetworkDialogFragment newInstance(String param1, String param2) {
        DemandNetworkDialogFragment fragment = new DemandNetworkDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        //args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_detail, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        network_imageView=(DynamicImageView)view.findViewById(R.id.network_imageView);
        network_desc_textView=(TextView)view.findViewById(R.id.network_desc_textView);
        overview_textView=(TextView)view.findViewById(R.id.overview_textView);
        show_list=(RecyclerView)view.findViewById(R.id.show_list);
        close=(ImageView)view.findViewById(R.id.close);
        center_progressBar=(ProgressBar)view.findViewById(R.id.center_progressBar);

        layoutManager = new GridLayoutManager(getActivity(), 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        show_list.hasFixedSize();
        show_list.setLayoutManager(layoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        show_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        if(mParam1.equalsIgnoreCase("0")){
            new LoadingNetworkDetail().execute(mParam1);
        }else{
            new LoadingNetworkDetail().execute(mParam1);
        }



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getDialog().dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private ArrayList<GridBean> grid_list=new ArrayList<>();
    private class LoadingNetworkDetail extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;
        JSONObject network_detail;


        @Override
        protected void onPreExecute() {
            center_progressBar.setVisibility(View.VISIBLE);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
              /*  if(mParam2.equalsIgnoreCase("0")){
                    m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0,0);
                }else  if(mParam2.equalsIgnoreCase("1")){*/
                    m_jsonNetworkList = JSONRPCAPI.getAllShow(Integer.parseInt(params[0]));
                //}

                if (m_jsonNetworkList  == null) return null;

                network_detail = JSONRPCAPI.getNetworkDetails(Integer.parseInt(mParam2));




                if ( network_detail  != null) {
                    String slogan = network_detail.getString("slogan");
                    String name = network_detail.getString("name");
                    String headquarters = network_detail.getString("headquarters");
                    String start_time = network_detail.getString("start_time");
                    String image = network_detail.getString("image");
                    String description = network_detail.getString("description");
                    int id = network_detail.getInt("id");
                    String slug = network_detail.getString("slug");

                    networkDetails = new HashMap<>();
                    networkDetails.put("slogan", slogan);
                    networkDetails.put("name", name);
                    networkDetails.put("headquarters", headquarters);
                    networkDetails.put("start_time", start_time);
                    networkDetails.put("image", image);
                    networkDetails.put("description", description);
                    networkDetails.put("id", "" + id);
                    networkDetails.put("slug", slug);

                }

                if (m_jsonNetworkList!=null&&m_jsonNetworkList.length()>0) {

                    String show_id = "", show_name = "", show_image = "", show_description = "", show_type = "";
                    for (int i = 0; i < m_jsonNetworkList.length(); i++) {
                        JSONObject object = m_jsonNetworkList.getJSONObject(i);
                        if (object.has("id")) {
                            show_id = object.getString("id");
                        }
                        if (object.has("name")) {
                            show_name = object.getString("name");
                        }

                        if (object.has("poster_url")) {
                            show_image = object.getString("poster_url");
                        }else if (object.has("carousel_image")) {
                            show_image = object.getString("carousel_image");
                        }


                        if (object.has("type")) {
                            show_type = object.getString("type");

                            Log.d("type", "grid" + show_type);

                        }else{
                            show_type="s";
                        }

                        grid_list.add(new GridBean(show_id, show_name, show_image, show_description, show_type));

                    }
                }


                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                try {
                    center_progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String image_url=networkDetails.get("image");
                Log.d("Url:::","::::"+image_url);
                if(!TextUtils.isEmpty(image_url)){
                    network_imageView.loadImage(image_url);
                }
                overview_textView.setVisibility(View.VISIBLE);
                network_desc_textView.setText(networkDetails.get("description"));

                if (m_jsonNetworkList.length() > 0) {
                    GridNetworkListAdapter adpater = new GridNetworkListAdapter(grid_list, getContext());
                    show_list.setAdapter(adpater);
                    /*FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, network_listfragment);
                    network_fragmentTransaction.commit();*/

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    public class GridNetworkListAdapter extends RecyclerView.Adapter<GridNetworkListAdapter.DataObjectHolder> {
        ArrayList<GridBean> list_data;
        Context context;

        public GridNetworkListAdapter(ArrayList<GridBean> list_data, Context context) {
            try {
                this.list_data = list_data;
                this.context = context;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public GridNetworkListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(GridNetworkListAdapter.DataObjectHolder holder, final int position) {
            try {
                String image_url = list_data.get(position).getImage();
                final String type = list_data.get(position).getType();
                Log.d("categoryimage", "===>" + image_url);

                if (type.equalsIgnoreCase("n")||type.equalsIgnoreCase("l")) {
                    holder.imageView.setVisibility(View.GONE);
                    holder.gridview_item.setVisibility(View.VISIBLE);

                    holder.gridview_item.setVisibility(View.VISIBLE);
                    Picasso.with(context
                            .getApplicationContext())
                            .load(image_url)
                            .fit()
                            .placeholder(R.drawable.loader_network).into(holder.gridview_item);


                } else {

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
    }
}
