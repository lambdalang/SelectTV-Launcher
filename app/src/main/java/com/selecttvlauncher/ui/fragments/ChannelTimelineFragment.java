//package com.selecttvlauncher.ui.fragments;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.Window;
//import android.widget.FrameLayout;
//import android.widget.HorizontalScrollView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.analytics.Tracker;
//import com.selecttvlauncher.Adapter.CustomGridLayoutManager;
//import com.selecttvlauncher.BeanClass.ChannelCategoryBean;
//import com.selecttvlauncher.BeanClass.ChannelCategoryList;
//import com.selecttvlauncher.BeanClass.ChannelListBean;
//import com.selecttvlauncher.BeanClass.ChannelScheduler;
//import com.selecttvlauncher.BeanClass.VideoBean;
//import com.selecttvlauncher.LauncherApplication;
//import com.selecttvlauncher.R;
//import com.selecttvlauncher.network.JSONRPCAPI;
//import com.selecttvlauncher.tools.Utilities;
//import com.selecttvlauncher.ui.activities.HomeActivity;
//import com.selecttvlauncher.ui.dialogs.OnDemandDialog;
//import com.selecttvlauncher.ui.dialogs.ProgressHUD;
//import com.selecttvlauncher.ui.views.CustomScrollView;
//import com.selecttvlauncher.ui.views.DynamicImageView;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//
//public class ChannelTimelineFragment extends Fragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private static final String ARG_PARAM3 = "param3";
//
//    private String mParam1;
//    private String mParam2;
//    private String mParam3;
//    private RecyclerView fragment_channel_alllist,fragment_channel_program_list,channel_show_alllist;
//    private CustomScrollView horizontal_scroll;
//    public int nTimeLimit = 1200;
//    public int nPaginationTimeLimit = 0;
//    public ArrayList<ChannelScheduler> m_channelSchedule = new ArrayList<>();
//    private ArrayList<ChannelScheduler> all_scheduler=new ArrayList<>();
//    long nMinStartTime = 0;
//    long nPrevStartTime=0;
//    public int nTimeOffset = 0;
//    int nTempWidth=0;
//    private List<String> currentVideoList = new ArrayList<>();
//
//    ListenSubList listenSubListt;
//    ListenMainList  mainList;
//    boolean canPaginate=true;
//    String paginate_ids="";
//    String browseall_ids="";
//    private RecyclerView firstView,secondview;
//    private OnDemandDialog demanddialog;
//    private String playingurl="";
//    private ImageView imgchannelupdown;
//    private int width;
//
//
//    TextView channel_tab_textview, show_tab_textview,browse_textView,allchannel_textView,horizontal_listview_item_count;
//    LinearLayout linearShowTab, linearChannelTab,scroll_arrow_layout,layoutchannel,dynamic_horizontalViews_layout,recycler_layout,image_view;
//    RelativeLayout layout_all_channelview;
//    RecyclerView horizontal_channel_list;
//    ImageView left_slide,right_slide,horizontal_listview_channel_close;
//    ProgressBar horizontal_channel_list_progressBar;
//    HorizontalScrollView horizontal_listview;
//    ImageView imgscrollprev,imgscrollnext;
//
//    private Typeface OpenSans_Bold;
//    private Typeface OpenSans_Regular;
//    private Typeface OpenSans_Semibold;
//    private Typeface osl_ttf;
//
//
//    private TimelineFragmentListener mListener;
//    private ScrollView overall_scroll;
//    private FrameLayout frameMenu;
//    private boolean channelSelected = false;
//    private int horizontal_position = 0;
//    int mMainCategorySelectedItem = 0;
//    private ArrayList<ChannelCategoryBean> m_channelViews = new ArrayList<>();
//    private LayoutInflater inflate;
//    Boolean aBoolean = true;
//    private View previousselected;
//
//    Dialog dialog;
//    private String szSplashTitle="";
//    private Tracker mTracker;
//    private String selectedChannelName="";
//
//    public ChannelTimelineFragment() {
//    }
//
//
//
//    public static ChannelTimelineFragment newInstance(String param1, String param2, String param3) {
//        ChannelTimelineFragment fragment = new ChannelTimelineFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        args.putString(ARG_PARAM3, param3);
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
//            mParam3 = getArguments().getString(ARG_PARAM3);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_channel_timeline, container, false);
//        ((HomeActivity)getActivity()).setmChannelTimelineFragment(this);
//        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        text_font_typeface();
//
//        mTracker = LauncherApplication.getInstance().getDefaultTracker();
//
//        fragment_channel_alllist        =(RecyclerView)view.findViewById(R.id.fragment_channel_alllist);
//        fragment_channel_program_list   = (RecyclerView)view.findViewById(R.id.fragment_channel_program_list);
//        channel_show_alllist            = (RecyclerView)view.findViewById(R.id.channel_show_alllist);
//        horizontal_scroll               = (CustomScrollView) view.findViewById(R.id.horizontal_scroll);
//
//        imgchannelupdown                =   (ImageView)view.findViewById(R.id.imgchannelupdown);
//        imgscrollprev                =   (ImageView)view.findViewById(R.id.imgscrollprev);
//        imgscrollnext =   (ImageView)view.findViewById(R.id.imgscrollnext);
//        channel_tab_textview            = (TextView) view.findViewById(R.id.channel_tab_textview);
//        show_tab_textview = (TextView) view.findViewById(R.id.show_tab_textview);
//
//        browse_textView = (TextView) view.findViewById(R.id.browse_textView);
//
//        linearShowTab = (LinearLayout) view.findViewById(R.id.linearShowTab);
//        linearChannelTab = (LinearLayout) view.findViewById(R.id.linearChannelTab);
//
//        overall_scroll = (ScrollView) view.findViewById(R.id.overall_scroll);
//        scroll_arrow_layout = (LinearLayout) view.findViewById(R.id.scroll_arrow_layout);
//        frameMenu = (FrameLayout) view.findViewById(R.id.frameMenu);
//        layoutchannel=(LinearLayout)view.findViewById(R.id.layoutchannel);
//
//        dialog = new Dialog(getActivity(), R.style.MY_DIALOG);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.browse_channel_dialog);
//        dialog.setCancelable(false);
//
//
//
//        dynamic_horizontalViews_layout = (LinearLayout) dialog.findViewById(R.id.dynamic_horizontalViews_layout);
//        layout_all_channelview = (RelativeLayout) dialog.findViewById(R.id.layout_all_channelview);
//        allchannel_textView = (TextView) view.findViewById(R.id.allchannel_textView);
//        horizontal_listview_item_count = (TextView) dialog.findViewById(R.id.horizontal_listview_item_count);
//        recycler_layout = (LinearLayout) dialog.findViewById(R.id.recycler_layout);
//        image_view = (LinearLayout) dialog.findViewById(R.id.image_view);
//        left_slide = (ImageView) dialog.findViewById(R.id.left_slide);
//        right_slide = (ImageView) dialog.findViewById(R.id.right_slide);
//        horizontal_listview_channel_close = (ImageView) dialog.findViewById(R.id.horizontal_listview_channel_close);
//        horizontal_channel_list = (RecyclerView) dialog.findViewById(R.id.horizontal_channel_list);
//        horizontal_channel_list_progressBar = (ProgressBar) dialog.findViewById(R.id.horizontal_channel_list_progressBar);
//        horizontal_listview = (HorizontalScrollView) dialog.findViewById(R.id.horizontal_listview);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
//        horizontal_channel_list.setLayoutManager(gridLayoutManager);
//
//
//
//        CustomGridLayoutManager linearLayoutManager = new CustomGridLayoutManager (getActivity());
//        fragment_channel_alllist.setLayoutManager(linearLayoutManager);
//        fragment_channel_alllist.setHasFixedSize(true);
//
//        CustomGridLayoutManager  linearLayoutManager1 = new CustomGridLayoutManager (getActivity());
//        fragment_channel_program_list.setLayoutManager(linearLayoutManager1);
//        fragment_channel_program_list.setHasFixedSize(true);
//
//        fragment_channel_alllist.setNestedScrollingEnabled(false);
//        fragment_channel_program_list.setNestedScrollingEnabled(false);
//
//        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
//        channel_show_alllist.setLayoutManager(lm);
//
//        linearShowTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickShowTab();
//            }
//        });
//        linearChannelTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickChannelTab();
//            }
//        });
//
//        imgchannelupdown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mListener!=null)
//                mListener.minimizeTimelineView();
//
//            }
//        });
//        layoutchannel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                channelSelected = false;
//                image_view.removeAllViews();
//                horizontal_position=mMainCategorySelectedItem;
//                addhorizontalScrolllayouts(dynamic_horizontalViews_layout, HomeActivity.channelMainCategoryNew,horizontal_position);
//                dialog.show();
//            }
//        });
//        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
//            if(mParam3.equalsIgnoreCase("search")){
//                new LoadingSearchChannelTask(true).execute();
//            }else{
//                if(mParam1.equalsIgnoreCase(""+ HomeActivity.channelID)){
//                    new LoadingChannelTask(true).execute();
//                }else{
//                    new LoadingChannelTask(false).execute();
//                }
//            }
//
//
//        }else{
//
//        }
//
//
//
//
//
//        horizontal_scroll.setHorizontalScrollListener(new CustomScrollView.ScrollListener() {
//            @Override
//            public void onScrolledRight() {
//                try {
//                    int scrollY = horizontal_scroll.getScrollY(); // For ScrollView
//                    int scrollX = horizontal_scroll.getScrollX(); // For HorizontalScrollView
//                    // DO SOMETHING WITH THE SCROLL COORDINATES
//
//                    // Log.d("scroll:::",":::::scroll"+scrollX+":::"+scrollY);
//                    if(firstView!=null){
//                        try {
//                            if(scrollX!=0){
//                                //  Log.d("view  xx:::",":::"+firstView.getX());
//                                //  Log.d("view child x:::",":::"+firstView.getChildAt(firstView.getChildCount()-1).getX());
//                                int lastView_pos=(int)firstView.getChildAt(firstView.getChildCount()-1).getX();
//                                if((lastView_pos-scrollX<=100&&canPaginate)){
//                                    canPaginate=false;
//                                    if(!mParam3.equalsIgnoreCase("search")){
//                                        new LoadingPaginationChannelTask(false).execute();
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        imgscrollnext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    horizontal_scroll.setScrollX(horizontal_scroll.getScrollX() + horizontal_scroll.getMeasuredWidth() / 2);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        imgscrollprev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    horizontal_scroll.setScrollX(horizontal_scroll.getScrollX() - horizontal_scroll.getMeasuredWidth());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof TimelineFragmentListener) {
//            mListener = (TimelineFragmentListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public void highlightview(String uri) {
//        try {
//            playingurl=uri;
//            if(firstView!=null){
//                for(int i=0;i<firstView.getChildCount();i++){
//                    final View v=firstView.getChildAt(i).findViewWithTag(uri);
//                    if(v!=null){
//                        Log.d(":::","::view exist::::");
//                        v.setBackgroundColor(Color.parseColor("#3e99f2"));
//                        if(previousselected!=null){
//                            previousselected.setBackgroundColor(0);
//                            previousselected=v;
//                        }
//                        horizontal_scroll.post(new Runnable() {
//
//                            public void run() {
//                                horizontal_scroll.scrollTo((int)v.getX(), 0);
//                            }
//                        });
//                    }else{
//                        Log.d(":::","::no view exist::::");
//                        firstView.getChildAt(i).setBackgroundColor(0);
//                    }
//                }
//
//            }
//
//            if(secondview!=null){
//                for(int i=0;i<secondview.getChildCount();i++){
//                    final View v=secondview.getChildAt(i);
//                    if(v!=null){
//                        secondview.getChildAt(i).setBackgroundColor(0);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void changearrowimage(boolean direction) {
//        try {
//            if(direction){
//                imgchannelupdown.setImageResource(R.drawable.ic_arrowup);
//            }else{
//                imgchannelupdown.setImageResource(R.drawable.ic_arrowdown);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public interface TimelineFragmentListener {
//        // TODO: Update argument type and name
//        void loadYoutubevideos(List<String> list, int offset);
//        void minimizeTimelineView();
//        void setVideoSchedule(ArrayList<VideoBean> v_list);
//        void setChannelSchedule(ArrayList<ChannelScheduler> scheduler_list);
//        void setChannellist();
//        void updateprogress(int progres, String progresstext, String message);
//        void showcontent();
//        void refereshcategoryList(int cID);
//    }
//
//    public class ListenMainList extends RecyclerView.Adapter<ListenMainList.DataObjectHolder> {
//        Context context;
//        int mlistPosition = 0;
//        ArrayList<ChannelScheduler> list_data;
//
//        public ListenMainList(ArrayList<ChannelScheduler> list_data, Context context) {
//            this.context = context;
//            this.list_data=list_data;
//        }
//
//        @Override
//        public ListenMainList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            //LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_grid, parent, false);
//            return new DataObjectHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ListenMainList.DataObjectHolder holder, final int position) {
//            holder.fragment_ondemandlist_items.setText(list_data.get(position).getName());
//            try {
//                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                holder.imageView.loadImage(list_data.get(position).getBig_logo_url());
//                if (position== 0)
//                    holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//                else
//                    holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_light_grey));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    swaplistpositions(position);
//
//                    /*int oldindex=position;
//                    ChannelScheduler cs=list_data.get(oldindex);
//                    Collections.swap(list_data, oldindex, 0);
//                    notifyDataSetChanged();
//                    listenSubListt.swapItems(oldindex);*/
//
//
//
//                }
//            });
//
//            holder.fragment_ondemandlist_items_layout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    try {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            holder.fragment_ondemandlist_items_layout.setAlpha(0.5f);
//
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            holder.fragment_ondemandlist_items_layout.setAlpha(1f);
//                        } else {
//                            holder.fragment_ondemandlist_items_layout.setAlpha(1f);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                    return false;
//                }
//            });
//
//        }
//
//        public void swapItems(int itemAIndex) {
//            //make sure to check if dataset is null and if itemA and itemB are valid indexes.
//
//            try {
//                ChannelScheduler itemA = list_data.get(itemAIndex);
//                list_data.remove(itemAIndex);
//                list_data.add(0,itemA);
//
//                notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        public void setData(ArrayList<ChannelScheduler> data) {
//            //make sure to check if dataset is null and if itemA and itemB are valid indexes.
//            try {
//                this.list_data=data;
//                notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        @Override
//        public int getItemCount() {
//            return list_data.size();
//        }
//
//        public class DataObjectHolder extends RecyclerView.ViewHolder {
//            LinearLayout fragment_ondemandlist_items_layout;
//            TextView fragment_ondemandlist_items;
//            DynamicImageView imageView;
//
//            public DataObjectHolder(View itemView) {
//                super(itemView);
//                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.item_grid);
//                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
//                imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
//            }
//        }
//    }
//
//    private void swaplistpositions(int position) {
//        try {
//            ChannelScheduler cs=m_channelSchedule.get(position);
//            m_channelSchedule.remove(position);
//            m_channelSchedule.add(0,cs);
//            //mainList.setData(m_channelSchedule);
//            listenSubListt.setDataset(m_channelSchedule);
//            if(m_channelSchedule.get(0).getVideolist().size()>0){
//                currentVideoList.clear();
//                for(int i=0;i<m_channelSchedule.get(0).getVideolist().size();i++){
//                    currentVideoList.add(m_channelSchedule.get(0).getVideolist().get(i).getUrl());
//                }
//                int offset=getTimeOffset();
//                if(mListener!=null){
//                    mListener.setVideoSchedule(m_channelSchedule.get(0).getVideolist());
//                    mListener.loadYoutubevideos(currentVideoList,offset);
//                }
//            }
//            new LoadingListStremTask().execute(""+m_channelSchedule.get(0).getId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            mainList.setData(m_channelSchedule);
//        }
//    }
//
//    public void swaplistpositionsByID(int cID) {
//        try {
//            for(int i=0;i<m_channelSchedule.size();i++){
//                if(cID==m_channelSchedule.get(i).getId()){
//                    ChannelScheduler cs=m_channelSchedule.get(i);
//                    m_channelSchedule.remove(i);
//                    m_channelSchedule.add(0,cs);
//                    //mainList.setData(m_channelSchedule);
//                    listenSubListt.setDataset(m_channelSchedule);
//                    if(m_channelSchedule.get(0).getVideolist().size()>0){
//                        currentVideoList.clear();
//                        for(int j=0;j<m_channelSchedule.get(0).getVideolist().size();j++){
//                            currentVideoList.add(m_channelSchedule.get(0).getVideolist().get(j).getUrl());
//                        }
//                        int offset=getTimeOffset();
//                        if(mListener!=null){
//                            mListener.setVideoSchedule(m_channelSchedule.get(0).getVideolist());
//                            mListener.loadYoutubevideos(currentVideoList,offset);
//                        }
//                    }
//                    new LoadingListStremTask().execute(""+m_channelSchedule.get(0).getId());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            mainList.setData(m_channelSchedule);
//        }
//    }
//
//    public class ListenMainListSub extends RecyclerView.Adapter<ListenMainListSub.DataObjectHolder> {
//        Context context;
//        int mlistPosition = 0;
//        ArrayList<VideoBean> video_list;
//
//        public ListenMainListSub(ArrayList<VideoBean> video_list,Context context) {
//            this.context = context;
//            this.video_list=video_list;
//        }
//
//        @Override
//        public ListenMainListSub.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            //LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_desc_item, parent, false);
//            return new DataObjectHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ListenMainListSub.DataObjectHolder holder, final int position) {
//            holder.fragment_ondemandlist_items.setText(video_list.get(position).getTitle());
//            String strTextTime = video_list.get(position).getTime();
//            holder.txtChannel_time.setText("Start Time: " + strTextTime);
//            holder.item_grid.setTag(video_list.get(position).getUrl());
//            holder.itemView.setTag(video_list.get(position).getUrl());
//            try {
//                holder.txtChannel_time.setTypeface(OpenSans_Regular);
//                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            //holder.itemView.setBackgroundColor(Color.parseColor("#3e99f2"));
//
//
//            try {
//                long nDeltaTime = video_list.get(position).getUnix_time()- nPrevStartTime;
//                if (position== 0) nDeltaTime = 0;
//                if (nDeltaTime > 0) {
//                    //holder.item_grid.setBackgroundColor(Color.parseColor("#3e99f2"));
//                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    int nWidth = (int) nDeltaTime;
//                    layoutParams.width = nWidth;
//                    Log.d("TiTle///::::", ":::" + "///");
//                    Log.d("Width///::::", ":::" + nWidth);
//                    holder.item_grid.setLayoutParams(layoutParams);
//                }
//                nTempWidth += nDeltaTime;
//
//
//                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                int nWidth = 100;
//                if (video_list.get(position).getDuration() < 100) {
//                    nWidth = 130;
//                }
//                nWidth = (int) (video_list.get(position).getDuration() + 30);
//                nTempWidth += nWidth;
//
//                layoutParams1.width = nWidth + 400;
//                holder.item_grid.setLayoutParams(layoutParams1);
//                nPrevStartTime = video_list.get(position).getUnix_time()+ video_list.get(position).getDuration();
//
//                holder.item_grid.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            int tag_pos=position;
//                            String strUrl = null;
//                            String strTitle = null;
//                            try {
//                                strTitle = video_list.get(tag_pos).getTitle();
//                                strUrl = video_list.get(tag_pos).getUrl();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            if (!TextUtils.isEmpty(strUrl)) {
//                                if (demanddialog != null && demanddialog.isShowing()) {
//                                    demanddialog.dismiss();
//                                }
//
//                                demanddialog = new OnDemandDialog(getActivity());
//                                demanddialog.setInfo(strTitle, strUrl, tag_pos);
//                                demanddialog.setActivity(getActivity());
//                                demanddialog.show();
//                            } else {
//                                Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                            try {
//                                Utilities.setAnalytics(mTracker,"Channels-"+selectedChannelName+"-"+video_list.get(tag_pos).getTitle());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                if(video_list.get(position).getUrl().equalsIgnoreCase(playingurl)&&position==0){
//                    holder.item_grid.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
//                    if(previousselected!=null){
//                        previousselected.setBackgroundColor(0);
//                        previousselected=holder.item_grid;
//                    }
//                }else{
//                    holder.item_grid.setBackgroundColor(0);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return video_list.size();
//        }
//
//        public class DataObjectHolder extends RecyclerView.ViewHolder {
//            LinearLayout fragment_ondemandlist_items_layout;
//            TextView fragment_ondemandlist_items,txtChannel_time;
//            RelativeLayout item_grid;
//
//            public DataObjectHolder(View itemView) {
//                super(itemView);
//                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
//                txtChannel_time = (TextView) itemView.findViewById(R.id.txtChannel_time);
//                item_grid = (RelativeLayout) itemView.findViewById(R.id.item_grid);
//
//            }
//        }
//    }
//
//
//    public class ListenSubList extends RecyclerView.Adapter<ListenSubList.DataObjectHolder> {
//        Context context;
//        int mlistPosition = 0;
//        ArrayList<ChannelScheduler> data_list;
//
//
//        public ListenSubList(ArrayList<ChannelScheduler> data_list, Context context) {
//            this.context = context;
//            this.data_list=data_list;
//        }
//
//        @Override
//        public ListenSubList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//           // LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlayout, parent, false);
//            return new DataObjectHolder(view);
//        }
//        public void addDta(int position, ArrayList<VideoBean> data) {
//            data_list.get(position).getVideolist().addAll(data);
//            Log.d(":: data_list.","::::"+ data_list.get(position).getVideolist());
//            notifyDataSetChanged();
//        }
//        public ChannelScheduler getItem(int position) {
//            return data_list.get(position);
//        }
//        public void swapItems(int itemAIndex) {
//            int oldindex=itemAIndex;
//            Collections.swap(data_list, oldindex, 0);
//            //notifyItemMoved(oldindex, 0);
//        }
//        public void setDataset(ArrayList<ChannelScheduler> dataset) {
//            this.data_list = dataset;
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public void onBindViewHolder(final ListenSubList.DataObjectHolder holder, final int position) {
//
//            final CustomGridLayoutManager layoutManager5
//                    = new CustomGridLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//            layoutManager5.setHoriScrollEnabled(false);
//            holder.fragment_channel_program_list_item.setLayoutManager(layoutManager5);
//            holder.fragment_channel_program_list_item.setHasFixedSize(false);
//
//            holder.fragment_channel_program_list_item.setNestedScrollingEnabled(false);
//            nPrevStartTime = nMinStartTime;
//            nTempWidth = 0;
//            try {
//                if(data_list.get(position).getVideolist()!=null&&data_list.get(position).getVideolist().size()>0){
//                    ListenMainListSub listenSubList3 = new ListenMainListSub(data_list.get(position).getVideolist(),getActivity());
//                    holder.fragment_channel_program_list_item.setAdapter(listenSubList3);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            try {
//                holder.fragment_channel_program_list_item.setTag(data_list.get(position).getId());
//                //holder.fragment_channel_program_list_item.invalidate();
//                holder.list_layout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        swapItems(position);
//                        Log.d(":::","::swap"+position);
//                    }
//                });
//
//
//                if(position==0){
//                    firstView=holder.fragment_channel_program_list_item;
//                }
//                if(position==1){
//                    secondview=holder.fragment_channel_program_list_item;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return data_list.size();
//        }
//
//        public class DataObjectHolder extends RecyclerView.ViewHolder {
//            RecyclerView fragment_channel_program_list_item;
//            TextView fragment_ondemandlist_items;
//            LinearLayout list_layout;
//
//            public DataObjectHolder(View itemView) {
//                super(itemView);
//                fragment_channel_program_list_item = (RecyclerView) itemView.findViewById(R.id.fragment_channel_program_list_item);
//                list_layout=(LinearLayout)itemView.findViewById(R.id.list_layout);
//
//            }
//        }
//    }
//
//    private class LoadingChannelTask extends AsyncTask<Object, Integer, Object> {
//
//        private boolean showDialog;
//        ProgressDialog dia;
//        private ArrayList<VideoBean> stream_list=new ArrayList<>();
//
//
//        public LoadingChannelTask() {
//        }
//
//        public LoadingChannelTask(boolean showDialog) {
//            this.showDialog = showDialog;
//        }
//
//        ProgressHUD mmProgressHUD;
//        JSONArray m_jsonLiveItems;
//        ArrayList<ChannelListBean> channel_name_list;
//
//        @Override
//        protected void onPreExecute() {
//            try {
//                //mmProgressHUD = new ProgressHUD(getActivity());
//                paginate_ids="";
//                nPaginationTimeLimit=0;
//                currentVideoList.clear();
//                szSplashTitle="Loading channels data....";
//                if(!showDialog){
//                    mmProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
//                }
//                channel_name_list=new ArrayList<>();
//                m_channelSchedule.clear();
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//
//            try {
//                if(mParam2.equalsIgnoreCase("")){
//                    m_jsonLiveItems = JSONRPCAPI.getChannels(Integer.parseInt(mParam1));
//
//                }else{
//                    m_jsonLiveItems = JSONRPCAPI.getChannelsByDecade(mParam2,Integer.parseInt(mParam1));
//                }
//                if(showDialog){
//                    for (int i = 51; i < 100; i++) {
//                        try {
//                            Thread.sleep(50);
//                            publishProgress(i);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//
//                if (m_jsonLiveItems == null) return null;
//                Log.d("getChannels::", ":::::" + m_jsonLiveItems.toString());
//                String IDs = "";
//                if(m_jsonLiveItems.length()>0){
//                    for(int i=0;i<m_jsonLiveItems.length();i++){
//                        JSONObject chan_obj=m_jsonLiveItems.getJSONObject(i);
//                        int chan_id=chan_obj.getInt("id");
//                        String chan_big_logo_url=chan_obj.getString("big_logo_url");
//                        String chan_description=chan_obj.getString("description");
//                        String chan_logo_url=chan_obj.getString("logo_url");
//                        String chan_name=chan_obj.getString("name");
//
//                        if (IDs.length() == 0)
//                            IDs = chan_id + "";
//                        else
//                            IDs = IDs + "," + chan_id;
//
//                        channel_name_list.add(new ChannelListBean( chan_big_logo_url, chan_description, chan_logo_url, chan_name, chan_id));
//                    }
//                }
//                paginate_ids=IDs;
//
//                JSONArray stream_array= null;
//                try {
//                    stream_array = JSONRPCAPI.getStreams(channel_name_list.get(0).getId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);
//                if (objSchedule == null) return null;
//                Log.d("getScheduller2::", ":::" + objSchedule.toString());
//
//                if (objSchedule != null) {
//                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
//                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
//                        IDs = obj.getInt("id") + "";
//                        ChannelScheduler model = new ChannelScheduler();
//                        model.setId(obj.getInt("id"));
//                        model.setName(obj.getString("name"));
//                        model.setBig_logo_url(obj.getString("big_logo_url"));
//
//                        JSONArray video_array = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
//                        ArrayList<VideoBean> video_list=new ArrayList<>();
//                        if(video_array.length()>0){
//                            for(int j=0;j<video_array.length();j++){
//                                JSONObject video_object=video_array.getJSONObject(j);
//                                VideoBean channel_data=new VideoBean();
//                                channel_data.setSeq(video_object.getInt("seq"));
//                                channel_data.setTitle(video_object.getString("title"));
//                                channel_data.setUrl(video_object.getString("url"));
//                                channel_data.setUnix_time(video_object.getLong("unix_time"));
//                                channel_data.setTime(video_object.getString("time"));
//                                channel_data.setDuration(video_object.getLong("duration"));
//                                channel_data.setId(video_object.getInt("id"));
//                                video_list.add(channel_data);
//                                if(i==0){
//                                    currentVideoList.add(video_object.getString("url"));
//                                }
//                            }
//                            model.setVideolist(video_list);
//                        }
//
//
//                        m_channelSchedule.add(model);
//                    }
//                } else {
//                    m_channelSchedule.clear();
//                }
//                if(stream_array!=null&&stream_array.length()>0){
//                    for(int l=0;l<stream_array.length();l++){
//                        JSONObject stream_object=stream_array.getJSONObject(l);
//                        VideoBean channel_data=new VideoBean();
//                        channel_data.setSeq(stream_object.getInt("seq"));
//                        channel_data.setTitle(stream_object.getString("title"));
//                        channel_data.setUrl(stream_object.getString("url"));
//                        channel_data.setUnix_time(stream_object.getLong("unix_time"));
//                        channel_data.setTime(stream_object.getString("time"));
//                        channel_data.setDuration(stream_object.getLong("duration"));
//                        channel_data.setId(stream_object.getInt("id"));
//                        stream_list.add(channel_data);
//                    }
//                }
//
//                makeChannelList();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            try {
//
//
//                mainList = new ListenMainList(m_channelSchedule,getActivity());
//                fragment_channel_alllist.setAdapter(mainList );
//
//                listenSubListt = new ListenSubList(m_channelSchedule,getActivity());
//                fragment_channel_program_list.setAdapter(listenSubListt);
//                if(mListener!=null){
//                    mListener.setChannelSchedule(m_channelSchedule);
//                    mListener.setChannellist();
//                }
//
//                if(currentVideoList.size()>0){
//                    int offset=getTimeOffset();
//                    if(mListener!=null){
//                        mListener.setVideoSchedule(m_channelSchedule.get(0).getVideolist());
//                        mListener.loadYoutubevideos(currentVideoList,offset);
//                    }
//                }
//
//                if(stream_list.size()>0){
//                    StreamAdapter listAdapter=new StreamAdapter(stream_list,getActivity());
//                    channel_show_alllist.setAdapter(listAdapter);
//                }
//                try {
//                    szSplashTitle="Loading Views...";
//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    new Thread(runnable).start();
//
//                    try {
//                        selectedChannelName=m_channelSchedule.get(0).getName();
//                        Utilities.setAnalytics(mTracker,"Channels-"+selectedChannelName);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //progressLoad.setVisibility(View.GONE);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            } finally {
//                try {
//                    if(mListener!=null){
//                        if (!showDialog ){
//                            mmProgressHUD.dismiss();
//                        }else{
//                            mListener.showcontent();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            try {
//                if (values == null) return;
//                int fPos = values[0];
//                if(mListener!=null)
//                mListener.updateprogress(values[0],(int) fPos + "%",szSplashTitle);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    private void makeChannelList() {
//
//        try {
//            int maxCount = 0;
//            if (m_channelSchedule.size() == 0) return;
//            long nMaxEndTime = 0;
//            nMinStartTime = 0;
//            for (int i = 0; i < m_channelSchedule.size(); i++) {
//                ChannelScheduler item = m_channelSchedule.get(i);
//                try {
//                    VideoBean objStart = item.getVideolist().get(0);
//                    VideoBean objEnd = item.getVideolist().get(item.getVideolist().size() - 1);
//
//                    if (nMinStartTime == 0)
//                        nMinStartTime = objStart.getUnix_time();
//                    if (objStart.getUnix_time()< nMinStartTime)
//                        nMinStartTime = objStart.getUnix_time();
//
//                    if (nMaxEndTime < (objEnd.getUnix_time() + objEnd.getDuration()))
//                        nMaxEndTime = (objEnd.getUnix_time() + objEnd.getDuration());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private class LoadingPaginationChannelTask extends AsyncTask<Object, Object, Object> {
//
//        private boolean showDialog;
//        ProgressDialog dia;
//
//
//        public LoadingPaginationChannelTask () {
//        }
//
//        public LoadingPaginationChannelTask (boolean showDialog) {
//            this.showDialog = showDialog;
//        }
//
//        ProgressHUD mmProgressHUD;
//        ArrayList<ChannelScheduler> new_list;
//
//        @Override
//        protected void onPreExecute() {
//            try {
//                //mmProgressHUD = new ProgressHUD(getActivity());
//                new_list=new ArrayList<>();
//                nPaginationTimeLimit+=nTimeLimit;
//                nPaginationTimeLimit++;
//
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//
//            try {
//
//                JSONObject objSchedule = JSONRPCAPI.getScheduller(paginate_ids, nPaginationTimeLimit, nTimeLimit);
//                if (objSchedule == null) return null;
//                Log.d("getScheduller2::", ":::" + objSchedule.toString());
//                new_list.clear();
//
//                if (objSchedule != null) {
//                    for (int i = 0; i < m_channelSchedule.size(); i++) {
//
//                        ChannelScheduler new_sch=new ChannelScheduler();
//                        new_sch.setId(m_channelSchedule.get(i).getId());
//                        new_sch.setName(m_channelSchedule.get(i).getName());
//                        new_sch.setBig_logo_url(m_channelSchedule.get(i).getBig_logo_url());
//                        new_sch.setDescription(m_channelSchedule.get(i).getDescription());
//                        new_sch.setLogo_url(m_channelSchedule.get(i).getLogo_url());
//
//
//                        JSONArray video_array = null;
//                        try {
//                            video_array = objSchedule.getJSONObject(""+m_channelSchedule.get(i).getId()).getJSONArray("videos").getJSONArray(0);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        ArrayList<VideoBean> video_list=new ArrayList<>();
//                        if(video_array!=null&&video_array.length()>0){
//                            for(int j=0;j<video_array.length();j++){
//                                JSONObject video_object=video_array.getJSONObject(j);
//                                VideoBean channel_data=new VideoBean();
//                                channel_data.setSeq(video_object.getInt("seq"));
//                                channel_data.setTitle(video_object.getString("title"));
//                                channel_data.setUrl(video_object.getString("url"));
//                                channel_data.setUnix_time(video_object.getLong("unix_time"));
//                                channel_data.setTime(video_object.getString("time"));
//                                channel_data.setDuration(video_object.getLong("duration"));
//                                channel_data.setId(video_object.getInt("id"));
//                                m_channelSchedule.get(i).getVideolist().add(channel_data);
//                                video_list.add(channel_data);
//                            }
//                            new_sch.setVideolist(video_list);
//                            Log.d(":::video_list"+m_channelSchedule.get(i).getId(),":::"+video_list.size());
//
//                        }
//                        new_list.add(new_sch);
//                    }
//                }
//                // makeChannelList();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            try {
//                Log.d("::::","listenSubListt::"+listenSubListt.getItemCount());
//                /*for(int i=0;i<listenSubListt.getItemCount();i++){
//
//                    for(int j=0;j<new_list.size();j++){
//                        if(listenSubListt.getItem(i).getId()==new_list.get(j).getId()){
//                            listenSubListt.addDta(i,new_list.get(j).getVideolist());
//                            Log.d("listenSubListt:::"+listenSubListt.getItem(i).getId(),":::added::"+new_list.get(j).getVideolist().size());
//                            //
//
//                        }
//                    }
//                }*/
//                listenSubListt.setDataset(m_channelSchedule);
//                //fragment_channel_program_list.getAdapter().notifyDataSetChanged();
//                //fragment_channel_program_list.invalidate();
//
//
//                //progressLoad.setVisibility(View.GONE);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            } finally {
//                try {
//                    canPaginate=true;
//                    if (showDialog ){
//                        mmProgressHUD.dismiss();
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private int getTimeOffset() {
//        if (m_channelSchedule==null) return 0;
//
//        try {
//            VideoBean firstObj = null;
//            long unixTime = 0;
//            long curTime = 0;
//            int timeOffset = 0;
//            try {
//
//                firstObj = m_channelSchedule.get(0).getVideolist().get(0);
//                unixTime = firstObj.getUnix_time();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            curTime = System.currentTimeMillis() / 1000;
//
//            long t1 = System.currentTimeMillis() / 1000;
//            Date dt = new Date();
//            long t2 = dt.getTime() / 1000;
//            int h = dt.getHours();
//            int m = dt.getMinutes();
//            int s = dt.getSeconds();
//            long t3 = Calendar.getInstance().getTimeInMillis() / 1000;
//
//            Log.e("TIMEOFFSET", String.format("unix_Time = %d, curTime = %d", unixTime, curTime));
//            timeOffset = (int) (curTime - unixTime);
//
//            if (timeOffset > 0)
//                return timeOffset;
//            else
//                return 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.DataObjectHolder> {
//        Context context;
//        int mlistPosition = 0;
//        ArrayList<VideoBean> list_data;
//
//        public StreamAdapter(ArrayList<VideoBean> list_data, Context context) {
//            this.context = context;
//            this.list_data=list_data;
//        }
//
//        @Override
//        public StreamAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            //LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_row_channel_details, parent, false);
//            return new DataObjectHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final StreamAdapter.DataObjectHolder holder, final int position) {
//
//            try {
//                holder.txtVideoName.setText(list_data.get(position).getTitle());
//                holder.txtStartTime.setText(list_data.get(position).getTime());
//                String strThumbUrl = "http://img.youtube.com/vi/" + list_data.get(position).getUrl()+ "/default.jpg";
//                holder.imgVideoThumbnail.loadImage(strThumbUrl);
//
//
//                holder.item_layout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String strTitle = list_data.get(position).getTitle();
//                        String  strUrl = list_data.get(position).getUrl();
//                        if (!TextUtils.isEmpty(strUrl)) {
//                            if (demanddialog != null && demanddialog.isShowing()) {
//                                demanddialog.dismiss();
//                            }
//
//                            demanddialog = new OnDemandDialog(getActivity());
//                            demanddialog.setInfo(strTitle, strUrl, position);
//                            demanddialog.setActivity(getActivity());
//                            demanddialog.show();
//                        } else {
//                            Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return list_data.size();
//        }
//
//        public class DataObjectHolder extends RecyclerView.ViewHolder {
//            public TextView txtVideoName;
//            public TextView txtStartTime;
//            public DynamicImageView imgVideoThumbnail;
//            RelativeLayout item_layout;
//
//            public DataObjectHolder(View itemView) {
//                super(itemView);
//                txtVideoName = (TextView) itemView.findViewById(R.id.ui_video_row_txt_tittle);
//                txtStartTime = (TextView) itemView.findViewById(R.id.ui_video_row_txt_time);
//                imgVideoThumbnail = (DynamicImageView) itemView.findViewById(R.id.ui_video_row_image);
//                item_layout=(RelativeLayout)itemView.findViewById(R.id.item_layout);
//            }
//        }
//    }
//
//    public void onClickShowTab() {
//        scroll_arrow_layout.setVisibility(View.GONE);
//        frameMenu.setVisibility(View.VISIBLE);
//        overall_scroll.setVisibility(View.GONE);
//
//        linearShowTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_light));
//        linearChannelTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_dark));
//
//        show_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        channel_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//
//    }
//
//    public void onClickChannelTab() {
//        scroll_arrow_layout.setVisibility(View.VISIBLE);
//        frameMenu.setVisibility(View.GONE);
//        overall_scroll.setVisibility(View.VISIBLE);
//
//        linearChannelTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_light));
//        linearShowTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_dark));
//        channel_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        show_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//    }
//
//    private void addhorizontalScrolllayouts(final LinearLayout dynamic_horizontalViews_layout, final ArrayList<ChannelCategoryList> listData, final int position) {
//
//
//
//        horizontal_listview_channel_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//
//        left_slide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                horizontal_listview.
//                        post(new Runnable() {
//                            public void run() {
//                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() - ((width / 5) * 4), horizontal_listview.getScrollY());
//
//                            }
//                        });
//
//            }
//        });
//
//
//        right_slide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                horizontal_listview.
//                        post(new Runnable() {
//                            public void run() {
//                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() + ((width / 5) * 4), horizontal_listview.getScrollY());
//
//
//                            }
//                        });
//
//            }
//        });
//
//        Utilities.setViewFocus(getActivity(), horizontal_listview_channel_close);
//        Utilities.setViewFocus(getActivity(), left_slide);
//        Utilities.setViewFocus(getActivity(), right_slide);
//
//
//        horizontal_listview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//
//                try {
//                    Log.d("scrollvalue", "++" + horizontal_listview.getScrollX());
//                    View view = horizontal_listview.getChildAt(horizontal_listview.getChildCount() - 1);
//                    // Calculate the scrolldiff
//                    int diff = (view.getRight() - (horizontal_listview.getWidth() + horizontal_listview.getScrollX()));
//                    // if diff is zero, then the bottom has been reached
//                    if (diff == 0) {
//                        left_slide.setImageResource(R.drawable.prev_active);
//                        right_slide.setImageResource(R.drawable.next_inactive);
//                        // notify that we have reached the bottom
//                        Log.d("ScrollTest.LOG_TAG", "MyScrollView: Bottom has been reached");
//                    } else if (horizontal_listview.getScrollX() == 0) {
//                        left_slide.setImageResource(R.drawable.prev_inactive);
//                        right_slide.setImageResource(R.drawable.next_active);
//                    } else {
//                        left_slide.setImageResource(R.drawable.prev_active);
//                        right_slide.setImageResource(R.drawable.next_active);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        int spanCount = listData.size(); // 3 columns
//
//        boolean includeEdge = false;
//        int layoutwidth = recycler_layout.getLayoutParams().width;
//        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);
//
//        try {
//            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    width = recycler_layout.getMeasuredWidth();
//
//
//
//                    Log.d("layoutwidth::", "width" + width);
//
//                    for (int l = 0; l < listData.size(); l++) {
//                        final int l1=l;
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_channel_list, null);
//                                final TextView horizontal_imageView = (TextView) itemmlayout.findViewById(R.id.horizontal_imageView);
//                                final LinearLayout ll = (LinearLayout) itemmlayout.findViewById(R.id.ll);
//
//                                horizontal_imageView.setText(listData.get(l1).getName());
//                                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams((width / 5), LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                                itemmlayout.setTag(l1);
//
//                                if (l1 != 0) {
//                                    vp.setMargins(0, 0, 0, 0);
//                                }
//
//
//                                if(mParam1.equalsIgnoreCase(""+ HomeActivity.channelID)){
//                                    if (l1 == 0) {
//
//                                        horizontal_imageView.setBackgroundResource(R.drawable.btn_favourite);
//                                        int x, y;
//                                        x = horizontal_imageView.getLeft();
//                                        y = horizontal_imageView.getTop();
//
//
//                                    }
//                                }else{
//                                    for(int k=0;k<listData.get(l1).getSubCategories().size();k++){
//                                        if (listData.get(l1).getSubCategories().get(k).getId() == Integer.parseInt(mParam1)) {
//
//                                            horizontal_imageView.setBackgroundResource(R.drawable.btn_favourite);
//                                            int x, y;
//                                            x = horizontal_imageView.getLeft();
//                                            y = horizontal_imageView.getTop();
//
//
//                                        }
//                                    }
//
//                                }
//
//
//
//
//
//
//                                final int finalL = l1;
//                                itemmlayout.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        try {
//                                            String tag = itemmlayout.getTag().toString();
//
//                                            View lv = (View) dynamic_horizontalViews_layout.findViewWithTag(horizontal_position);
//
//                                            if (lv != null) {
//                                                final TextView horizontal_imageViewww = (TextView) lv.findViewById(R.id.horizontal_imageView);
//                                                horizontal_imageViewww.setBackgroundResource(0);
//                                            }
//
//                                            horizontal_imageView.setBackgroundResource(R.drawable.btn_favourite);
//                                            horizontal_position = Integer.parseInt(tag);
////                                horizontal_position = tag;
//                                            horizontal_listview_item_count.setVisibility(View.INVISIBLE);
//                                            horizontal_channel_list.setVisibility(View.INVISIBLE);
//                                            horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
//                                            aBoolean = false;
//                                           /* loadallChannel(listData.get(finalL).getId());*/
//                                            new LoadingAllChannelTask().execute(""+listData.get(finalL).getId());
//                                            if(mListener!=null)
//                                            mListener.refereshcategoryList(listData.get(finalL).getId());
//
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//
//                                    }
//                                });
//
//                                Utilities.setViewFocus(getActivity(), itemmlayout);
//
//
//                                horizontal_imageView.setLayoutParams(vp);
//
//                                image_view.addView(itemmlayout);
//                            }
//                        });
//
//
//                    }
//
//                    aBoolean = false;
//                    horizontal_listview_item_count.setVisibility(View.INVISIBLE);
//                    horizontal_channel_list.setVisibility(View.INVISIBLE);
//                    horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
//
//                    //loadallChannel(listData.get(horizontal_position).getId());
//
//
//                    if(mParam1.equalsIgnoreCase(""+ HomeActivity.channelID)){
//                        if(listData.size()>0){
//                            new LoadingAllChannelTask().execute(""+listData.get(0).getId());
//                        }
//
//                    }else{
//                        new LoadingAllChannelTask().execute(""+Integer.parseInt(mParam1));
//                    }
//
//
//                    horizontal_listview.postDelayed(new Runnable() {
//                        public void run() {
////                            horizontal_listview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//                            horizontal_listview.scrollTo(horizontal_listview.getScrollX() + ((width / 5) * (position)), horizontal_listview.getScrollY());
//
//                        }
//                    }, 100L);
//
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private class LoadingAllChannelTask extends AsyncTask<String, Object, Object> {
//        private boolean showDialog;
//        int channelId;
//        String  channelName,
//                channelLogo;
//        JSONArray channelVideos;
//        String all_ids="";
//
//        public LoadingAllChannelTask() {
//        }
//
//        public LoadingAllChannelTask(boolean showDialog) {
//            this.showDialog = showDialog;
//        }
//
//        ProgressHUD mProgressHUD;
//
//        @Override
//        protected void onPreExecute() {
//            try {
//                browseall_ids="";
//                all_scheduler=new ArrayList<>();
//                /*if(showDialog) {
//                    mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
//                }*/
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        @Override
//        protected Object doInBackground(String... params) {
//
//            try {
//                JSONArray m_jsonLiveItems = JSONRPCAPI.getChannels(Integer.parseInt(params[0]));
//
//                if (m_jsonLiveItems == null) return null;
//                Log.d("getChannels::", ":::::" + m_jsonLiveItems.toString());
//                String IDs = "";
//                if(m_jsonLiveItems.length()>0){
//                    for(int i=0;i<m_jsonLiveItems.length();i++){
//                        JSONObject chan_obj=m_jsonLiveItems.getJSONObject(i);
//                        int chan_id=chan_obj.getInt("id");
//                        String chan_big_logo_url=chan_obj.getString("big_logo_url");
//                        String chan_description=chan_obj.getString("description");
//                        String chan_logo_url=chan_obj.getString("logo_url");
//                        String chan_name=chan_obj.getString("name");
//
//                        if (IDs.length() == 0)
//                            IDs = chan_id + "";
//                        else
//                            IDs = IDs + "," + chan_id;
//
//
//                    }
//                }
//                browseall_ids=IDs;
//
//                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);
//                if (objSchedule == null) return null;
//                Log.d("getScheduller2::", ":::" + objSchedule.toString());
//
//                if (objSchedule != null) {
//                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
//                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
//                        IDs = obj.getInt("id") + "";
//                        ChannelScheduler model = new ChannelScheduler();
//                        model.setId(obj.getInt("id"));
//                        model.setName(obj.getString("name"));
//                        model.setBig_logo_url(obj.getString("big_logo_url"));
//
//                        JSONArray video_array = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
//                        ArrayList<VideoBean> video_list=new ArrayList<>();
//                        if(video_array.length()>0){
//                            for(int j=0;j<video_array.length();j++){
//                                JSONObject video_object=video_array.getJSONObject(j);
//                                VideoBean channel_data=new VideoBean();
//                                channel_data.setSeq(video_object.getInt("seq"));
//                                channel_data.setTitle(video_object.getString("title"));
//                                channel_data.setUrl(video_object.getString("url"));
//                                channel_data.setUnix_time(video_object.getLong("unix_time"));
//                                channel_data.setTime(video_object.getString("time"));
//                                channel_data.setDuration(video_object.getLong("duration"));
//                                channel_data.setId(video_object.getInt("id"));
//                                video_list.add(channel_data);
//                            }
//                            model.setVideolist(video_list);
//                        }
//
//
//                        all_scheduler.add(model);
//                    }
//                } else {
//                    all_scheduler.clear();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//
//            try {
//                horizontal_channel_list.setVisibility(View.VISIBLE);
//                horizontal_channel_list_progressBar.setVisibility(View.GONE);
//
//                if(all_scheduler!=null&&all_scheduler.size()!=0)
//                {
//                    AllChannelAdapter allChannelAdapter = new AllChannelAdapter(all_scheduler, getActivity());
//                    horizontal_channel_list.setAdapter(allChannelAdapter);
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//
//        }
//    }
//
//    public class AllChannelAdapter extends RecyclerView.Adapter<AllChannelAdapter.DataObjectHolder> {
//        ArrayList<ChannelScheduler> channelModels;
//        Context context;
//
//        public AllChannelAdapter(ArrayList<ChannelScheduler> channelModels, Context context) {
//            this.channelModels = channelModels;
//            this.context = context;
//        }
//
//
//
//        @Override
//        public AllChannelAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            //LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_allchannel_grid, parent, false);
//            return new DataObjectHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(AllChannelAdapter.DataObjectHolder holder, final int position) {
//            try {
//                Log.d("cha_images",channelModels.get(position).getBig_logo_url());
//                Log.d("cha_images", channelModels.get(position).getName());
////            holder.imageView.loadImage(channelModels.get(position).getChannelLogo());
//                holder.txtChannelName.setText(channelModels.get(position).getName());
//                horizontal_listview_item_count.setVisibility(View.VISIBLE);
//                horizontal_listview_item_count.setText(m_channelSchedule.size() + " " + "Channels found");
//
//                Picasso.with(getContext()
//                        .getApplicationContext())
//                        .load(channelModels.get(position).getBig_logo_url())
//                        .fit().centerInside()
//                        .placeholder(R.drawable.loader_show).into(holder.imageView);
//
//                holder.item_grid.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        try {
//                            channelSelected = true;
//                            paginate_ids=browseall_ids;
//                            m_channelSchedule.clear();
//                            m_channelSchedule.addAll(all_scheduler);
//                            for(int i=0;i<m_channelSchedule.size();i++){
//                                if(channelModels.get(position).getId()==m_channelSchedule.get(i).getId()){
//                                    ChannelScheduler cs=m_channelSchedule.get(position);
//                                    m_channelSchedule.remove(position);
//                                    m_channelSchedule.add(0,cs);
//                                }
//                            }
//                            new LoadingListStremTask().execute(""+m_channelSchedule.get(0).getId());
//                            mainList = new ListenMainList(m_channelSchedule,getActivity());
//                            fragment_channel_alllist.setAdapter(mainList );
//                            listenSubListt = new ListenSubList(m_channelSchedule,getActivity());
//                            fragment_channel_program_list.setAdapter(listenSubListt);
//                            currentVideoList.clear();
//                            for(int i=0;i<m_channelSchedule.get(0).getVideolist().size();i++){
//                                currentVideoList.add(m_channelSchedule.get(0).getVideolist().get(i).getUrl());
//
//                            }
//                            if(currentVideoList.size()>0){
//                                int offset=getTimeOffset();
//                                if(mListener!=null){
//                                    mListener.setVideoSchedule(m_channelSchedule.get(0).getVideolist());
//                                    mListener.loadYoutubevideos(currentVideoList,offset);
//                                }
//
//                            }
//
//                            try {
//                                selectedChannelName=m_channelSchedule.get(0).getName();
//                                Utilities.setAnalytics(mTracker,"Channels-"+selectedChannelName);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return channelModels.size();
//        }
//
//        public class DataObjectHolder extends RecyclerView.ViewHolder {
//            DynamicImageView imageView;
//            TextView txtChannelName;
//            LinearLayout item_grid;
//
//            public DataObjectHolder(View itemView) {
//                super(itemView);
//                item_grid = (LinearLayout) itemView.findViewById(R.id.item_grid);
//                imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
//                txtChannelName = (TextView) itemView.findViewById(R.id.txtChannelName);
//
//                Utilities.setViewFocus(getActivity(), item_grid);
//            }
//        }
//    }
//
//    private class LoadingListStremTask extends AsyncTask<String, Object, Object> {
//
//        private boolean showDialog;
//        ProgressDialog dia;
//        private ArrayList<VideoBean> stream_list=new ArrayList<>();
//
//
//        public LoadingListStremTask() {
//        }
//
//        public LoadingListStremTask(boolean showDialog) {
//            this.showDialog = showDialog;
//        }
//
//        ProgressHUD mmProgressHUD;
//        ArrayList<ChannelListBean> channel_name_list;
//
//        @Override
//        protected void onPreExecute() {
//            try {
//                //mmProgressHUD = new ProgressHUD(getActivity());
//                showDialog=true;
//
//
//                mmProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        @Override
//        protected Object doInBackground(String... params) {
//
//            try {
//
//                JSONArray stream_array=JSONRPCAPI.getStreams(Integer.parseInt(params[0]));
//
//                if(stream_array!=null&&stream_array.length()>0){
//                    for(int l=0;l<stream_array.length();l++){
//                        JSONObject stream_object=stream_array.getJSONObject(l);
//                        VideoBean channel_data=new VideoBean();
//                        channel_data.setSeq(stream_object.getInt("seq"));
//                        channel_data.setTitle(stream_object.getString("title"));
//                        channel_data.setUrl(stream_object.getString("url"));
//                        channel_data.setUnix_time(stream_object.getLong("unix_time"));
//                        channel_data.setTime(stream_object.getString("time"));
//                        channel_data.setDuration(stream_object.getLong("duration"));
//                        channel_data.setId(stream_object.getInt("id"));
//                        stream_list.add(channel_data);
//                    }
//                }
//
//                makeChannelList();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            try {
//
//                if(stream_list.size()>0){
//                    StreamAdapter listAdapter=new StreamAdapter(stream_list,getActivity());
//                    channel_show_alllist.setAdapter(listAdapter);
//                }
//                //progressLoad.setVisibility(View.GONE);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            } finally {
//                try {
//                    if(mmProgressHUD.isShowing()){
//                        if (showDialog ){
//                            mmProgressHUD.dismiss();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    private class LoadingSearchChannelTask extends AsyncTask<Object, Integer, Object> {
//
//        private boolean showDialog;
//        ProgressDialog dia;
//        private ArrayList<VideoBean> stream_list=new ArrayList<>();
//
//
//        public LoadingSearchChannelTask() {
//        }
//
//        public LoadingSearchChannelTask(boolean showDialog) {
//            this.showDialog = showDialog;
//        }
//
//        ProgressHUD mmProgressHUD;
//        JSONArray m_jsonLiveItems;
//        ArrayList<ChannelListBean> channel_name_list;
//
//        @Override
//        protected void onPreExecute() {
//            try {
//                //mmProgressHUD = new ProgressHUD(getActivity());
//                paginate_ids="";
//                nPaginationTimeLimit=0;
//                currentVideoList.clear();
//                szSplashTitle="Loading channels data....";
//                if(!showDialog){
//                    mmProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
//                }
//                channel_name_list=new ArrayList<>();
//                m_channelSchedule.clear();
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        @Override
//        protected Object doInBackground(Object... params) {
//
//            try {
//                JSONObject m_jsonSearchLiveItems = JSONRPCAPI.getschedullerRelated(Integer.parseInt(mParam1));
//
//
//                if(showDialog){
//                    for (int i = 51; i < 100; i++) {
//                        try {
//                            Thread.sleep(50);
//                            publishProgress(i);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//
//                if (m_jsonSearchLiveItems == null) return null;
//                Log.d("getChannels::", ":::::" + m_jsonSearchLiveItems.toString());
//                String IDs = "";
//
//                try {
//                    if (m_jsonSearchLiveItems.has("data")) {
//                        JSONArray mSearch_Trailors = m_jsonSearchLiveItems.getJSONArray("data");
//                        for (int i = 0; i < mSearch_Trailors.length(); i++) {
//                            JSONObject dat_object = mSearch_Trailors.getJSONObject(i);
//
//                            int chan_id=dat_object.getInt("id");
//                            String chan_big_logo_url=dat_object.getString("image");
//                            String chan_description="";
//                            String chan_logo_url=dat_object.getString("image");
//                            String chan_name=dat_object.getString("name");
//
//                            if (IDs.length() == 0)
//                                IDs = chan_id + "";
//                            else
//                                IDs = IDs + "," + chan_id;
//
//                            JSONArray v_arry=dat_object.getJSONArray("videos");
//                            ArrayList<VideoBean> video_list=new ArrayList<>();
//                            if(v_arry.length()>0){
//                                for(int m=0;m<v_arry.length();m++){
//                                    JSONObject video_object=v_arry.getJSONObject(m);
//                                    VideoBean channel_data=new VideoBean();
//                                    channel_data.setSeq(video_object.getInt("seq"));
//                                    channel_data.setTitle(video_object.getString("title"));
//                                    channel_data.setUrl(video_object.getString("url"));
//                                    channel_data.setUnix_time(video_object.getLong("unix_time"));
//                                    channel_data.setTime(video_object.getString("time"));
//                                    channel_data.setDuration(video_object.getLong("duration"));
//                                    channel_data.setId(video_object.getInt("id"));
//                                    video_list.add(channel_data);
//                                    if(i==0){
//                                        currentVideoList.add(video_object.getString("url"));
//                                    }
//                                }
//                            }
//
//                            channel_name_list.add(new ChannelListBean( chan_big_logo_url, chan_description, chan_logo_url, chan_name, chan_id));
//
//                            ChannelScheduler model = new ChannelScheduler();
//                            model.setId(chan_id);
//                            model.setName(chan_name);
//                            model.setBig_logo_url(chan_big_logo_url);
//                            model.setVideolist(video_list);
//
//                            m_channelSchedule.add(model);
//
//                            if(chan_id==Integer.parseInt(mParam1)){
//                                ChannelScheduler c1=m_channelSchedule.get(i);
//                                m_channelSchedule.remove(i);
//                                m_channelSchedule.add(0,c1);
//                            }
//
//
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                paginate_ids=IDs;
//
//                JSONArray stream_array= null;
//                try {
//                    stream_array = JSONRPCAPI.getStreams(channel_name_list.get(0).getId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//
//                if(stream_array!=null&&stream_array.length()>0){
//                    for(int l=0;l<stream_array.length();l++){
//                        JSONObject stream_object=stream_array.getJSONObject(l);
//                        VideoBean channel_data=new VideoBean();
//                        channel_data.setSeq(stream_object.getInt("seq"));
//                        channel_data.setTitle(stream_object.getString("title"));
//                        channel_data.setUrl(stream_object.getString("url"));
//                        channel_data.setUnix_time(stream_object.getLong("unix_time"));
//                        channel_data.setTime(stream_object.getString("time"));
//                        channel_data.setDuration(stream_object.getLong("duration"));
//                        channel_data.setId(stream_object.getInt("id"));
//                        stream_list.add(channel_data);
//                    }
//                }
//
//                makeChannelList();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            try {
//
//                mainList = new ListenMainList(m_channelSchedule,getActivity());
//                fragment_channel_alllist.setAdapter(mainList );
//
//                listenSubListt = new ListenSubList(m_channelSchedule,getActivity());
//                fragment_channel_program_list.setAdapter(listenSubListt);
//                if(mListener!=null){
//                    mListener.setChannelSchedule(m_channelSchedule);
//                    mListener.setChannellist();
//                }
//
//
//                if(currentVideoList.size()>0){
//                    int offset=getTimeOffset();
//                    if(mListener!=null){
//                        mListener.setVideoSchedule(m_channelSchedule.get(0).getVideolist());
//                        mListener.loadYoutubevideos(currentVideoList,offset);
//                    }
//
//                }
//
//                if(stream_list.size()>0){
//                    StreamAdapter listAdapter=new StreamAdapter(stream_list,getActivity());
//                    channel_show_alllist.setAdapter(listAdapter);
//                }
//                try {
//                    szSplashTitle="Loading Views...";
//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//
//                                Thread.sleep(500);
//
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    new Thread(runnable).start();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //progressLoad.setVisibility(View.GONE);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            } finally {
//                try {
//                    if (!showDialog ){
//                        mmProgressHUD.dismiss();
//                    }else{
//                        if(mListener!=null)
//                            mListener.showcontent();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            try {
//                if (values == null) return;
//                int fPos = values[0];
//                if(mListener!=null)
//                    mListener.updateprogress(values[0],(int) fPos + "%",szSplashTitle);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void text_font_typeface() {
//        try {
//            OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
//            OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
//            OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
//            osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
