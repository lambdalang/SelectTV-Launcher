//package com.selecttvlauncher.ui.fragments;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.android.youtube.player.YouTubeInitializationResult;
//import com.google.android.youtube.player.YouTubePlayer;
//import com.google.android.youtube.player.YouTubePlayerSupportFragment;
//import com.selecttvlauncher.BeanClass.ChannelCategoryList;
//import com.selecttvlauncher.BeanClass.ChannelScheduler;
//import com.selecttvlauncher.BeanClass.ChannelSubCategoryList;
//import com.selecttvlauncher.BeanClass.VideoBean;
//import com.selecttvlauncher.R;
//import com.selecttvlauncher.network.JSONRPCAPI;
//import com.selecttvlauncher.ui.activities.HomeActivity;
//import com.selecttvlauncher.ui.dialogs.ProgressHUD;
//import com.selecttvlauncher.ui.views.DynamicImageView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//
//public class ChannelFragmentNew extends Fragment implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaylistEventListener {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private RelativeLayout linearFullScreen,layout_all_channelview,layout_channelviewby_list;
//
//
//    private OnChannelFragmentInteractionListener mListener;
//
//    YouTubePlayerSupportFragment youTubePlayerFragment;
//
//    private RecyclerView fragment_ondemand_alllist_items;
//    private LinearLayoutManager mLayoutManager;
//    private int mMainCategorySelectedItem = 0;
//
//    private int nChannelID = -1;
//    private int nCategory = 0;
//    public int nTimeLimit = 2400;
//
//    long nMinStartTime = 0;
//    int nMaxWidth = 0;
//
//    private LinearLayout linearScrollChannelTitle,linearScrollChannelDesc,botton_layout;
//    private ProgressBar list_progressBar;
//
//    ArrayList<ChannelScheduler> channel_scheduler_list;
//    View firstRow;
//
//    public ChannelFragmentNew() {
//        // Required empty public constructor
//    }
//
//
//    public static ChannelFragmentNew newInstance(String param1, String param2) {
//        ChannelFragmentNew fragment = new ChannelFragmentNew();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_channel_fragment_new, container, false);
//
//        linearFullScreen=(RelativeLayout)view.findViewById(R.id.linearFullScreen);
//        layout_all_channelview=(RelativeLayout)view.findViewById(R.id.layout_all_channelview);
//        layout_channelviewby_list=(RelativeLayout)view.findViewById(R.id.layout_channelviewby_list);
//
//        fragment_ondemand_alllist_items = (RecyclerView) view.findViewById(R.id.fragment_ondemand_alllist_items);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
//        fragment_ondemand_alllist_items.hasFixedSize();
//
//        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
//
//        linearScrollChannelTitle = (LinearLayout) view.findViewById(R.id.linearscrollChannelTitle);
//        linearScrollChannelDesc = (LinearLayout) view.findViewById(R.id.linearscrollChannelDesc);
//        botton_layout = (LinearLayout) view.findViewById(R.id.botton_layout);
//        list_progressBar = (ProgressBar) view.findViewById(R.id.list_progressBar);
//
//
//        loadCategoriesAndShow();
//
//
//
//
//        return view;
//
//    }
//
//    private void loadCategoriesAndShow() {
//        showloaderView();
//        new LoadingCategoryTask().execute();
//    }
//
//    private class LoadingCategoryTask extends AsyncTask<Object, Integer, Object> {
//
//        //ProgressHUD mProgressHUD;
//
//
//        @Override
//        protected void onPreExecute() {
//            //  mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            JSONArray array = JSONRPCAPI.getCategories();
//            if (array == null) return null;
//            for (int i = 0; i < 50; i++) {
//                try {
//                    Thread.sleep(100);
//                    publishProgress(i);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            HomeActivity.channelCategoryList=new ArrayList<>();
//
//            if (array != null) {
//                for (int i = 0; i < array.length(); i++) {
//                    try {
//                        JSONObject objItem = array.getJSONObject(i);
//
//                        boolean b = objItem.isNull("parent_id");
//                        if (b) {
//                            if (!objItem.getString("id").equals("0")) {
//                                String parent_id="";
//                                String image=objItem.getString("image");
//                                int id=objItem.getInt("id");
//                                String name=objItem.getString("name");
//
//                                ArrayList<ChannelSubCategoryList> subList=new ArrayList<>();
//                                for (int j = 0; j < array.length(); j++) {
//                                    JSONObject sub_objItem = array.getJSONObject(i);
//                                    String sub_parent_id=sub_objItem.getString("parent_id");
//                                    String sub_image=sub_objItem.getString("image");
//                                    int sub_id=sub_objItem.getInt("id");
//                                    String sub_name=sub_objItem.getString("name");
//                                    if(!TextUtils.isEmpty(sub_parent_id)&&sub_parent_id.equalsIgnoreCase(Integer.toString(id))){
//                                        subList.add(new ChannelSubCategoryList(sub_id,sub_parent_id,sub_image,sub_name));
//                                    }
//                                }
//
//                                HomeActivity.channelCategoryList.add(new ChannelCategoryList(parent_id,id,image,name,subList));
//                            }
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            // mProgressHUD.dismiss();
//            showChannelsView();
//
//            if (HomeActivity.channelCategoryList.size()>0) {
//                ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(HomeActivity.channelCategoryList, getActivity());
//                fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
//                nCategory=145;
//                new FirstLoadingTask().execute();
//            }
//        }
//    }
//    private class FirstLoadingTask extends AsyncTask<Object, Integer, Object> {
//        ProgressHUD mProgressHUD;
//        JSONArray m_jsonLiveItems;
//        String IDs="";
//        ArrayList<ChannelScheduler> scheduler_list;
//
//        @Override
//        protected void onPreExecute() {
//            list_progressBar.setVisibility(View.VISIBLE);
//            botton_layout.setVisibility(View.GONE);
//            //mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            if (nCategory != -1)
//                m_jsonLiveItems = JSONRPCAPI.getChannels(nCategory);
//
//            if (m_jsonLiveItems == null) return null;
//            try {
//               /* for (int i = 50; i <= 100; i++) {
//                    try {
//                        Thread.sleep(100);
//                        publishProgress(i);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }*/
//
//                String IDs = "";
//                scheduler_list=new ArrayList<>();
//                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
//                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
//                    if (IDs.length() == 0)
//                        IDs = obj.getInt("id") + "";
//                    else
//                        IDs = IDs + "," + obj.getInt("id");
//                }
//                int nTimeOffset = 0;
//                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);
//                if (objSchedule == null) return null;
//                if (objSchedule != null) {
//                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
//                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
//                        IDs = obj.getInt("id") + "";
//                        int channelId = obj.getInt("id");
//                        String channelName = obj.getString("name");
//                        String channelLogo = obj.getString("big_logo_url");
//                        JSONArray channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
//                        if(channelVideos!=null&&channelVideos.length()>0){
//                            ArrayList<VideoBean> v_list=new ArrayList<>();
//                            for(int k=0;k<channelVideos.length();k++){
//
//                                JSONObject videoObj=channelVideos.getJSONObject(k);
//
//                                int seq=videoObj.getInt("seq");
//                                String title=videoObj.getString("title");
//                                String url=videoObj.getString("url");
//                                Long unix_time=videoObj.getLong("unix_time");
//                                String time=videoObj.getString("time");
//                                Long duration=videoObj.getLong("duration");
//                                int id=videoObj.getInt("id");
//
//                                v_list.add(new VideoBean(seq,id,title,url,time,unix_time,duration));
//
//
//                            }
//                            scheduler_list.add(new ChannelScheduler(channelId,channelLogo,"","",channelName,v_list));
//                        }
//
//
//
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            //  mProgressHUD.dismiss();
//            //mProgressHUD.dismiss();;
//            list_progressBar.setVisibility(View.GONE);
//            botton_layout.setVisibility(View.VISIBLE);
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    makeChannelTitleList(scheduler_list);
//                }
//            });
//
//
//
//
//
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//
//        }
//
//
//    }
//
//    private void loadChannelViews(ArrayList<ChannelScheduler> scheduler_list) {
//        channel_scheduler_list=new ArrayList<>();
//        channel_scheduler_list=scheduler_list;
//        new makeChannelListService().execute();
//
//    }
//
//    private class makeChannelListService extends AsyncTask <Void, Void, Object> {
//        int maxCount = 0;
//        ProgressHUD mProgressHUD;
//        ViewGroup.LayoutParams layoutParams = null;
//        LayoutInflater m_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//
//        @Override
//        protected void onPreExecute() {
//            //mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
//
//            list_progressBar.setVisibility(View.VISIBLE);
//            botton_layout.setVisibility(View.GONE);
//            linearScrollChannelDesc.removeAllViews();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            try {
//
//                if (channel_scheduler_list.size() > 0) {
//                    long nMaxEndTime = 0;
//                    nMinStartTime = 0;
//                    for (int i = 0; i < channel_scheduler_list.size(); i++) {
//                        ChannelScheduler item = channel_scheduler_list.get(i);
//                        try {
//                            VideoBean objStart = item.getVideolist().get(0);
//                            VideoBean objEnd = item.getVideolist().get(item.getVideolist().size() - 1);
//
//                            if (nMinStartTime == 0)
//                                nMinStartTime = objStart.getUnix_time();
//                            if (objStart.getUnix_time() < nMinStartTime)
//                                nMinStartTime = objStart.getUnix_time();
//
//                            if (nMaxEndTime < (objEnd.getUnix_time()) + objEnd.getDuration())
//                                nMaxEndTime = (objEnd.getUnix_time() + objEnd.getDuration());
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    nMaxWidth = 0;
//                    for (int k = 0; k < channel_scheduler_list.size(); k++) {
//                        ChannelScheduler model = channel_scheduler_list.get(k);
//                        long nPrevStartTime = nMinStartTime;
//                        int nWidth = 0;
//                        if (maxCount < model.getVideolist().size()) {
//                            maxCount = model.getVideolist().size();
//                        }
//                        for (int i = 0; i < model.getVideolist().size(); i++) {
//                            try {
//                                VideoBean obj = model.getVideolist().get(i);
//                                long nDeltaTime = obj.getUnix_time() - nPrevStartTime;
//                                if (nDeltaTime > 0) {
//                                    nWidth += (int) nDeltaTime;
//                                }
//
//                                if ((obj.getDuration()) < 100) {
//                                    nWidth += 130;
//                                }/*else if( (obj.getLong("duration") ) > 500 ){
//                        nWidth += 500;
//                    }*/ else
//                                    nWidth += (int) (obj.getDuration() + 30);
//
//                                nPrevStartTime = obj.getUnix_time() + obj.getDuration();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (nMaxWidth < nWidth)
//                            nMaxWidth = nWidth;
//
//                    }
//
//
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            //mProgressHUD.dismiss();
//
//            list_progressBar.setVisibility(View.GONE);
//            botton_layout.setVisibility(View.VISIBLE);
//
//
//            //int nWidth = channelTabContent.getMeasuredWidth() / 2;
//
//
//            if (channel_scheduler_list.size() != 0) {
//                for (int k = 0; k <channel_scheduler_list.size(); k++) {
//
//                    View row = m_inflater.inflate(R.layout.item_channel_desc, null);
//                    LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_channel_desc);
//
//                    int nTempWidth = 0;
//
//                    try {
//                        linear.removeAllViews();
//
//                        long nPrevStartTime = nMinStartTime;
//
//                        ArrayList<VideoBean> data_list = channel_scheduler_list.get(k).getVideolist();
//
//                        for (int i = 0; i < 3; i++) {
//
//                           /* LinearLayout ll=new LinearLayout(getActivity());
//                            ll.setOrientation(LinearLayout.VERTICAL);
//
//                            // Create TextView
//                            TextView c_name = new TextView(getActivity());
//                            c_name.setText(data_list.get(i).getTitle());
//                            c_name.setSingleLine(true);
//                            c_name.setTextColor(getResources().getColor(R.color.white));
//                            ll.addView(c_name);
//
//                            // Create TextView
//                            TextView time = new TextView(getActivity());
//                            time.setText(data_list.get(i).getTime());
//                            time.setSingleLine(true);
//                            time.setTextColor(getResources().getColor(R.color.channels_unselecetd_texcolor));
//                            ll.addView(time);*/
//
//
//                            View childItem = m_inflater.inflate(R.layout.item_channel_desc_item, null);
//
//                            //DynamicImageView imageView = (DynamicImageView) childItem.findViewById(R.id.imageShowThumbnail);
//                            TextView txtChannelName = (TextView) childItem.findViewById(R.id.txtChannelName);
//                            TextView txtChannel_time = (TextView) childItem.findViewById(R.id.txtChannel_time);
//                            //imageView.setVisibility(View.GONE);
//
//                            long nDeltaTime = data_list.get(i).getUnix_time() - nPrevStartTime;
//                            if (i == 0) nDeltaTime = 0;
//                            if (nDeltaTime > 0) {
//                                View view = m_inflater.inflate(R.layout.item_channel_desc_item, null);
//                                TextView txtChannelName1 = (TextView) view.findViewById(R.id.txtChannelName);
//                                TextView txtChannel_time1 = (TextView) view.findViewById(R.id.txtChannel_time);
//                                txtChannelName1.setText("");
//                                txtChannel_time1.setText("");
//                                view.setBackgroundColor(Color.parseColor("#3e99f2"));
//                                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                int nWidth = (int) nDeltaTime;
//                                layoutParams.width = nWidth;
//                                Log.d("TiTle///::::", ":::" + "///");
//                                Log.d("Width///::::", ":::" + nWidth);
//                                view.setLayoutParams(layoutParams);
//                                linear.addView(view);
//                            }
//                            nTempWidth += nDeltaTime;
//
//
//
//
//
//                            String strText = data_list.get(i).getTitle();
//                            String strTextTime = data_list.get(i).getTime();
//                            txtChannelName.setText(strText);
//                            txtChannel_time.setText("Start Time: " + strTextTime);
//                            String youtuve_video_id = data_list.get(i).getUrl();
//                            childItem.setTag(youtuve_video_id);
//
//
//                            childItem.setBackgroundColor(Color.parseColor("#01395E"));
//                            final VideoBean jObj = data_list.get(i);
//                            final int nPos = i;
//
//                            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                            int nWidth = 100;
//                            if ((data_list.get(i).getDuration()) < 100) {
//                                nWidth = 130;
//                            }  else
//                                nWidth = (int) (data_list.get(i).getDuration() + 30);
//                            nTempWidth += nWidth;
//                            if (i == data_list.size() - 1) {
//                                nWidth += nMaxWidth - nTempWidth;
//                            }
//                            //Log.d("TiTle::::", ":::" + strText);
//                            Log.d("Width::::", ":::" + nWidth);
//
//                            layoutParams.width = nWidth + 400;
//                            childItem.setLayoutParams(layoutParams);
//
//                            linear.addView(childItem);
//                            nPrevStartTime = data_list.get(i).getUnix_time() + data_list.get(i).getDuration();
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    row.setTag(k);
//                    if (k == 0) firstRow = row;
//                    linearScrollChannelDesc.addView(row);
//
//                }
//            }
//
//        }
//    }
//
//    private void makeChannelTitleList(final ArrayList<ChannelScheduler> scheduler_list) {
//        LayoutInflater m_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        linearScrollChannelTitle.removeAllViews();
//        if(scheduler_list.size()>0){
//            for (int k = 0; k < scheduler_list.size(); k++) {
//                View row = m_inflater.inflate(R.layout.item_channel_grid, null);
//                LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_grid);
//                DynamicImageView imageView = (DynamicImageView) row.findViewById(R.id.imageShowThumbnail);
//                TextView txtChannelName = (TextView) row.findViewById(R.id.txtChannelName);
//
//                imageView.loadImage(scheduler_list.get(k).getBig_logo_url());
//                txtChannelName.setText(scheduler_list.get(k).getName());
//                if (k == 0)
//                    linear.setBackgroundColor(Color.parseColor("#0D76BC"));
//                else
//                    linear.setBackgroundColor(Color.parseColor("#004A7C"));
//
//                final int position = k;
//                row.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    loadChannelViews(scheduler_list);
//                                }
//                            });
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                linearScrollChannelTitle.addView(row);
//            }
//        }
//    }
//
//    private class ChannelCategoryAdapter extends RecyclerView.Adapter<ChannelCategoryAdapter.DataObjectHolder> {
//        ArrayList<ChannelCategoryList> m_channelViews;
//        Context context;
//        int mSelectedItem;
//
//        public ChannelCategoryAdapter(ArrayList<ChannelCategoryList> m_channelViews, Context context) {
//            this.m_channelViews = m_channelViews;
//            this.context = context;
//        }
//
//
//        @Override
//        public ChannelCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
//            return new DataObjectHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ChannelCategoryAdapter.DataObjectHolder holder, final int position) {
//            holder.fragment_ondemandlist_items.setText(m_channelViews.get(position).getName());
//
//
//            if (position == mMainCategorySelectedItem) {
//                Log.d("drawable", "position  " + mMainCategorySelectedItem);
//                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
//            } else {
//                holder.fragment_ondemandlist_items.setBackgroundResource(0);
//            }
//
//
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return m_channelViews.size();
//        }
//
//        public class DataObjectHolder extends RecyclerView.ViewHolder {
//            LinearLayout fragment_ondemandlist_items_layout;
//            TextView fragment_ondemandlist_items;
//
//            public DataObjectHolder(View itemView) {
//                super(itemView);
//
//                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
//                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
//            }
//        }
//    }
//
//    private void showloaderView(){
//        if (mListener != null) {
//            mListener.displayToolbar(true);
//        }
//        linearFullScreen.setVisibility(View.VISIBLE);
//        layout_all_channelview.setVisibility(View.GONE);
//        layout_channelviewby_list.setVisibility(View.GONE);
//    }
//
//    private void showBrowseAllView(){
//        if (mListener != null) {
//            mListener.displayToolbar(false);
//        }
//        linearFullScreen.setVisibility(View.GONE);
//        layout_all_channelview.setVisibility(View.VISIBLE);
//        layout_channelviewby_list.setVisibility(View.GONE);
//    }
//
//    private void showChannelsView(){
//        if (mListener != null) {
//            mListener.displayToolbar(false);
//        }
//        linearFullScreen.setVisibility(View.GONE);
//        layout_all_channelview.setVisibility(View.GONE);
//        layout_channelviewby_list.setVisibility(View.VISIBLE);
//    }
//
//
//
//
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnChannelFragmentInteractionListener) {
//            mListener = (OnChannelFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//    }
//
//    @Override
//    public void onPrevious() {
//
//    }
//
//    @Override
//    public void onNext() {
//
//    }
//
//    @Override
//    public void onPlaylistEnded() {
//
//    }
//
//
//    public interface OnChannelFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//        void displayToolbar(boolean b);
//    }
//}
