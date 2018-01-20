package com.selecttvlauncher.channels;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.Adapter.CustomGridLayoutManager;
import com.selecttvlauncher.BeanClass.ChannelCategoryBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.helper.Image;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.dialogs.OnDemandDialog;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.views.DynamicImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ChannelTimelineFragment extends Fragment implements YoutubeDataListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private String mParam2;
    private String mParam3;
    private RecyclerView fragment_channel_alllist, fragment_channel_program_list, channel_show_alllist, horizontal_channel_category_list;
    //    private CustomScrollView horizontal_scroll;
    public int nTimeLimit = 1200;
    public int nPaginationTimeLimit = 0;
    public ArrayList<ChannelScheduler> m_channelSchedule = new ArrayList<>();
    private ArrayList<ChannelScheduler> all_scheduler = new ArrayList<>();
    long nMinStartTime = 0;
    long nPrevStartTime = 0;
    public int nTimeOffset = 0;
    int nTempWidth = 0;
    private List<String> currentVideoList = new ArrayList<>();

    Activity activity;
    AdapterListenSubList listenSubListt;
    ListenMainList mainList;
    boolean canPaginate = true;
    String paginate_ids = "";
    String browseall_ids = "";
    private RecyclerView firstView, secondview;
    private OnDemandDialog demanddialog;
    private String playingurl = "";
    private ImageView imgchannelupdown;
    private int width;
    private static int numberOfMoves = 0;

    TextView channel_tab_textview, show_tab_textview, browse_textView, allchannel_textView, horizontal_listview_item_count;
    LinearLayout linearShowTab, linearChannelTab, scroll_arrow_layout, layoutchannel, dynamic_horizontalViews_layout, recycler_layout, image_view;
    RelativeLayout layout_all_channelview;
    RecyclerView horizontal_channel_list;
    ImageView left_slide, right_slide, horizontal_listview_channel_close;
    ProgressBar horizontal_channel_list_progressBar;
    HorizontalScrollView horizontal_listview;
    ImageView imgscrollprev, imgscrollnext;

    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;


    private TimelineFragmentListener mListener;
    //    private CustomNestedScrollView overall_scroll;
    private NestedScrollView overall_scroll;
    private FrameLayout frameMenu, layoutProgramListContainer;
    private boolean channelSelected = false;
    private int horizontal_position = 0;
    int mMainCategorySelectedItem = 0;
    private ArrayList<ChannelCategoryBean> m_channelViews = new ArrayList<>();
    private LayoutInflater inflate;
    Boolean aBoolean = true;
    private View previousselected;
    private View ruler_line;
    private int markedRulerLine = 0;

    Dialog dialog;
    private String szSplashTitle = "";
    private Tracker mTracker;
    private String selectedChannelName = "";
    private int nWidth = 0;
    private int nHeight = 0;

    public ChannelTimelineFragment() {
    }


    public static ChannelTimelineFragment newInstance(String param1, String param2, String param3) {
        ChannelTimelineFragment fragment = new ChannelTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        nWidth = displayMetrics.widthPixels;
        nHeight = displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_timeline, container, false);
        ((HomeActivity) getActivity()).setmChannelTimelineFragment(this);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        text_font_typeface();

        mTracker = LauncherApplication.getInstance().getDefaultTracker();

        fragment_channel_alllist = (RecyclerView) view.findViewById(R.id.fragment_channel_alllist);
//        fragment_channel_alllist.getLayoutParams().width = (nWidth / 100) * 30;
        fragment_channel_program_list = (RecyclerView) view.findViewById(R.id.fragment_channel_program_list);
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fragment_channel_program_list.getLayoutParams();
//        layoutParams.setMargins((nWidth / 100) * 30, 0, 0, 0);
//        fragment_channel_program_list.setLayoutParams(layoutParams);
        channel_show_alllist = (RecyclerView) view.findViewById(R.id.channel_show_alllist);
//        horizontal_scroll = (CustomScrollView) view.findViewById(R.id.horizontal_scroll);

        imgchannelupdown = (ImageView) view.findViewById(R.id.imgchannelupdown);
        imgscrollprev = (ImageView) view.findViewById(R.id.imgscrollprev);
        imgscrollnext = (ImageView) view.findViewById(R.id.imgscrollnext);
        channel_tab_textview = (TextView) view.findViewById(R.id.channel_tab_textview);
        show_tab_textview = (TextView) view.findViewById(R.id.show_tab_textview);

        browse_textView = (TextView) view.findViewById(R.id.browse_textView);

        linearShowTab = (LinearLayout) view.findViewById(R.id.linearShowTab);
        linearChannelTab = (LinearLayout) view.findViewById(R.id.linearChannelTab);

        overall_scroll = (NestedScrollView) view.findViewById(R.id.overall_scroll);
        scroll_arrow_layout = (LinearLayout) view.findViewById(R.id.scroll_arrow_layout);
        frameMenu = (FrameLayout) view.findViewById(R.id.frameMenu);
        layoutProgramListContainer = (FrameLayout) view.findViewById(R.id.layoutProgramListContainer);
        layoutchannel = (LinearLayout) view.findViewById(R.id.layoutchannel);
        ruler_line = view.findViewById(R.id.ruler_line);
        ruler_line.post(new Runnable() {
            @Override
            public void run() {
//                int rulerLeft = ruler_line.getLeft() + fragment_channel_program_list.getLeft();
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ruler_line.getLayoutParams();
//                layoutParams.setMargins(rulerLeft, 0, 0, 0);
//                ruler_line.setLayoutParams(layoutParams);
                markedRulerLine = ruler_line.getLeft();
            }
        });
        dialog = new Dialog(activity, R.style.MY_DIALOG);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.browse_channel_dialog);
        dialog.setCancelable(false);


        horizontal_channel_category_list = (RecyclerView) dialog.findViewById(R.id.horizontal_channel_category_list);
        LinearLayoutManager layMan = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        horizontal_channel_category_list.setLayoutManager(layMan);
        dynamic_horizontalViews_layout = (LinearLayout) dialog.findViewById(R.id.dynamic_horizontalViews_layout);
        layout_all_channelview = (RelativeLayout) dialog.findViewById(R.id.layout_all_channelview);
        allchannel_textView = (TextView) view.findViewById(R.id.allchannel_textView);
        horizontal_listview_item_count = (TextView) dialog.findViewById(R.id.horizontal_listview_item_count);
        recycler_layout = (LinearLayout) dialog.findViewById(R.id.recycler_layout);
        image_view = (LinearLayout) dialog.findViewById(R.id.image_view);
        left_slide = (ImageView) dialog.findViewById(R.id.left_slide);
        right_slide = (ImageView) dialog.findViewById(R.id.right_slide);
        horizontal_listview_channel_close = (ImageView) dialog.findViewById(R.id.horizontal_listview_channel_close);
        horizontal_channel_list = (RecyclerView) dialog.findViewById(R.id.horizontal_channel_list);
        horizontal_channel_list_progressBar = (ProgressBar) dialog.findViewById(R.id.horizontal_channel_list_progressBar);
        horizontal_listview = (HorizontalScrollView) dialog.findViewById(R.id.horizontal_listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        horizontal_channel_list.setLayoutManager(gridLayoutManager);


        CustomGridLayoutManager linearLayoutManager = new CustomGridLayoutManager(getActivity());
        linearLayoutManager.setHoriScrollEnabled(false);
        linearLayoutManager.setScrollEnabled(false);
        fragment_channel_alllist.setLayoutManager(linearLayoutManager);
        fragment_channel_alllist.setHasFixedSize(true);

        CustomGridLayoutManager linearLayoutManager1 = new CustomGridLayoutManager(getActivity());
        linearLayoutManager.setHoriScrollEnabled(false);
        linearLayoutManager1.setScrollEnabled(false);
        fragment_channel_program_list.setLayoutManager(linearLayoutManager1);
        fragment_channel_program_list.setHasFixedSize(true);

        fragment_channel_alllist.setNestedScrollingEnabled(false);
        fragment_channel_program_list.setNestedScrollingEnabled(false);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        channel_show_alllist.setLayoutManager(lm);

        linearShowTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShowTab();
            }
        });
        linearChannelTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChannelTab();
            }
        });

        imgchannelupdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.minimizeTimelineView();

            }
        });
        layoutchannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelSelected = false;
                image_view.removeAllViews();
                horizontal_position = mMainCategorySelectedItem;
                addhorizontalScrolllayouts(dynamic_horizontalViews_layout, HomeActivity.channelMainCategoryNew, horizontal_position);
                dialog.show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
            if (mParam3.equalsIgnoreCase("search")) {
                loadChannelList(mParam1, false, true);
            } else {
                if (mParam1.equalsIgnoreCase("" + HomeActivity.channelID)) {
                    loadChannelList(mParam1, false, false);
                } else {
                    loadChannelList(mParam1, false, false);
                }
            }
        }

//        overall_scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                if (overall_scroll != null) {
//                    View view = (View) overall_scroll.getChildAt(0);
////                    View view = (View) overall_scroll.getChildAt(overall_scroll.getChildCount() - 1);
//                    int diff = (view.getBottom() - (overall_scroll.getHeight() + overall_scroll.getScrollY()));
//                    if (diff == 0) {
//                        Log.e("::::::::", "MyScrollView: Bottom has been reached");
//                        if (mainList != null)
//                            mainList.updateChannelList();
//                        if (listenSubListt != null)
//                            listenSubListt.updateChannelList();
////                        if (handler != null)
////                            handler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                    if (mainList != null)
////                                        mainList.updateChannelList();
////                                    if (listenSubListt != null)
////                                        listenSubListt.updateChannelList();
////                                }
////                            });
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimelineFragmentListener) {
            mListener = (TimelineFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("fragment:::", "onDetach()");
        if (mListener != null) {
            mListener.stopFullscreenTimer();
            mListener.stopYoutubePlayer();
            mListener = null;
        }

        if (handler != null)
            handler.removeCallbacks(null);

        if (mainList != null) {
            mainList.list_data.clear();
            mainList = null;
        }
        if (listenSubListt != null) {
            listenSubListt.data_list.clear();
            listenSubListt.clearProgramListFragments();
            listenSubListt = null;
        }
    }


    public void changearrowimage(boolean direction) {
        try {
            if (direction) {
                imgchannelupdown.setImageResource(R.drawable.ic_arrowup);
            } else {
                imgchannelupdown.setImageResource(R.drawable.ic_arrowdown);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadYoutubeVideo(String url, String title, int offset) {

    }

    @Override
    public ArrayList<ProgramList> getProgramList(String mSlug) {
        final ChannelScheduler cs = VideoFilter.getSelectedChannel(m_channelSchedule, mSlug);
        return cs.getPrograms().getProgramlist();
    }


    public interface TimelineFragmentListener {
        // TODO: Update argument type and name
        void loadYoutubevideos(List<String> list, int offset);

        void minimizeTimelineView();

        void setVideoSchedule(ArrayList<ProgramList> v_list);

        void setChannelSchedule(ArrayList<ChannelScheduler> scheduler_list);

        void setChannellist();

        void updateprogress(int progres, String progresstext, String message);

        void showcontent();

        void refereshcategoryList(String cID);

        void setPlayVideo(ChannelScheduler channelScheduler);

        void stopYoutubePlayer();

        void stopFullscreenTimer();
    }

    public interface ChannelTimeLineListener {
        void setPlayVideo(ChannelScheduler channelScheduler);

        void loadStreamList(ArrayList<ProgramList> programLists);

        void showOnDemandDialog(String title, String url, int tagPos);

        void setAnalytics(String name);
    }

    ChannelTimeLineListener channelTimeLineListener = new ChannelTimeLineListener() {
        @Override
        public void setPlayVideo(ChannelScheduler channelScheduler) {
            mListener.setPlayVideo(channelScheduler);
        }

        @Override
        public void loadStreamList(ArrayList<ProgramList> programLists) {
            setStreamAdapter(programLists);
        }

        @Override
        public void showOnDemandDialog(String title, String url, int tagPos) {
            if (demanddialog != null && demanddialog.isShowing()) {
                demanddialog.dismiss();
            }

            demanddialog = new OnDemandDialog(getActivity());
            demanddialog.setInfo(title, url, tagPos);
            demanddialog.setActivity(getActivity());
            demanddialog.show();
        }

        @Override
        public void setAnalytics(String name) {
            try {
                Utilities.setAnalytics(mTracker, "Channels-" + selectedChannelName + "-" + name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private class ListenMainList extends RecyclerView.Adapter<ListenMainList.DataObjectHolder> {
        Context context;
        int mlistPosition = 0;
        int setChannelsCount = 0;
        public ArrayList<ChannelScheduler> list_data = new ArrayList<>();
        public ArrayList<ChannelScheduler> listAll = new ArrayList<>();
        int limit = 10;

        public ListenMainList(ArrayList<ChannelScheduler> list_data, Context context) {
            this.context = context;
            this.list_data = list_data;
//            this.listAll = list_data;
//            if (list_data.size() > limit) {
//                this.list_data.addAll(list_data.subList(0, limit));
//            } else {
//                this.list_data.addAll(list_data);
//            }
            setChannelsCount = this.list_data.size();
        }

        public void addList(ArrayList<ChannelScheduler> moreListItems) {
            this.list_data.addAll(moreListItems);
            this.notifyItemInserted(this.list_data.size());
            this.notifyDataSetChanged();
        }

        @Override
        public ListenMainList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_grid, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ListenMainList.DataObjectHolder holder, final int position) {
            if (position == 0)
                holder.itemView.setBackgroundColor(Color.parseColor("#3e99f2"));
            holder.fragment_ondemandlist_items.setText(list_data.get(position).getName());
            try {
                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Image.loadImage(getActivity(), list_data.get(position).getLogo(), holder.imageView);
                if (position == 0)
                    holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                else
                    holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swaplist(position);
                }
            });

            holder.fragment_ondemandlist_items_layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            holder.fragment_ondemandlist_items_layout.setAlpha(0.5f);

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            holder.fragment_ondemandlist_items_layout.setAlpha(1f);
                        } else {
                            holder.fragment_ondemandlist_items_layout.setAlpha(1f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return false;
                }
            });

        }

        public void swapItems(int itemAIndex) {
            //make sure to check if dataset is null and if itemA and itemB are valid indexes.

            try {
                ChannelScheduler itemA = list_data.get(itemAIndex);
                list_data.remove(itemAIndex);
                list_data.add(0, itemA);

                notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setData(ArrayList<ChannelScheduler> data) {
            //make sure to check if dataset is null and if itemA and itemB are valid indexes.
            try {
                this.list_data = data;
                notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.item_grid);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
            }
        }

        private void swaplist(int position) {
            try {
                Log.e("position", "::" + position);
                ArrayList<ChannelScheduler> tempList = new ArrayList<>();
                tempList.addAll(list_data);
                ChannelScheduler cs = tempList.get(position);
                tempList.remove(position);
                tempList.add(0, cs);
                ChannelTotalFragment.showBackIcon(true);
                setChannelsList(tempList);
                setPlayVideo(cs);
                overall_scroll.smoothScrollTo(0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }

        public void updateChannelList() {
            try {
                ArrayList<ChannelScheduler> filteredList = new ArrayList<>();
                if (listAll.size() > 0)
                    if (this.list_data.size() < listAll.size()) {
                        int offset = list_data.size() - 1;
                        filteredList.add(listAll.get(offset + 1));
                        addList(filteredList);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(null);
            handler = null;
        }
        if (mListener != null) {
            mListener.stopFullscreenTimer();
            mListener.stopYoutubePlayer();
            mListener = null;
        }

        if (mainList != null) {
            mainList.list_data.clear();
            mainList = null;
        }
        if (listenSubListt != null) {
            listenSubListt.data_list.clear();
            listenSubListt.clearProgramListFragments();
            listenSubListt = null;
        }
    }

    ProgressHUD mProgressHUD = null;
    private Handler handler = new Handler();

    private void setRecyclerviewChangingAnimator(RecyclerView recyclerView, boolean canAnimate) {
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(canAnimate); //if false animate will be not working
        }
    }


    private void loadChannelList(final String slug, final boolean isDialog, final boolean isfromSearch) {
        if (!isDialog) {
            mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
            if (mListener != null) {
                mListener.stopFullscreenTimer();
                mListener.stopYoutubePlayer();
            }
            if (handler != null)
                handler.removeCallbacks(null);

            if (mainList != null) {
                mainList.list_data.clear();
                mainList.notifyDataSetChanged();
                mainList = null;
            }
            if (listenSubListt != null) {
                listenSubListt.data_list.clear();
                listenSubListt.clearProgramListFragments();
                listenSubListt = null;
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LauncherApplication.getmWebService().loadChannelsDtaa(slug, isDialog, new ChannelApiListener() {
                    @Override
                    public void onChannelListLoaded(String categorySlug, final ArrayList<ChannelScheduler> channelList, final boolean isDialog) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (isDialog) {
                                        try {
                                            horizontal_channel_list.setVisibility(View.VISIBLE);
                                            horizontal_channel_list_progressBar.setVisibility(View.GONE);

                                            if (channelList != null && channelList.size() != 0) {
                                                AllChannelAdapter allChannelAdapter = new AllChannelAdapter(channelList, getActivity());
                                                horizontal_channel_list.setAdapter(allChannelAdapter);
                                            }
                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                    } else {
                                        setChannelsList(channelList);
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                } finally {
                                    stopProgressDialog(mProgressHUD);
                                    if (horizontal_channel_list_progressBar != null)
                                        horizontal_channel_list_progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onNetworkError() {
                        stopProgressDialog(mProgressHUD);
                        if (horizontal_channel_list_progressBar != null)
                            horizontal_channel_list_progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        thread.start();
    }

    private void stopProgressDialog(ProgressHUD mProgressHUD) {
        if (mProgressHUD != null)
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
    }

    private void setChannelsList(ArrayList<ChannelScheduler> channelList) {
        try {
            if (handler != null)
                handler.removeCallbacks(null);

            if (mainList != null) {
                mainList.list_data.clear();
                mainList.notifyDataSetChanged();
                mainList = null;
            }
            if (listenSubListt != null) {
                listenSubListt.data_list.clear();
                listenSubListt.clearProgramListFragments();
                listenSubListt = null;
            }

            if (m_channelSchedule != null)
                m_channelSchedule.clear();

            if (channelList.size() > 0) {
                m_channelSchedule = new ArrayList<ChannelScheduler>();
                m_channelSchedule = channelList;

                mainList = new ListenMainList(channelList, getActivity());
                fragment_channel_alllist.setAdapter(mainList);

                setRecyclerviewChangingAnimator(fragment_channel_program_list, false);
                listenSubListt = new AdapterListenSubList(channelList, getActivity(), markedRulerLine, channelTimeLineListener);
                listenSubListt.setFragmentManager(getChildFragmentManager());
                listenSubListt.setChannelListWidth(fragment_channel_alllist.getWidth());
                fragment_channel_program_list.setAdapter(listenSubListt);

                if (mListener != null) {
                    mListener.setChannelSchedule(channelList);
                    mListener.setChannellist();
                }

                if (currentVideoList.size() > 0) {
                    int offset = getTimeOffset();
                    if (mListener != null) {
                        mListener.setVideoSchedule(channelList.get(0).getPrograms().getProgramlist());
                        mListener.loadYoutubevideos(currentVideoList, offset);
                    }
                }
                try {
                    selectedChannelName = channelList.get(0).getName();
                    Utilities.setAnalytics(mTracker, "Channels-" + selectedChannelName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (
                Exception exception)

        {
            exception.printStackTrace();
        }
    }

    private void setPlayVideo(final ChannelScheduler channelScheduler) {
        mListener.setPlayVideo(channelScheduler);
    }


    private int getTimeOffset() {
        if (m_channelSchedule == null) return 0;

        try {
            ProgramList firstObj = null;
            long unixTime = 0;
            long curTime = 0;
            int timeOffset = 0;
            try {

                firstObj = m_channelSchedule.get(0).getPrograms().getProgramlist().get(0);
                unixTime = ChannelUtils.getDurationFromDate(firstObj.getStart_at());
            } catch (Exception e) {
                e.printStackTrace();
            }

            curTime = System.currentTimeMillis() / 1000;
            long t1 = System.currentTimeMillis() / 1000;
            Date dt = new Date();
            long t2 = dt.getTime() / 1000;
            int h = dt.getHours();
            int m = dt.getMinutes();
            int s = dt.getSeconds();
            long t3 = Calendar.getInstance().getTimeInMillis() / 1000;

            Log.e("TIMEOFFSET", String.format("unix_Time = %d, curTime = %d", unixTime, curTime));
            timeOffset = (int) (curTime - unixTime);

            if (timeOffset > 0)
                return timeOffset;
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setStreamAdapter(final ArrayList<ProgramList> programLists) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (programLists != null)
                    if (programLists.size() > 0) {
                        StreamAdapter listAdapter = new StreamAdapter(programLists, getActivity());
                        channel_show_alllist.setAdapter(listAdapter);
                    }
            }
        });
    }

    public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.DataObjectHolder> {
        private Context context;
        private int mlistPosition = 0;
        private ArrayList<ProgramList> list_data;

        public StreamAdapter(ArrayList<ProgramList> list_data, Context context) {
            this.context = context;
            this.list_data = list_data;
        }

        @Override
        public StreamAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_row_channel_details, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final StreamAdapter.DataObjectHolder holder, final int position) {

            try {
                holder.txtVideoName.setText(list_data.get(position).getName());
                String strTextTime = ChannelUtils.parseDateToddMMyyyy(list_data.get(position).getStart_at());
                holder.txtStartTime.setText(String.valueOf(strTextTime));

                String strThumbUrl = "http://img.youtube.com/vi/" + list_data.get(position).getPlaylist().get(0).getData() + "/default.jpg";
                holder.imgVideoThumbnail.loadImage(strThumbUrl);


                holder.item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strTitle = list_data.get(position).getName();
                        String strUrl = list_data.get(position).getPlaylist().get(0).getData();
                        if (!TextUtils.isEmpty(strUrl)) {
                            if (demanddialog != null && demanddialog.isShowing()) {
                                demanddialog.dismiss();
                            }

                            demanddialog = new OnDemandDialog(getActivity());
                            demanddialog.setInfo(strTitle, strUrl, position);
                            demanddialog.setActivity(getActivity());
                            demanddialog.show();
                        } else {
                            Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
                        }
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
            public TextView txtVideoName;
            public TextView txtStartTime;
            public DynamicImageView imgVideoThumbnail;
            RelativeLayout item_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                txtVideoName = (TextView) itemView.findViewById(R.id.ui_video_row_txt_tittle);
                txtStartTime = (TextView) itemView.findViewById(R.id.ui_video_row_txt_time);
                imgVideoThumbnail = (DynamicImageView) itemView.findViewById(R.id.ui_video_row_image);
                item_layout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
            }
        }
    }

    public void onClickShowTab() {
        scroll_arrow_layout.setVisibility(View.GONE);
        frameMenu.setVisibility(View.VISIBLE);
        overall_scroll.setVisibility(View.GONE);

        linearShowTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_light));
        linearChannelTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_dark));

        show_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        channel_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

    }

    public void onClickChannelTab() {
        scroll_arrow_layout.setVisibility(View.VISIBLE);
        frameMenu.setVisibility(View.GONE);
        overall_scroll.setVisibility(View.VISIBLE);

        linearChannelTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_light));
        linearShowTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_list_dark));
        channel_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        show_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    public class AllChannelAdapter extends RecyclerView.Adapter<AllChannelAdapter.DataObjectHolder> {
        ArrayList<ChannelScheduler> channelModels;
        Context context;

        public AllChannelAdapter(ArrayList<ChannelScheduler> channelModels, Context context) {
            this.channelModels = channelModels;
            this.context = context;
        }


        @Override
        public AllChannelAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_allchannel_grid, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(AllChannelAdapter.DataObjectHolder holder, final int position) {
            try {
                Log.d("cha_images", channelModels.get(position).getLogo());
                Log.d("cha_images", channelModels.get(position).getName());
                holder.txtChannelName.setText(channelModels.get(position).getName());
                horizontal_listview_item_count.setVisibility(View.VISIBLE);
                horizontal_listview_item_count.setText(channelModels.size() + " " + "Channels found");

                Image.loadImage(getContext().getApplicationContext(), channelModels.get(position).getLogo(), holder.imageView);

                holder.item_grid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            channelSelected = true;
                            paginate_ids = browseall_ids;
                            for (int i = 0; i < channelModels.size(); i++) {
                                if (channelModels.get(position).getSlug() == channelModels.get(i).getSlug()) {
                                    ChannelScheduler cs = channelModels.get(position);
                                    channelModels.remove(position);
                                    channelModels.add(0, cs);
                                }
                            }

                            setChannelsList(channelModels);
                            try {
                                selectedChannelName = channelModels.get(0).getName();
                                Utilities.setAnalytics(mTracker, "Channels-" + selectedChannelName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return channelModels.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;
            TextView txtChannelName;
            LinearLayout item_grid;

            public DataObjectHolder(View itemView) {
                super(itemView);
                item_grid = (LinearLayout) itemView.findViewById(R.id.item_grid);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
                txtChannelName = (TextView) itemView.findViewById(R.id.txtChannelName);

                Utilities.setViewFocus(getActivity(), item_grid);
            }
        }

    }


    private void addhorizontalScrolllayouts(final LinearLayout dynamic_horizontalViews_layout, final ArrayList<ChannelCategoryList> channelMainCategoryList, final int position) {

        horizontal_listview_channel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        left_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontal_listview.
                        post(new Runnable() {
                            public void run() {
                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() - ((width / 5) * 4), horizontal_listview.getScrollY());

                            }
                        });

            }
        });

        right_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontal_listview.
                        post(new Runnable() {
                            public void run() {
                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() + ((width / 5) * 4), horizontal_listview.getScrollY());


                            }
                        });
            }
        });

        Utilities.setViewFocus(getActivity(), horizontal_listview_channel_close);
        Utilities.setViewFocus(getActivity(), left_slide);
        Utilities.setViewFocus(getActivity(), right_slide);

        int layoutwidth = recycler_layout.getLayoutParams().width;
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    Log.d("layoutwidth::", "width" + width);

                    for (int l = 0; l < channelMainCategoryList.size(); l++) {
                        final int l1 = l;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_channel_list, null);
                                final TextView horizontal_imageView = (TextView) itemmlayout.findViewById(R.id.horizontal_imageView);
                                final LinearLayout ll = (LinearLayout) itemmlayout.findViewById(R.id.ll);

                                horizontal_imageView.setText(channelMainCategoryList.get(l1).getName());
                                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams((width / 5), LinearLayout.LayoutParams.WRAP_CONTENT);
                                itemmlayout.setTag(l1);

                                if (l1 != 0) {
                                    vp.setMargins(0, 0, 0, 0);
                                }


                                if (mParam1.equalsIgnoreCase("" + HomeActivity.channelID)) {
                                    if (l1 == 0) {
                                        horizontal_imageView.setBackgroundResource(R.drawable.btn_favourite);
                                        int x, y;
                                        x = horizontal_imageView.getLeft();
                                        y = horizontal_imageView.getTop();
                                    }
                                } else {
                                    for (int k = 0; k < channelMainCategoryList.get(l1).getSubCategories().size(); k++) {
                                        if (channelMainCategoryList.get(l1).getSubCategories().get(k).getSlug().equalsIgnoreCase(mParam1)) {
                                            horizontal_imageView.setBackgroundResource(R.drawable.btn_favourite);
                                            int x, y;
                                            x = horizontal_imageView.getLeft();
                                            y = horizontal_imageView.getTop();
                                        }
                                    }

                                }


                                final int finalL = l1;
                                itemmlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            String tag = itemmlayout.getTag().toString();
                                            View lv = (View) dynamic_horizontalViews_layout.findViewWithTag(horizontal_position);
                                            if (lv != null) {
                                                final TextView horizontal_imageViewww = (TextView) lv.findViewById(R.id.horizontal_imageView);
                                                horizontal_imageViewww.setBackgroundResource(0);
                                            }

                                            horizontal_imageView.setBackgroundResource(R.drawable.btn_favourite);
                                            horizontal_position = Integer.parseInt(tag);
                                            if (channelMainCategoryList.get(Integer.parseInt(tag)).getSubCategories().size() > 0)
                                                loadsubcategories(channelMainCategoryList.get(finalL).getSubCategories(), channelMainCategoryList.get(finalL).getName());
                                            else {
                                                horizontal_listview_item_count.setVisibility(View.INVISIBLE);
                                                horizontal_channel_list.setVisibility(View.INVISIBLE);
                                                horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
                                                aBoolean = false;
                                                loadChannelList(channelMainCategoryList.get(Integer.parseInt(tag)).getSlug(), true, false);
                                            }
                                            if (mListener != null)
                                                mListener.refereshcategoryList(channelMainCategoryList.get(finalL).getSlug());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                                Utilities.setViewFocus(getActivity(), itemmlayout);
                                horizontal_imageView.setLayoutParams(vp);
                                image_view.addView(itemmlayout);
                            }
                        });


                    }

                }
            });

            aBoolean = false;
            horizontal_listview_item_count.setVisibility(View.INVISIBLE);
            horizontal_channel_list.setVisibility(View.INVISIBLE);
            horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);

            if (mParam1.equalsIgnoreCase("" + HomeActivity.channelID)) {
                if (channelMainCategoryList.size() > 0) {
                    loadChannelList(mParam1, true, false);
                }
            } else {
                loadChannelList(channelMainCategoryList.get(0).getSlug(), true, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadsubcategories(final ArrayList<ChannelCategoryList> temp_list, String name) {
        if (temp_list == null) {
            return;
        }
        final AlertDialog.Builder builderMovieList = new AlertDialog.Builder(activity);

        LayoutInflater inflater12 = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater12.inflate(R.layout.alert_dialog, null);
        View titleLayout = inflater12.inflate(R.layout.alert_dialog_title, null);
        builderMovieList.setCustomTitle(titleLayout);
        builderMovieList.setView(layout);

        TextView txtTitle = (TextView) titleLayout.findViewById(R.id.txtDialogTitle);
        txtTitle.setText(name);

        ListView listContents = (ListView) layout.findViewById(R.id.listContents);
        ArrayList<String> arrayAdapter = new ArrayList<>();
        for (int i = 0; i < temp_list.size(); i++) {
            try {

                String strName = temp_list.get(i).getName();

                arrayAdapter.add(strName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        AlertDailogListAdapter adapter = new AlertDailogListAdapter(activity, arrayAdapter);
        listContents.setAdapter(adapter);

        final AlertDialog dialog = builderMovieList.create();
        Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listContents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    horizontal_listview_item_count.setVisibility(View.INVISIBLE);
                    horizontal_channel_list.setVisibility(View.INVISIBLE);
                    horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
                    aBoolean = false;
                    loadChannelList(temp_list.get(position).getSlug(), true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(nWidth - 50, nHeight - 100);
        dialog.getWindow().setGravity(Gravity.CENTER);

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
}
