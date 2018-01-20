package com.selecttvlauncher.channels;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.views.CustomScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Appsolute dev on 23-Aug-17.
 */

public class AdapterListenMainListSub extends RecyclerView.Adapter<AdapterListenMainListSub.DataObjectHolder> {
    private Context context;
    private Activity activity;
    private Typeface OpenSans_Regular;

    private ArrayList<ProgramList> programLists = new ArrayList<>();
    private int cellwidth;
    private int numberOfMoves = 0;
    private int nWidth = 0;
    private int nHeight = 0;
    private boolean isFirst = false;
    private boolean loading;
    private CustomScrollView horizontalScrollView;
    ChannelTimelineFragment.ChannelTimeLineListener channelTimeLineListener;
    private int markedRulerLine = 0;
    private ChannelScheduler channelScheduler;
    private boolean canScroll = true;
    private int limit = 6;
    private int mlistPosition = 0;

    public ArrayList<ProgramList> getProgramLists() {
        return programLists;
    }

    public AdapterListenMainListSub(ChannelScheduler channelScheduler, ArrayList<ProgramList> programListsALL,
                                    Activity context, boolean isFirst, int markedRulerLine, CustomScrollView horizontalScrollView,
                                    ChannelTimelineFragment.ChannelTimeLineListener channelTimeLineListener) {
        this.context = context;
        this.activity = (Activity) context;
        this.channelScheduler = channelScheduler;
        this.isFirst = isFirst;
        this.markedRulerLine = markedRulerLine;
        this.channelTimeLineListener = channelTimeLineListener;

        Log.e("program list size", "::" + programListsALL.size());
        this.horizontalScrollView = horizontalScrollView;
        if (programListsALL.size() > limit) {
            this.programLists.addAll(programListsALL.subList(0, limit));
        } else {
            this.programLists.addAll(programListsALL);
        }
        OpenSans_Regular = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular_1.ttf");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        nWidth = displayMetrics.widthPixels;
        nHeight = displayMetrics.heightPixels;
        cellwidth = (nWidth - ChannelUtils.convertDpToPixels(context)) / 3;
        numberOfMoves = cellwidth / 5;

    }

    public void setProgramList(ArrayList<ProgramList> programListsALL) {
        if (programListsALL.size() > limit) {
            this.programLists.addAll(programListsALL.subList(0, limit));
        } else {
            this.programLists.addAll(programListsALL);
        }
        this.notifyDataSetChanged();
    }

    public void setList(ArrayList<ProgramList> programList) {
        this.programLists = programList;
        this.notifyDataSetChanged();
    }

    public void addList(ArrayList<ProgramList> programList) {
        if (programList.size() > limit) {
            this.programLists.addAll(programList.subList(0, limit));
        } else {
            this.programLists.addAll(programList);
        }
        this.notifyItemInserted(this.programLists.size());
    }

    public void addItem(ProgramList programList) {
        this.programLists.add(programList);
        this.notifyItemInserted(this.programLists.size());
    }

    public void removeItem(int position) {
        this.programLists.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public AdapterListenMainListSub.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_desc_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterListenMainListSub.DataObjectHolder holder, final int position) {

        holder.itemView.getLayoutParams().width = cellwidth;
        if (programLists.get(position) != null) {
            holder.fragment_ondemandlist_items.setText(programLists.get(position).getName());
            String strTextTime = ChannelUtils.parseDateToddMMyyyy(programLists.get(position).getStart_at());
            holder.txtChannel_time.setText("Start Time: " + strTextTime);

            try {

                holder.item_grid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int tag_pos = position;
                            String strUrl = null;
                            String strTitle = null;
                            try {
                                strTitle = programLists.get(tag_pos).getName();
                                strUrl = programLists.get(tag_pos).getPlaylist().get(0).getData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!TextUtils.isEmpty(strUrl)) {
                                channelTimeLineListener.showOnDemandDialog(strTitle, strUrl, tag_pos);
                            } else {
                                Toast.makeText(context, "No url Found", Toast.LENGTH_SHORT).show();
                            }


                            try {
                                channelTimeLineListener.setAnalytics(programLists.get(tag_pos).getName());
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
    }

    @Override
    public int getItemCount() {
        return programLists.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private LinearLayout fragment_ondemandlist_items_layout;
        private TextView fragment_ondemandlist_items, txtChannel_time;
        private RelativeLayout item_grid;

        public DataObjectHolder(View itemView) {
            super(itemView);
            fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
            txtChannel_time = (TextView) itemView.findViewById(R.id.txtChannel_time);
            item_grid = (RelativeLayout) itemView.findViewById(R.id.item_grid);
            txtChannel_time.setTypeface(OpenSans_Regular);
            fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
        }
    }


    public void setMoreData(List<ProgramList> moreProgramLists) {
        try {
            if (moreProgramLists.size() > 0) {
                setLoaded(false);
                programLists.addAll(moreProgramLists);
                this.notifyItemInserted(programLists.size());
                this.notifyDataSetChanged();
            } else {
                setLoaded(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //its used for Loded Data set.
    public void setLoaded(boolean load) {
        loading = load;
    }
}