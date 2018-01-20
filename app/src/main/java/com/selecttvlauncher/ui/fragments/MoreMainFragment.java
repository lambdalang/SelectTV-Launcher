package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.More;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.AutofitRecylerview;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class MoreMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView fragment_more_prev_icon;
    private AutofitRecylerview more_fragment_app_list;
    private ArrayList<More> mMoreArray;

    GridLayoutManager gridLayoutManager;
    private ProgressBar fragment_game_progress;
    private LinearLayout more_fragment_app_layout;
    private int width;
    private int spacing;
    private int height;
    private Tracker mTracker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreMainFragment newInstance(String param1, String param2) {
        MoreMainFragment fragment = new MoreMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MoreMainFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_main, container, false);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        more_fragment_app_layout = (LinearLayout) view.findViewById(R.id.more_fragment_app_layout);
        fragment_more_prev_icon = (ImageView) view.findViewById(R.id.fragment_more_prev_icon);
        more_fragment_app_list = (AutofitRecylerview) view.findViewById(R.id.more_fragment_app_list);
        fragment_game_progress = (ProgressBar) view.findViewById(R.id.fragment_game_progress);
        width = more_fragment_app_list.getMeasuredWidth();
        height = more_fragment_app_list.getMeasuredHeight();
        gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        more_fragment_app_list.hasFixedSize();
        more_fragment_app_list.setLayoutManager(gridLayoutManager);
        int spanCount = 5; // 3 columns
        spacing = 25; // 50px
        boolean includeEdge = true;
        more_fragment_app_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        new LoadingGameMoreTask().execute();
        fragment_more_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.isPayperview = "";
                HomeActivity.toolbarMainContent = "";
                HomeActivity.toolbarSubContent = "";
                ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    private class LoadingGameMoreTask extends AsyncTask<Object, Object, Object> {

        String url, display_mode, name, weight, tags, device, image, description, id, slug;

        @Override
        protected void onPreExecute() {
            more_fragment_app_layout.setVisibility(View.GONE);
            fragment_game_progress.setVisibility(View.VISIBLE);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.m_jsonGamemore = JSONRPCAPI.getGameMoreList();
                if (HomeActivity.m_jsonGamemore == null) return null;
                Log.d("m_jsonDemandListItems::", "::" + HomeActivity.m_jsonGamemore);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                fragment_game_progress.setVisibility(View.GONE);
                more_fragment_app_layout.setVisibility(View.VISIBLE);
                if (HomeActivity.m_jsonGamemore != null && HomeActivity.m_jsonGamemore.length() > 0) {

                    mMoreArray = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonGamemore.length(); i++) {

                        JSONObject object = HomeActivity.m_jsonGamemore.getJSONObject(i);
                        if (object.has("url")) {
                            url = object.getString("url");
                        }

                        if (object.has("display_mode")) {
                            display_mode = object.getString("display_mode");
                        }

                        if (object.has("name")) {
                            name = object.getString("name");
                        }

                        if (object.has("weight")) {
                            weight = object.getString("weight");
                        }

                        if (object.has("tags")) {
                            tags = object.getString("tags");
                        }

                        if (object.has("device")) {
                            device = object.getString("device");
                        }

                        if (object.has("image")) {
                            image = object.getString("image");
                        }

                        if (object.has("description")) {
                            description = object.getString("description");
                        }
                        if (object.has("id")) {
                            id = object.getString("id");
                        }
                        if (object.has("slug")) {
                            slug = object.getString("slug");
                        }

//                        if (!slug.equalsIgnoreCase("video-tv-cast-samsung-android") && !slug.equalsIgnoreCase("samsung-vr-ios") && !slug.equalsIgnoreCase("samsung-vr-android")) {
                        mMoreArray.add(new More(url, display_mode, name, weight, tags, device, image, description, id, slug));
//                        }
                    }

//                    if (getActivity().getPackageName().equalsIgnoreCase(getResources().getString(R.string.broadview_package_name))) {
//                        mMoreArray.add(new More("https://play.google.com/store/apps/details?id=com.ubercab", "P", "Uber", "5", "More", "A", "https://lh3.googleusercontent.com/iyt_f1j2d2ildbFLDlS0Qf36NJMeRVZFBetcg-pmrkdQtN9C1wUTaFhloKUcwVVfQeg=w300-rw", "Uber is a ridesharing app for fast, reliable rides in minutes – day or night. There’s no need to park or wait for a taxi or bus. With Uber, you just tap to request a ride, and it’s easy to pay with credit card or cash (in selected cities only). ", "", "uber-android"));
//                        mMoreArray.add(new More("https://play.google.com/store/apps/details?id=com.weather.alps", "P", "Weather Forecasts + Alerts", "5", "More", "A", "https://lh3.googleusercontent.com/UeNewb4NX8_OY6zdUtxEEmHW8OQTRYhmU_38ZcHVcO02JEeCpsd9BMH8rJDZ_reBb1I2=w300-rw", "The Weather Channel App for Android is your best option for getting accurate weather information. Now with Mesh Network Alerts, you can receive severe weather alerts even without the internet or data. Make confident decisions, whether you are planning for the day, the entire week, or the next 15 days! We’ve improved the app so that it runs faster, offers offline access, gives you more control over data settings, and keeps you safe with the most accurate and up-to-date severe weather alerts.", "", "weather-forecasts-alerts-android"));
//                        mMoreArray.add(new More("https://play.google.com/store/apps/details?id=com.espn.score_center", "P", "ESPN", "5", "More", "A", "https://lh3.googleusercontent.com/wNpPb7xox1BacwcthbenSULDrwNRtKSkZ5xJqku-yYHSTW7Z6YP115bCTWYj-Eo6cA=w300-rw", "From scores to signings, the ESPN App is here to keep you updated. Never miss another sporting moment with up-to-the-minute scores, latest news & a range of video content. Sign in and personalise the app to receive alerts for your teams and leagues. Wherever, whenever; the ESPN app keeps you connected. ", "", "espn-android"));
//                    }

                    GameMoreList gameMoreList = new GameMoreList(getActivity(), mMoreArray);
                    more_fragment_app_list.setAdapter(gameMoreList);


                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingGameMoreTask().execute();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    public class GameMoreList extends RecyclerView.Adapter<GameMoreList.DataObjectHolder> {
        Context context;
        ArrayList<More> mores;


        public GameMoreList(Context context, ArrayList<More> mores) {
            this.context = context;
            this.mores = mores;


        }

        @Override
        public GameMoreList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.more_list_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(GameMoreList.DataObjectHolder holder, int position) {

            final String url = mores.get(position).getUrl();
            final String image_url = mores.get(position).getImage();
            final String name = mores.get(position).getName();


            Log.d("data", image_url);

            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - (spacing * 5), height - (spacing * 5));
            holder.more_gridview_item.setLayoutParams(layoutParams);*/


            holder.more_gridview_text.setText(name);
            Picasso.with(context
                    .getApplicationContext())
                    .load(image_url)
                    .fit()
                    .placeholder(R.drawable.loader_network).into(holder.more_gridview_item);


            holder.more_gridview_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!url.equalsIgnoreCase("")) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent + "-" + name);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mores.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            GridViewItem more_gridview_item;
            TextView more_gridview_text;
            LinearLayout more_gridview_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                more_gridview_layout = (LinearLayout) itemView.findViewById(R.id.more_gridview_layout);
                more_gridview_item = (GridViewItem) itemView.findViewById(R.id.more_gridview_item);
                more_gridview_text = (TextView) itemView.findViewById(R.id.more_gridview_text);
            }
        }
    }


}
