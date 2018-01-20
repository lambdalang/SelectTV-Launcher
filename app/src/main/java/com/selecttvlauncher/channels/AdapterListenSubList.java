package com.selecttvlauncher.channels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.views.CustomScrollView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Appsolute dev on 23-Aug-17.
 */

public class AdapterListenSubList extends RecyclerView.Adapter<AdapterListenSubList.DataObjectHolder> {
    private Context context;
    private Activity activity;
    private int mlistPosition = 0;
    public ArrayList<ChannelScheduler> data_list = new ArrayList<>();
    public ArrayList<ChannelScheduler> allChannelList = new ArrayList<>();
    public ArrayList<RecyclerView> recyclerList = new ArrayList<>();
    public ArrayList<Fragment> fragmentList = new ArrayList<>();
    private int nWidth = 0;
    private int nHeight = 0;
    private int cellwidth = 0;
    private ChannelTimelineFragment.ChannelTimeLineListener channelTimeLineListener;
    private int markedRulerLine = 0;
    int setChannelsCount = 0;
    int channelListWidth = 0;
    int limit = 10;
    FragmentManager fragmentManager;

    public void setChannelListWidth(int channelListWidth) {
        this.channelListWidth = channelListWidth;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public AdapterListenSubList(ArrayList<ChannelScheduler> data_list, Activity context, int markedRulerLine, ChannelTimelineFragment.ChannelTimeLineListener channelTimeLineListener) {
        recyclerList.clear();
        this.context = context;
        this.activity = (Activity) context;
        this.data_list = data_list;
//        this.allChannelList = data_list;
//        if (data_list.size() > limit) {
//            this.data_list.addAll(data_list.subList(0, limit));
//        } else {
//            this.data_list.addAll(data_list);
//        }
        setChannelsCount = data_list.size();
        this.markedRulerLine = markedRulerLine;
        this.channelTimeLineListener = channelTimeLineListener;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        nWidth = displayMetrics.widthPixels;
        nHeight = displayMetrics.heightPixels;
        cellwidth = (nWidth - ChannelUtils.convertDpToPixels(context)) / 3;
    }


    @Override
    public AdapterListenSubList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlayout, parent, false);
        return new DataObjectHolder(view);
    }

    public void addDta(int position, ArrayList<ProgramList> data) {
        data_list.get(position).getPrograms().getProgramlist().addAll(data);
        Log.d(":: data_list.", "::::" + data_list.get(position).getPrograms().getProgramlist());
        notifyDataSetChanged();
    }

    public ChannelScheduler getItem(int position) {
        return data_list.get(position);
    }

    public void swapItems(int itemAIndex) {
        int oldindex = itemAIndex;
        Collections.swap(data_list, oldindex, 0);
        //notifyItemMoved(oldindex, 0);
    }

    public void setDataset(ArrayList<ChannelScheduler> dataset) {
        this.data_list = dataset;
        notifyDataSetChanged();
    }

    public void addList(ArrayList<ChannelScheduler> dataset) {
        this.data_list.addAll(dataset);
        this.notifyItemInserted(this.data_list.size());
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final AdapterListenSubList.DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ChannelScheduler channelScheduler = data_list.get(holder.getAdapterPosition());

        Log.e("type::", channelScheduler.getType() + "position::" + holder.getAdapterPosition());

        try {
            if (channelScheduler.getType().equalsIgnoreCase("live/video") || channelScheduler.getType().equalsIgnoreCase("live/radio")) {
                holder.layoutInfo.setVisibility(View.VISIBLE);
                holder.layoutProgramList.setVisibility(View.GONE);
//                    holder.horizontalScrollView.setVisibility(View.GONE);
//                holder.fragment_channel_program_list_item.setVisibility(View.GONE);
                holder.txtChannelName.setText(channelScheduler.getDescription());
                holder.txtChannel_time.setText("LIVE");
                if (channelScheduler.getStreams() != null) {
                    if (channelScheduler.getStreams().size() > 0) {
//                            if (position == 0)
//                                mListener.setPlayVideo(channelScheduler);
                    } else {
                        loadStream(channelScheduler, position);
                    }
                } else {
                    loadStream(channelScheduler, position);
                }
            }

            if (channelScheduler.getType().equalsIgnoreCase("linear/stream")) {
                holder.layoutInfo.setVisibility(View.GONE);
                holder.layoutProgramList.setVisibility(View.VISIBLE);
//                holder.fragment_channel_program_list_item.setVisibility(View.VISIBLE);

                try {
                    int containerId = holder.layoutProgramList.getId();
                    Fragment oldFragment = fragmentManager.findFragmentById(containerId);
                    if (oldFragment != null) {
                        fragmentManager.beginTransaction().remove(oldFragment).commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int newContainerId = ChannelUtils.GetUniqueID();// My method
                holder.layoutProgramList.setId(newContainerId);// Set container id

                ProgramListFragment fragment = ProgramListFragment.newInstance(channelScheduler, position, markedRulerLine, channelListWidth, holder.horizontalScrollView1, channelTimeLineListener);
                fragmentList.add(fragment);
                fragmentManager.beginTransaction().replace(newContainerId, fragment).commitAllowingStateLoss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        //        private RecyclerView fragment_channel_program_list_item;
        private TextView fragment_ondemandlist_items;
        private TextView txtChannelName, txtChannel_time;
        private LinearLayout list_layout, layoutInfo;
        private CustomScrollView horizontalScrollView1;
        private FrameLayout layoutProgramList;

        public DataObjectHolder(View itemView) {
            super(itemView);
            list_layout = (LinearLayout) itemView.findViewById(R.id.list_layout);
            layoutInfo = (LinearLayout) itemView.findViewById(R.id.layoutInfo);
            txtChannelName = (TextView) itemView.findViewById(R.id.txtChannelName);
            txtChannel_time = (TextView) itemView.findViewById(R.id.txtChannel_time);
            horizontalScrollView1 = (CustomScrollView) itemView.findViewById(R.id.horizontal_scroll1);
            horizontalScrollView1.setScrolledRight(false);
            horizontalScrollView1.setEnableScrolling(false);
            layoutProgramList = (FrameLayout) itemView.findViewById(R.id.layoutProgramList);
        }
    }

    public void swaplist(int position) {
        try {
            Log.e("position", "::" + position);
            ChannelScheduler cs = data_list.get(position);
            ArrayList<ChannelScheduler> tempList = new ArrayList<>();
            tempList.addAll(data_list);
            tempList.remove(position);
            tempList.add(0, cs);
            setDataset(tempList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void clearProgramListFragments() {
        if (fragmentManager == null)
            return;
        try {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for (Fragment fragment : fragmentList) {
                if (fragment != null)
                    ft.remove(fragment);
            }
            ft.commit();
            fragmentList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateChannelList() {
        try {
            int end = 0;
            ArrayList<ChannelScheduler> filteredList = new ArrayList<>();
            if (allChannelList.size() > 0)
                if (this.data_list.size() < allChannelList.size()) {
                    int offset = data_list.size() - 1;
                    filteredList.add(allChannelList.get(offset + 1));
                    addList(filteredList);
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void loadStream(final ChannelScheduler mChannelScheduler, final int position) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LauncherApplication.getmWebService().loadSteamData(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug(), new StreamApiListener() {
                    @Override
                    public void onStreamLoaded(String categorySlug, String slug, final ArrayList<Streams> mStream) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mStream.size() > 0) {
                                    mChannelScheduler.setStreams(mStream);
                                    if (mStream != null && position == 0)
                                        channelTimeLineListener.setPlayVideo(mChannelScheduler);
                                }
                            }
                        });
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
        thread.start();
    }

}