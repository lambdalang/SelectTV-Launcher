package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvlauncher.BeanClass.ChannelCategoryList;
import com.selecttvlauncher.BeanClass.ChannelListBean;
import com.selecttvlauncher.BeanClass.ChannelSubCategoryList;
import com.selecttvlauncher.BeanClass.FavoriteBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class InterestChannelsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnInterestChannelFragmentInteractionListener mListener;

    private RecyclerView content_recyclerView,channel_main_list,channel_sub_list;
    private ProgressBar list_progressBar;
    private ArrayList<ChannelCategoryList> channel_list=new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private ImageView close_imageButton,back_imageView;
    private Button next_button,previous_button;
    LinearLayoutManager linearLayoutManager,sublinearLayoutManager;
    private RelativeLayout main_list_layout,sub_list_layout;
    private FrameLayout frame1,frame2;
    int mainList_SelectedItem=0;
    int subList_SelectedItem=0;
    private TextView skip_text,title_txt,desc_textView;
    private Typeface OpenSans_Bold,OpenSans_Regular,OpenSans_Semibold,osl_ttf;
    private boolean isMainList=true;

    public InterestChannelsFragment() {
        // Required empty public constructor
    }


    public static InterestChannelsFragment newInstance(String param1, String param2) {
        InterestChannelsFragment fragment = new InterestChannelsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_channels, container, false);


        text_font_typeface();


        content_recyclerView=(RecyclerView)view.findViewById(R.id.content_recyclerView);
        channel_main_list=(RecyclerView)view.findViewById(R.id.channel_main_list);
        channel_sub_list=(RecyclerView)view.findViewById(R.id.channel_main_list);
        list_progressBar=(ProgressBar)view.findViewById(R.id.list_progressBar);
        back_imageView=(ImageView)view.findViewById(R.id.back_imageView);
        next_button=(Button)view.findViewById(R.id.next_button);
        previous_button=(Button)view.findViewById(R.id.previous_button);
        skip_text=(TextView)view.findViewById(R.id.skip_text);
        title_txt=(TextView)view.findViewById(R.id.title_txt);
        desc_textView=(TextView)view.findViewById(R.id.desc_textView);

        title_txt.setTypeface(OpenSans_Regular);
        desc_textView.setTypeface(OpenSans_Regular);
        previous_button.setTypeface(OpenSans_Regular);
        next_button.setTypeface(OpenSans_Regular);
        skip_text.setTypeface(OpenSans_Regular);

        frame1=(FrameLayout)view.findViewById(R.id.frame1);
        frame2=(FrameLayout)view.findViewById(R.id.frame2);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        sublinearLayoutManager = new LinearLayoutManager(getActivity());
        channel_main_list.setLayoutManager(linearLayoutManager);
        channel_sub_list.setLayoutManager(sublinearLayoutManager);

        gridLayoutManager = new GridLayoutManager(getActivity(), 6);
        content_recyclerView.setLayoutManager(gridLayoutManager);
        content_recyclerView.setHasFixedSize(true);
        int spanCount = 6; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        content_recyclerView.setFocusable(true);
        content_recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        content_recyclerView.setFocusable(true);

        Utilities.setViewFocus(getActivity(), back_imageView);
        Utilities.setViewFocus(getActivity(),next_button);
        Utilities.setViewFocus(getActivity(), previous_button);
        Utilities.setTextFocus(getActivity(),skip_text);


        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
            new LoadingChannelCategoryList().execute();


        }else{
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNetworkDialog();
            }
        });

        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMainList){
                    isMainList=true;
                    displayMainCategoryList();
                }else{
                    closeDialog();
                }

            }
        });
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenreDialog();
            }
        });
        skip_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenreDialog();
            }
        });
        return view;
    }
    private void showNetworkDialog() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        InterestNetworkDialogFragment mInterestNetworkDialogFragment = new InterestNetworkDialogFragment();
        fragmentTransaction.replace(R.id.fragment_dialogs, mInterestNetworkDialogFragment);
        fragmentTransaction.commit();



    }
    private void showGenreDialog() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        InterestGenreDialogFragment mInterestGenreDialogFragment = new InterestGenreDialogFragment();
        fragmentTransaction.replace(R.id.fragment_dialogs, mInterestGenreDialogFragment);
        fragmentTransaction.commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void closeDialog() {
        if (mListener != null) {
            mListener.onInterestChannelFragmentcloseDialog();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInterestChannelFragmentInteractionListener) {
            mListener = (OnInterestChannelFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnInterestChannelFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInterestChannelFragmentcloseDialog();
    }

    private class LoadingChannelCategoryList extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            try {
                JSONArray m_jsoncategories = JSONRPCAPI.getCategories();
                if (m_jsoncategories == null) return null;
                Log.d("m_jsoncategories::", "::" + m_jsoncategories);
                if(m_jsoncategories.length()>0){
                    for(int i=0;i<m_jsoncategories.length();i++){
                        JSONObject obj=m_jsoncategories.getJSONObject(i);
                        boolean b = obj.isNull("parent_id");
                        if (b) {
                            String parent_id="";
                            String image=obj.getString("image");
                            int id=obj.getInt("id");
                            String name=obj.getString("name");

                            ArrayList<ChannelSubCategoryList> subList=new ArrayList<>();
                            for (int j = 0; j < m_jsoncategories.length(); j++) {
                                JSONObject sub_objItem = m_jsoncategories.getJSONObject(j);
                                String sub_parent_id=sub_objItem.getString("parent_id");
                                String sub_image=sub_objItem.getString("image");
                                int sub_id=sub_objItem.getInt("id");
                                String sub_name=sub_objItem.getString("name");
                                if(!TextUtils.isEmpty(sub_parent_id)&&sub_parent_id.equalsIgnoreCase(Integer.toString(id))){
                                    subList.add(new ChannelSubCategoryList(sub_id,sub_parent_id,sub_image,sub_name));
                                }
                            }

                            channel_list.add(new ChannelCategoryList(parent_id,id,image,name,subList));

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list_progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                list_progressBar.setVisibility(View.GONE);
                displayMainCategoryList();
                int first_id=0;
                if(channel_list.get(0).getSubCategories().size()>0){
                    first_id=channel_list.get(0).getSubCategories().get(0).getId();
                }else{
                    first_id=channel_list.get(0).getId();
                }
                new ChannelList().execute(first_id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayMainCategoryList() {
        ChannelCategoryAdapter mChannelCategoryAdapter = new ChannelCategoryAdapter(channel_list, getActivity());
        channel_main_list.setAdapter(mChannelCategoryAdapter);
        channel_main_list.invalidate();
        channel_main_list.smoothScrollToPosition(mainList_SelectedItem);
    }

    private void displayNetworkList() {

    }

    private class ChannelCategoryAdapter extends RecyclerView.Adapter<ChannelCategoryAdapter.DataObjectHolder> {
        ArrayList<ChannelCategoryList> m_channelViews;
        Context context;

        public ChannelCategoryAdapter(ArrayList<ChannelCategoryList> m_channelViews, Context context) {
            this.m_channelViews = m_channelViews;
            this.context = context;
        }


        @Override
        public ChannelCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChannelCategoryAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(m_channelViews.get(position).getName());
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);

            if (position == mainList_SelectedItem) {
                Log.d("drawable", "position  " + mainList_SelectedItem);
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }

            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mainList_SelectedItem=position;
                        subList_SelectedItem=-1;
                        Log.d("sublist size::",":::"+m_channelViews.get(position).getSubCategories().size());
                        if(m_channelViews.get(position).getSubCategories().size()>0){
                          /*  frame1.setVisibility(View.GONE);
                            frame2.setVisibility(View.VISIBLE);*/
                            setSubCategory(m_channelViews.get(position).getSubCategories());
                        }else{
                            new ChannelList().execute(m_channelViews.get(position).getId());
                            notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return m_channelViews.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);

                Utilities.setViewFocus(getActivity(),fragment_ondemandlist_items_layout);
            }
        }
    }

    private class ChannelSubCategoryAdapter extends RecyclerView.Adapter<ChannelSubCategoryAdapter.DataObjectHolder> {
        ArrayList<ChannelSubCategoryList> m_channelViews;
        Context context;
        int mSelectedItem=0;

        public ChannelSubCategoryAdapter(ArrayList<ChannelSubCategoryList> m_channelViews, Context context) {
            this.m_channelViews = m_channelViews;
            this.context = context;
        }


        @Override
        public ChannelSubCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChannelSubCategoryAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(m_channelViews.get(position).getName());

            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            if (position == subList_SelectedItem) {
                Log.d("drawable", "position  " + subList_SelectedItem);
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }

            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subList_SelectedItem=position;

                    new ChannelList().execute(m_channelViews.get(position).getId());
                    notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return m_channelViews.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);

                Utilities.setViewFocus(getActivity(),fragment_ondemandlist_items_layout);
            }
        }
    }

    private void setSubCategory(ArrayList<ChannelSubCategoryList> subCategories) {
        isMainList=false;
        ChannelSubCategoryAdapter mChannelsubCategoryAdapter = new ChannelSubCategoryAdapter(subCategories, getActivity());
        channel_main_list.setAdapter(mChannelsubCategoryAdapter);
        channel_main_list.invalidate();
    }


    private class ChannelList extends AsyncTask<Integer, Integer, Object> {

        ArrayList<ChannelListBean> mchannelList=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            list_progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Integer... params) {

            JSONArray channel_array = JSONRPCAPI.getChannels(params[0]);
            if (channel_array == null) return null;
            try {
                for (int i = 0; i < channel_array.length(); i++) {
                    JSONObject obj = channel_array.getJSONObject(i);
                    String big_logo_url=obj.getString("big_logo_url");
                    String description=obj.getString("description");
                    String logo_url=obj.getString("logo_url");
                    int id=obj.getInt("id");
                    String name=obj.getString("name");

                    mchannelList.add(new ChannelListBean(big_logo_url,description,logo_url,name,id));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {

            list_progressBar.setVisibility(View.GONE);
            InterestChannelGridAdapter mInterestChannelGridAdapter= new InterestChannelGridAdapter(mchannelList, getActivity());
            content_recyclerView.setAdapter(mInterestChannelGridAdapter);

        }
    }

    private class InterestChannelGridAdapter extends RecyclerView.Adapter<InterestChannelGridAdapter.DataObjectHolder> {
        ArrayList<ChannelListBean> listchannelBeans;
        Context context;

        public InterestChannelGridAdapter(ArrayList<ChannelListBean> listchannelBeans, Context context) {
            this.listchannelBeans = listchannelBeans;
            this.context = context;
        }

        @Override
        public InterestChannelGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.interest_channel_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final InterestChannelGridAdapter.DataObjectHolder holder, final int position) {
            holder.dynamic_imageview.loadImage(listchannelBeans.get(position).getBig_logo_url());
            holder.text.setText(listchannelBeans.get(position).getName());
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setTypeface(OpenSans_Regular);
            if(isfavorited(listchannelBeans.get(position).getId())){
                holder.checkBox.setChecked(true);
                holder.checkBox.setVisibility(View.VISIBLE);
            }else{
                holder.checkBox.setChecked(false);
                holder.checkBox.setVisibility(View.GONE);
            }

            holder.network_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkBox.isChecked()){
                        new AddFavorites().execute("remove",""+listchannelBeans.get(position).getId(),"");
                        holder.checkBox.setVisibility(View.GONE);
                        holder.checkBox.setChecked(false);
                    }else{
                        new AddFavorites().execute("add",""+listchannelBeans.get(position).getId(),listchannelBeans.get(position).getName());
                        holder.checkBox.setVisibility(View.VISIBLE);
                        holder.checkBox.setChecked(true);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listchannelBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            LinearLayout network_layout;
            DynamicImageView dynamic_imageview;
            TextView text;

            public DataObjectHolder(View itemView) {
                super(itemView);
                checkBox=(CheckBox)itemView.findViewById(R.id.checkBox);
                network_layout=(LinearLayout)itemView.findViewById(R.id.genre_layout);
                dynamic_imageview=(DynamicImageView)itemView.findViewById(R.id.dynamic_imageview);
                text=(TextView)itemView.findViewById(R.id.text);

                Utilities.setViewFocus(getActivity(),network_layout);

            }
        }
    }

    private void text_font_typeface() {
        try {
            OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
            OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
            osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AddFavorites extends AsyncTask<String, Object, Object> {
        JSONObject m_response;
        String task="";
        int id;
        String name="";

        @Override
        protected Object doInBackground(String... params) {
            try {
                Log.d("accesstoken::","::"+ PreferenceManager.getAccessToken(getActivity()));
                task=params[0];
                id=Integer.parseInt(params[1]);
                name=params[2];
                if(params[0].equals("add")){
                    m_response = JSONRPCAPI.addToFavorite(PreferenceManager.getAccessToken(getActivity()),"channel",Integer.parseInt(params[1]));
                    if ( m_response == null) return null;
                    Log.d("m_response::", "::" + m_response);
                }else  if(params[0].equals("remove")){
                    m_response = JSONRPCAPI.removeFavorite(PreferenceManager.getAccessToken(getActivity()), "channel", Integer.parseInt(params[1]));
                    if ( m_response == null) return null;
                    Log.d("m_response::", "::" + m_response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if(m_response.has("sucess")){
                    if(m_response.getBoolean("sucess")){
                        if(task.equalsIgnoreCase("add")){
                            HomeActivity.channelsList.add(new FavoriteBean("","",name,id,"","",""));
                            Toast.makeText(getActivity(),"Added to Favorite List",Toast.LENGTH_SHORT).show();
                        }else{

                            for(Iterator<FavoriteBean> it = HomeActivity.channelsList.iterator(); it.hasNext();) {
                                FavoriteBean s = it.next();
                                if(s.getId() == id) {
                                    it.remove();
                                }
                            }
                            Toast.makeText(getActivity(),"Removed from Favorite List",Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isfavorited(int id) {
        boolean result=false;
        for(FavoriteBean b : HomeActivity.channelsList){
            if(b.getId()==id){
                result=true;
            }else {

            }
        }
        return result;
    }
}
