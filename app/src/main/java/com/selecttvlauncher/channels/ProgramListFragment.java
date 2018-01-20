package com.selecttvlauncher.channels;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.views.CustomScrollView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class ProgramListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity activity;
    private ChannelTimelineFragment.ChannelTimeLineListener mListener;
    private Typeface OpenSans_Regular;
    private ChannelScheduler channelScheduler;
    private RecyclerView recyclerViewProgramList;
    private CustomScrollView horizontalScrollView;
    private AdapterListenMainListSub timelineAdapter;
    private TextView txtLoading;
    private Handler handler;
    private Runnable runnable;

    private ArrayList<ProgramList> programListsALL = new ArrayList<>();

    private int markedRulerLineLength = 0;
    private int channelListWidth = 0;
    private int adapterPosition = -1;
    private int numberOfMoves = 0;
    private int nWidth = 0;
    private int cellwidth = 0;
    private int nHeight = 0;
    private int limit = 6;
    private boolean canScroll = true;

    public ProgramListFragment() {
        // Required empty public constructor
    }

    interface LoadProgramListener {
        void onProgramListLoaded(ArrayList<ProgramList> programLists);
    }


    @SuppressLint("ValidFragment")
    public ProgramListFragment(ChannelTimelineFragment.ChannelTimeLineListener mListener, CustomScrollView horizontalScrollView) {
        this.mListener = mListener;
        this.horizontalScrollView = horizontalScrollView;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgramListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramListFragment newInstance(String param1, String param2) {
        ProgramListFragment fragment = new ProgramListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProgramListFragment newInstance(ChannelScheduler channelScheduler, int adapterPosition, int markedRulerLineLength, int channelListWidth, CustomScrollView horizontalScrollView, ChannelTimelineFragment.ChannelTimeLineListener mListener) {
        ProgramListFragment fragment = new ProgramListFragment(mListener, horizontalScrollView);
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, channelScheduler);
        args.putInt(ARG_PARAM2, adapterPosition);
        args.putInt(ARG_PARAM3, markedRulerLineLength);
        args.putInt(ARG_PARAM4, channelListWidth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        if (getArguments() != null) {
            channelScheduler = (ChannelScheduler) getArguments().getSerializable(ARG_PARAM1);
            adapterPosition = getArguments().getInt(ARG_PARAM2);
            markedRulerLineLength = getArguments().getInt(ARG_PARAM3);
            channelListWidth = getArguments().getInt(ARG_PARAM4);
        }

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        nWidth = displayMetrics.widthPixels;
        cellwidth = (nWidth - ChannelUtils.convertDpToPixels(activity)) / 3;
        numberOfMoves = cellwidth / 5;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_list, container, false);
        txtLoading = (TextView) view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(OpenSans_Regular);
        recyclerViewProgramList = (RecyclerView) view.findViewById(R.id.fragment_channel_program_list_item);
        setRecyclerviewChangingAnimator(recyclerViewProgramList, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recyclerViewProgramList.setLayoutManager(linearLayoutManager);
        recyclerViewProgramList.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewProgramList.setVisibility(View.GONE);

        loadProgramList(channelScheduler, adapterPosition, new LoadProgramListener() {
            @Override
            public void onProgramListLoaded(ArrayList<ProgramList> programLists) {
                setAdapter(programLists);
            }
        });
    }

    private void setAdapter(ArrayList<ProgramList> programLists) {
        txtLoading.setVisibility(View.GONE);
        recyclerViewProgramList.setVisibility(View.VISIBLE);

        ArrayList<ProgramList> tempList = new ArrayList<>();
        programListsALL = programLists;
        if (programListsALL.size() > limit) {
            tempList.addAll(programListsALL.subList(0, limit));
        } else {
            tempList.addAll(programListsALL);
        }
        timelineAdapter = new AdapterListenMainListSub(channelScheduler, tempList, activity, adapterPosition == 0, markedRulerLineLength, horizontalScrollView, mListener);
        recyclerViewProgramList.setAdapter(timelineAdapter);

        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("runnable", ":: running");
                if (timelineAdapter != null)
                    scrollHorizontalScrollView();
            }
        }, 1000);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (handler != null) {
            handler.removeCallbacks(null);
            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacks(runnable);
        }

        if (handler != null)
            if (runnable != null)
                handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onPause() {
        if (handler != null) {
            handler.removeCallbacks(null);
            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacks(runnable);
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(null);
            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (handler != null) {
            handler.removeCallbacks(null);
            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }


    public void scrollHorizontalScrollView() {
        if (!canScroll)
            return;
        try {
            ArrayList<ProgramList> programLists = timelineAdapter.getProgramLists();

            int currentPlayingPosition = VideoFilter.getCurentVideoPosition(programLists);
            if (currentPlayingPosition >= 0 && currentPlayingPosition < programLists.size()) {
                final long startTime = ChannelUtils.getDurationFromDate(programLists.get(currentPlayingPosition).getStart_at());
                int i1 = ((int) ChannelUtils.getDuration(programLists.get(currentPlayingPosition).getDuration())) / cellwidth;
                int i2 = (int) (ChannelUtils.GetUnixTime() - ChannelUtils.getDurationFromDate(programLists.get(currentPlayingPosition).getStart_at()));
                final int seekOffset = (i2 / i1);
                horizontalScrollView.smoothScrollTo(seekOffset + (currentPlayingPosition * cellwidth) - markedRulerLineLength, markedRulerLineLength);

                if (currentPlayingPosition >= programLists.size() - 2) {
                    if (programListsALL.size() > programLists.size()) {
                        timelineAdapter.addItem(programListsALL.get(programLists.size()));
                        if (currentPlayingPosition > limit) {
                            recyclerViewProgramList.removeViewAt(0);
                            timelineAdapter.removeItem(0);
                        }
                        return;
                    }
                }
            } else loadMorePrograms();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (handler != null)
                handler.postDelayed(runnable, 1000);
        }

    }


    private void setRecyclerviewChangingAnimator(RecyclerView recyclerView, boolean canAnimate) {
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(canAnimate); //if false animate will be not working
        }
    }

    private void loadMorePrograms() {
        txtLoading.setVisibility(View.VISIBLE);
        canScroll = false;
        loadProgramList(this.channelScheduler, adapterPosition, new LoadProgramListener() {
            @Override
            public void onProgramListLoaded(ArrayList<ProgramList> programLists) {
                canScroll = true;
                if (programLists.size() > 0) {
                    filterList(programLists);
                } else if (handler != null)
                    handler.postDelayed(runnable, 1000);
            }
        });
    }

    private void filterList(ArrayList<ProgramList> programLists) {
        recyclerViewProgramList.removeAllViews();
        programListsALL.clear();
        timelineAdapter.getProgramLists().clear();
        timelineAdapter = null;
//                timelineAdapter.setProgramList(programLists);
        if (handler != null) {
            handler.removeCallbacks(null);
            handler = null;
        }
        setAdapter(programLists);
    }

    private void loadProgramList(final ChannelScheduler mChannelScheduler, final int position, final LoadProgramListener loadProgramListener) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                LauncherApplication.getmWebService().loadProgramData(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug(), numberOfMoves, new ProgramApiListener() {

                    @Override
                    public void onProgramsLoaded(String categorySlug, String slug, Programs mPrograms) {
                        if (mPrograms != null) {
                            ArrayList<ProgramList> programLists = removePreviousPrograms(mPrograms.getProgramlist());
                            mPrograms.setProgramlist(programLists);

                            mChannelScheduler.setPrograms(mPrograms);
                            if (mPrograms.getProgramlist() != null) {
                                if (mChannelScheduler != null)
                                    if (mChannelScheduler.getPrograms() != null && position == 0) {
                                        try {
                                            if (mListener != null)
                                                mListener.loadStreamList(mChannelScheduler.getPrograms().getProgramlist());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                            }

                            Log.e("program List ", "size:::" + mPrograms.getProgramlist().size());
                            mChannelScheduler.setPrograms(mPrograms);
                            if (mPrograms.getProgramlist() != null && position == 0)
                                if (mListener != null)
                                    mListener.setPlayVideo(mChannelScheduler);

                            if (mPrograms.getProgramlist() != null) {
                                if (mPrograms.getProgramlist().size() > 0) {
                                    final Programs finalPrograms = mPrograms;
                                    if (activity != null)
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadProgramListener.onProgramListLoaded(finalPrograms.getProgramlist());
                                            }
                                        });
                                }
                            }
                        }
                    }


                    @Override
                    public void onLoadingFailed() {

                    }

                    @Override
                    public void onNetworkError() {

                    }
                });
            }
        });
    }

    private ArrayList<ProgramList> removePreviousPrograms(ArrayList<ProgramList> programLists) {
        Log.e("filter-before::", " programs list size::" + programLists.size());
        ArrayList<ProgramList> tempList = new ArrayList<>();
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        boolean isAddedPreviousItem = false;
        for (int i = 0; i < programLists.size(); i++) {
            long startTime = ChannelUtils.getDurationFromDate(programLists.get(i).getStart_at());
            long endTime = ChannelUtils.getDurationFromDate(programLists.get(i).getEnd_at());
            if (startTime < nowAsPerDeviceTimeZone && nowAsPerDeviceTimeZone < endTime || startTime > nowAsPerDeviceTimeZone) {
                if (!isAddedPreviousItem) {
                    isAddedPreviousItem = true;
                    if (i > 0) {
                        tempList.add(programLists.get(i - 1));
                    }
                }
                tempList.add(programLists.get(i));
            }
        }
        Log.e("filter-after::", " programs list size::" + tempList.size());
        return tempList;
    }

}
