package com.selecttvlauncher.ui.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvlauncher.BeanClass.CauroselsItemBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.BeanClass.SliderBean;
import com.selecttvlauncher.BeanClass.UserAllSubscriptionBean;
import com.selecttvlauncher.BeanClass.UserSubscriptionBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utils;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.activities.LoginActivity;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MySubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySubscriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private ArrayList<UserSubscriptionBean> subscriptionList = new ArrayList<>();
    private HashMap<String, UserSubscriptionBean> user_map = new HashMap<String, UserSubscriptionBean>();
    private ArrayList<UserAllSubscriptionBean> all_subscriptionList = new ArrayList<>();
    private RecyclerView subscription_list, subscription_show_list, subscription_Movie_list;
    private AQuery aq;
    private Button set_button, skip_button;
    private TextView set_button1;
    private ImageView switch_image, switch_image1;
    private ArrayList<HorizontalListitemBean> horizontal_list_date = new ArrayList<>();

    private boolean iscableSelected = false;

    private static double list_width = 0;
    private int slider_image_width = 0;
    private LayoutInflater mlayoutinflater;

    private RelativeLayout relativelayout_select_subscriptions, layoutSwitchImage1;
    private LinearLayout linearlayout_showsbySubscription, linearlayout_dynamic_image, linearLayout_shows_tab, linearLayout_movies_tab;
    private TextView textview_manage, shows_text_tab, movies_text_tab;
    private View view_shows_left, view_shows_top, view_shows_bottom, view_shows_right, view_movies_top, view_movies_bottom, view_movies_right;
    private HorizontalScrollView horizontal_listview;
    View lastselected;
    private ImageView fragment_prev_icon;

    private int selectedPositionOfShowsbysubscriptions = -1;


    public MySubscriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySubscriptionFragment newInstance(String param1, String param2) {
        MySubscriptionFragment fragment = new MySubscriptionFragment();
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
        View view = inflater.inflate(R.layout.sub_layout, container, false);

        mContext = getActivity();
        PreferenceManager.setFirstLogin(false, mContext);
        aq = new AQuery(getActivity());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        ((HomeActivity) getActivity()).toolbarTextChange("Subscriptions", "", "", "");

        int listwidth = (width / 5) * 3;
        mlayoutinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            list_width = (0.9 * height * 0.8);
        } else {
            list_width = (0.9 * width * 0.8);
        }
        slider_image_width = (int) (list_width);


        subscription_list = (RecyclerView) view.findViewById(R.id.subscription_list);
        subscription_show_list = (RecyclerView) view.findViewById(R.id.subscription_show_list);
        subscription_Movie_list = (RecyclerView) view.findViewById(R.id.subscription_Movie_list);
        set_button = (Button) view.findViewById(R.id.set_button);
        set_button1 = (TextView) view.findViewById(R.id.set_button1);
        skip_button = (Button) view.findViewById(R.id.skip_button);
        switch_image = (ImageView) view.findViewById(R.id.switch_image);
        switch_image1 = (ImageView) view.findViewById(R.id.switch_image1);
        fragment_prev_icon = (ImageView) view.findViewById(R.id.fragment_prev_icon);

        relativelayout_select_subscriptions = (RelativeLayout) view.findViewById(R.id.relativelayout_select_subscriptions);
        layoutSwitchImage1 = (RelativeLayout) view.findViewById(R.id.layoutSwitchImage1);
        linearlayout_showsbySubscription = (LinearLayout) view.findViewById(R.id.linearlayout_showsbySubscription);
        linearlayout_dynamic_image = (LinearLayout) view.findViewById(R.id.linearlayout_dynamic_image);
        textview_manage = (TextView) view.findViewById(R.id.textview_manage);
        shows_text_tab = (TextView) view.findViewById(R.id.shows_text_tab);
        movies_text_tab = (TextView) view.findViewById(R.id.movies_text_tab);
        horizontal_listview = (HorizontalScrollView) view.findViewById(R.id.horizontal_listview);

        view_shows_left = (View) view.findViewById(R.id.view_shows_left);
        view_shows_top = (View) view.findViewById(R.id.view_shows_top);
        view_shows_bottom = (View) view.findViewById(R.id.view_shows_bottom);
        view_movies_bottom = (View) view.findViewById(R.id.view_movies_bottom);
        view_movies_right = (View) view.findViewById(R.id.view_movies_right);
        view_shows_right = (View) view.findViewById(R.id.view_shows_right);
        view_movies_top = (View) view.findViewById(R.id.view_movies_top);
        linearLayout_shows_tab = (LinearLayout) view.findViewById(R.id.linearLayout_shows_tab);
        linearLayout_movies_tab = (LinearLayout) view.findViewById(R.id.linearLayout_movies_tab);

        linearLayout_shows_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayShowsTab();
            }
        });

        linearLayout_movies_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMoviesTab();
            }
        });


        skip_button.setVisibility(View.GONE);

        subscription_list.setNestedScrollingEnabled(false);

        /*ViewGroup.LayoutParams params=subscription_list.getLayoutParams();
        params.width=listwidth;
        subscription_list.setLayoutParams(params);*/
        GridLayoutManager manager = new GridLayoutManager(mContext, 5);


        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        subscription_show_list.setLayoutManager(layoutManager);

        LinearLayoutManager linear_layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        subscription_list.setLayoutManager(linear_layoutManager1);
        subscription_list.hasFixedSize();

        GridLayoutManager mlayoutManager = new GridLayoutManager(mContext, 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        subscription_Movie_list.setLayoutManager(mlayoutManager);


        int spanCount = 3; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        subscription_show_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        subscription_Movie_list.addItemDecoration(new GridSpacingItemDecoration(4, spacing, includeEdge));

        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new updateSubscriptions().execute();
                /*Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

        set_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updateSubscriptions().execute();

                /*Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();*/

            }
        });

        textview_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    linearlayout_showsbySubscription.setVisibility(View.GONE);
                    relativelayout_select_subscriptions.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        switch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap_switch();
            }
        });

        switch_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap_switch();
            }
        });

        fragment_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HomeActivity.isPayperview = "";
                    HomeActivity.toolbarMainContent = "";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
            relativelayout_select_subscriptions.setVisibility(View.GONE);
            textview_manage.setVisibility(View.GONE);

            set_button1.setVisibility(View.VISIBLE);
            layoutSwitchImage1.setVisibility(View.VISIBLE);
        }

        new LoadSubscriptionAPI().execute();

        return view;
    }

    private void swap_switch() {
        if (iscableSelected) {
            iscableSelected = false;
            switch_image.setImageResource(R.drawable.off);
            switch_image1.setImageResource(R.drawable.off);


        } else {
            iscableSelected = true;
            switch_image.setImageResource(R.drawable.on);
            switch_image1.setImageResource(R.drawable.on);
        }
    }

    private class updateSubscriptions extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        ProgressHUD mProgressHUD;
        JSONObject result_array;
        JSONObject mJsonobj;

        @Override
        protected void onPreExecute() {
            try {
               /* progressDialog = ProgressDialog.show(mContext, "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();*/
                mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            String value = "";


            try {
                if (all_subscriptionList != null && all_subscriptionList.size() > 0) {
                    for (int i = 0; i < all_subscriptionList.size(); i++) {
                        if (all_subscriptionList.get(i).isSelected()) {
                            if (!TextUtils.isEmpty(value)) {
                                value += ",";
                            }
                            value += all_subscriptionList.get(i).getCode();

                        }

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (iscableSelected) {
                if (!TextUtils.isEmpty(value)) {
                    value = "CABLE," + value;
                } else {
                    value += "CABLE";
                }
            }
            if (!TextUtils.isEmpty(value)) {
                String list[] = value.split(",");
                PreferenceManager.setSubscribedList(list, mContext);
            } else {
                String list[] = new String[1];
                PreferenceManager.setSubscribedList(list, mContext);
            }


            result_array = JSONRPCAPI.setUserSubscription(PreferenceManager.getAccessToken(mContext), value);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            /*try {
                if (result_array == null) return;



                if(subscriptionList!=null&&subscriptionList.size()>0){

                    SubscriptionAdapter mAdapter=new SubscriptionAdapter(mContext,subscriptionList);
                    subscription_list.setAdapter(mAdapter);





                }

            } catch (Exception exception) {
                exception.printStackTrace();*/
            try {
                try {

                    if (mProgressHUD != null && mProgressHUD.isShowing()) {
                        mProgressHUD.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result_array != null && result_array.length() > 0) {
                    Log.d("result_array:::", ":::notnull");


                } else {
                    Log.d("result_array:::", ":::null");
                    Toast.makeText(getActivity(), "Subscriptions Updated", Toast.LENGTH_SHORT).show();

                }


                if (all_subscriptionList != null && all_subscriptionList.size() > 0) {
                    displayShowsbysubscriptions();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayShowsbysubscriptions() {
        linearlayout_showsbySubscription.setVisibility(View.VISIBLE);
        relativelayout_select_subscriptions.setVisibility(View.GONE);
        final ArrayList<UserAllSubscriptionBean> selectedList = new ArrayList<>();

        if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
            selectedList.addAll(all_subscriptionList);
        } else {
            for (int i = 0; i < all_subscriptionList.size(); i++) {
                if (all_subscriptionList.get(i).isSelected()) {
                    selectedList.add(all_subscriptionList.get(i));
                }
            }
        }

        if (selectedList != null && selectedList.size() > 0) {

            linearlayout_dynamic_image.removeAllViews();

            for (int i = 0; i < selectedList.size(); i++) {
                final View itemmlayout = (View) mlayoutinflater.inflate(R.layout.subscription_network_item, null);
                final GridViewItem gridview_item = (GridViewItem) itemmlayout.findViewById(R.id.gridview_item);
                if (i == 0) {
                    lastselected = gridview_item;
                    gridview_item.setBackgroundResource(R.drawable.subscription_bg);
                    new LoadNetworkData().execute("" + selectedList.get(0).getCode());

                }

                String image_url = "";
                if (selectedList.get(i).isSelected()) {
                    image_url = selectedList.get(i).getImage_url();
                } else {
                    image_url = selectedList.get(i).getGray_image_url();
                }

                aq.id(gridview_item).image(image_url);
//                aq.id(gridview_item).image(selectedList.get(i).getImage_url());  // old statement
                gridview_item.setTag(i);

                gridview_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = gridview_item.getTag().toString();
                        selectedPositionOfShowsbysubscriptions = Integer.parseInt(tag);

                        if (lastselected != null) {
                            lastselected.setBackgroundResource(0);
                        }
                        gridview_item.setBackgroundResource(R.drawable.subscription_bg);
                        lastselected = gridview_item;

                        String t = selectedList.get(Integer.parseInt(tag)).getCode();

                        if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
                            selectedList.get(selectedPositionOfShowsbysubscriptions).setSelected(!selectedList.get(selectedPositionOfShowsbysubscriptions).isSelected());
                            if (selectedList.get(selectedPositionOfShowsbysubscriptions).isSelected()) {
//                            holder.installed_textView.setVisibility(View.VISIBLE);
//                            holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
//                            holder.installed_textView.setText("Subscribed");
//                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(selectedList.get(selectedPositionOfShowsbysubscriptions).getImage_url())) {
                                    aq.id(gridview_item).image(selectedList.get(selectedPositionOfShowsbysubscriptions).getImage_url());
                                }
                            } else {
//                            holder.installed_textView.setVisibility(View.VISIBLE);
//                            holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
//                            holder.installed_textView.setText("UnSubscribed");
//                            holder.tick_imageButton.setVisibility(View.GONE);
                                if (!TextUtils.isEmpty(selectedList.get(selectedPositionOfShowsbysubscriptions).getGray_image_url())) {
                                    aq.id(gridview_item).image(selectedList.get(selectedPositionOfShowsbysubscriptions).getGray_image_url());
                                }
                            }
                        }

                        new LoadNetworkData().execute("" + t);

                    }
                });

                linearlayout_dynamic_image.addView(itemmlayout);


            }

        } else {

        }

    }

    private class LoadSubscriptionAPI extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        ProgressHUD mProgressHUD;
        JSONArray result_array;
        JSONObject mJsonobj;
        boolean error = false;
        String err_message = "";

        @Override
        protected void onPreExecute() {
            try {
               /* progressDialog = ProgressDialog.show(mContext, "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();*/
                mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {


            try {
                Object res_obj = JSONRPCAPI.getUserSubscription(PreferenceManager.getAccessToken(mContext));
                if (res_obj != null) {
                    if (res_obj instanceof JSONArray) {
                        result_array = (JSONArray) res_obj;
                    } else {

                        error = true;
                        JSONObject err_obj = (JSONObject) res_obj;
                        if (err_obj.has("name")) {
                            if (err_obj.getString("name").equalsIgnoreCase("JSONRPCError")) {
                                if (err_obj.has("message")) {
                                    String message = err_obj.getString("message");
                                    if (message.contains("Invalide or expired token") || message.contains("Invalid or expired token")) {
                                        err_message = message;
                                    }
                                }

                            }

                        }

                    }
                }


                if (result_array != null && result_array.length() > 0) {
                    for (int i = 0; i < result_array.length(); i++) {
                        JSONObject result_object = result_array.getJSONObject(i);
                        String code = result_object.getString("code");
                        final String image = result_object.getString("image_url");
                        String name = result_object.getString("name");
                        String slug = result_object.getString("slug");
                        final String gray_image_url = result_object.getString("gray_image_url");
                        boolean subscribed = result_object.getBoolean("subscribed");

                        Log.d("sunscribed::::", "::::::" + subscribed);

                        if (code.equalsIgnoreCase("CABLE")) {
                            iscableSelected = subscribed;

                        } else {
                            subscriptionList.add(new UserSubscriptionBean(code, image, name, slug, gray_image_url, subscribed));
                        }

                        user_map.put(code, new UserSubscriptionBean(code, image, name, slug, gray_image_url, subscribed));


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                aq.id(new ImageView(mContext)).image(image);
                                aq.id(new ImageView(mContext)).image(gray_image_url);
                            }
                        });

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (error) {
                    set_button.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setMessage("Your session has been expired because you are logged into multiple devices. Click OK to login.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                PreferenceManager.setLogin(false, getActivity());
                                PreferenceManager.setAccessToken("", getActivity());

                                PreferenceManager.setusername("", getActivity());
                                PreferenceManager.setcity("", getActivity());
                                PreferenceManager.setfirst_name("", getActivity());
                                PreferenceManager.setlast_name("", getActivity());
                                PreferenceManager.setgender("", getActivity());
                                PreferenceManager.setemail("", getActivity());
                                PreferenceManager.setstate("", getActivity());
                                PreferenceManager.setdate_of_birth("", getActivity());
                                PreferenceManager.setlast_login("", getActivity());
                                PreferenceManager.setaddress_1("", getActivity());
                                PreferenceManager.setaddress_2("", getActivity());
                                PreferenceManager.setpostal_code("", getActivity());
                                PreferenceManager.setphone_number("", getActivity());
                                PreferenceManager.setid(0, getActivity());

                                Intent in = new Intent(getActivity(), LoginActivity.class);
                                startActivity(in);
                                getActivity().finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    TextView titleView = (TextView) dialog
                            .findViewById(getResources()
                                    .getIdentifier("alertTitle", "id",
                                            "android"));

                    if (titleView != null) {
                        titleView.setGravity(Gravity.CENTER);
                    }


                } else {
                    if (result_array == null) {
                        if (!getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
                            Toast.makeText(getActivity(), "No Data Found/Session Expired", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    } else {
                        new LoadAllSubscriptionAPI().execute();
                    }

                    if (iscableSelected) {
                        switch_image.setImageResource(R.drawable.on);
                        switch_image1.setImageResource(R.drawable.on);
                    }
                }

                if (iscableSelected) {
                    switch_image.setImageResource(R.drawable.on);
                    switch_image1.setImageResource(R.drawable.on);
                }



                /*if(subscriptionList!=null&&subscriptionList.size()>0){


                    SubscriptionAdapter mAdapter=new SubscriptionAdapter(mContext,subscriptionList);
                    subscription_list.setAdapter(mAdapter);





                }
*/
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (mProgressHUD != null && mProgressHUD.isShowing()) {
                        mProgressHUD.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LoadAllSubscriptionAPI extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        ProgressHUD mProgressHUD;
        JSONArray result_array;
        JSONObject mJsonobj;
        UserSubscriptionBean ub;

        @Override
        protected void onPreExecute() {
            try {
                /*progressDialog = ProgressDialog.show(mContext, "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();*/
                mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            result_array = JSONRPCAPI.getAllUserSubscription();


            try {


                if (result_array != null && result_array.length() > 0) {
                    for (int i = 0; i < result_array.length(); i++) {
                        horizontal_list_date = new ArrayList<>();
                        JSONObject result_object = result_array.getJSONObject(i);
                        String image = "", name = "", slug = "", gray_image_url = "";
                        int nid = 0;
                        boolean subscribed = false;
                        ub = null;

                        String code = result_object.getString("code");


                        if (user_map != null && user_map.containsKey(code)) {
                            ub = user_map.get(code);
                        }
                        if (result_object.has("image_url")) {
                            image = result_object.getString("image_url");
                        }
                        if (TextUtils.isEmpty(image) && ub != null) {
                            image = ub.getImage_url();
                        }


                        name = result_object.getString("name");
                        slug = result_object.getString("slug");

                        if (result_object.has("gray_image_url")) {
                            gray_image_url = result_object.getString("gray_image_url");
                        }
                        if (TextUtils.isEmpty(gray_image_url) && ub != null) {
                            gray_image_url = ub.getGray_image_url();
                        }

                        if (result_object.has("subscribed")) {
                            subscribed = result_object.getBoolean("subscribed");
                        } else if (ub != null) {

                            subscribed = ub.isSelected();
                        }
                        if (result_object.has("id")) {
                            nid = result_object.getInt("id");
                        } else {
                            if (code.equalsIgnoreCase("HULU")) {
                                nid = 75;
                            } else if (code.equalsIgnoreCase("NETFLIX")) {
                                nid = 303;
                            } else if (code.equalsIgnoreCase("AMZN")) {
                                nid = 537;
                            } else if (code.equalsIgnoreCase("HBO_NOW")) {
                                nid = 54;
                            } else if (code.equalsIgnoreCase("SHOWTIME")) {
                                nid = 120;
                            } else if (code.equalsIgnoreCase("CBS")) {
                                nid = 17;
                            }

                        }


                        Log.d("sunscribed::::", "::::::" + subscribed);


                        if (result_object.has("movies")) {
                            JSONArray movies_array = result_object.getJSONArray("movies");
                            if (movies_array != null && movies_array.length() > 0) {
                                for (int j = 0; j < movies_array.length(); j++) {
                                    JSONObject movies_object = movies_array.getJSONObject(j);

                                    int id = 0;
                                    String itemsname = "", itemscarousel_image = "";

                                    if (movies_object.has("id")) {
                                        id = movies_object.getInt("id");
                                    }
                                    if (movies_object.has("name")) {
                                        itemsname = movies_object.getString("name");
                                    }
                                    if (movies_object.has("image")) {
                                        itemscarousel_image = movies_object.getString("image");
                                    }

                                    horizontal_list_date.add(new HorizontalListitemBean(id, itemscarousel_image, "M", itemsname));
                                }
                            }
                        }

                        if (result_object.has("shows")) {
                            JSONArray show_array = result_object.getJSONArray("shows");
                            if (show_array != null && show_array.length() > 0) {
                                for (int j = 0; j < show_array.length(); j++) {
                                    JSONObject show_object = show_array.getJSONObject(j);

                                    int id = 0;
                                    String itemsname = "", itemscarousel_image = "";

                                    if (show_object.has("id")) {
                                        id = show_object.getInt("id");
                                    }
                                    if (show_object.has("name")) {
                                        itemsname = show_object.getString("name");
                                    }
                                    if (show_object.has("image")) {
                                        itemscarousel_image = show_object.getString("image");
                                    }

                                    horizontal_list_date.add(new HorizontalListitemBean(id, itemscarousel_image, "S", itemsname));
                                }
                            }
                        }


                        if (horizontal_list_date.size() > 0) {
                            all_subscriptionList.add(new UserAllSubscriptionBean(nid, code, image, name, slug, gray_image_url, subscribed, horizontal_list_date));
                        }


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (result_array == null) {
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (iscableSelected) {
                    switch_image.setImageResource(R.drawable.on);
                    switch_image1.setImageResource(R.drawable.on);
                }


                if (all_subscriptionList != null && all_subscriptionList.size() > 0) {

//                    OnDemandContentAdapter mOnDemandContentAdapter = new OnDemandContentAdapter(all_subscriptionList, getActivity());
//                    subscription_list.setAdapter(mOnDemandContentAdapter);

                    /*SubscriptionAdapter mAdapter=new SubscriptionAdapter(mContext,subscriptionList);
                    subscription_list.setAdapter(mAdapter);*/

                    if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
                        new updateSubscriptions().execute();
                    } else {
                        OnDemandContentAdapter mOnDemandContentAdapter = new OnDemandContentAdapter(all_subscriptionList, getActivity());
                        subscription_list.setAdapter(mOnDemandContentAdapter);
                    }


                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (mProgressHUD != null && mProgressHUD.isShowing()) {
                        mProgressHUD.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.DataObjectHolder> {
        Context context;
        JSONArray jsonBuyItmes;
        ArrayList<UserSubscriptionBean> data_list;
        private String[] sub_list;

        public SubscriptionAdapter(Context context, ArrayList<UserSubscriptionBean> data_list) {
            this.context = context;
            this.data_list = data_list;
            sub_list = PreferenceManager.geSubscribedList(context);
            Log.d("sublist:::", ":::start");
            for (int i = 0; i < sub_list.length; i++) {
                Log.d("sublist:::", ":::" + sub_list[i]);
            }

        }

        @Override
        public SubscriptionAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.subscription_item, parent, false);

            return new SubscriptionAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final SubscriptionAdapter.DataObjectHolder holder, final int position) {

            String image = "";
            String grey_image = "";
            String deepLink = "";
            String app_download_link = "";
            String display_name = "";
            String app_required = "";
            String app_link = "";
            String price = "";
            try {
                image = data_list.get(position).getImage_url();
                grey_image = data_list.get(position).getGray_image_url();


                if (!TextUtils.isEmpty(image)) {

                    if (data_list.get(position).isSelected()) {
                        aq.id(holder.subscription_image).progress(holder.progressBar).image(image);
                    } else {
                        aq.id(holder.subscription_image).progress(holder.progressBar).image(grey_image);
                    }
                } else {
                    if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hulu")) {
                        if (data_list.get(position).isSelected()) {

                            holder.subscription_image.setImageResource(R.drawable.hulu_active);
                        } else {
                            holder.subscription_image.setImageResource(R.drawable.hulu);
                        }
                    } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("netflix")) {
                        if (data_list.get(position).isSelected()) {
                            holder.subscription_image.setImageResource(R.drawable.netflix_active);
                        } else {
                            holder.subscription_image.setImageResource(R.drawable.netflix);
                        }
                    } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("amzn")) {
                        if (data_list.get(position).isSelected()) {
                            holder.subscription_image.setImageResource(R.drawable.amazon_active);
                        } else {
                            holder.subscription_image.setImageResource(R.drawable.amazon);
                        }
                    } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("cbs")) {
                        if (data_list.get(position).isSelected()) {
                            holder.subscription_image.setImageResource(R.drawable.cbs_active);
                        } else {
                            holder.subscription_image.setImageResource(R.drawable.cbss);
                        }
                    } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hbo_now")) {
                        if (data_list.get(position).isSelected()) {
                            holder.subscription_image.setImageResource(R.drawable.hbo_active);
                        } else {
                            holder.subscription_image.setImageResource(R.drawable.hbo);
                        }
                    } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("showtime")) {
                        if (data_list.get(position).isSelected()) {
                            holder.subscription_image.setImageResource(R.drawable.sho_active);
                        } else {
                            holder.subscription_image.setImageResource(R.drawable.sho);
                        }
                    } else if (!TextUtils.isEmpty(image)) {
                        aq.id(holder.subscription_image).progress(holder.progressBar).image(image);
                    }

                }

                if (data_list.get(position).isSelected()) {
                    holder.tick_imageButton.setVisibility(View.VISIBLE);
                } else {
                    holder.tick_imageButton.setVisibility(View.GONE);
                }


                holder.item.setTag(position);


                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int p = Integer.parseInt(holder.item.getTag().toString());
                        if (data_list.get(p).isSelected()) {
                            holder.tick_imageButton.setVisibility(View.GONE);
                            subscriptionList.get(p).setSelected(false);
                            // setLocked(holder.subscription_image);
                        } else {
                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                            subscriptionList.get(p).setSelected(true);
                            //setUnlocked(holder.subscription_image);
                        }

                        if (!TextUtils.isEmpty(data_list.get(position).getImage_url())) {

                            if (data_list.get(position).isSelected()) {
                                aq.id(holder.subscription_image).progress(holder.progressBar).image(data_list.get(position).getImage_url());
                            } else {
                                aq.id(holder.subscription_image).progress(holder.progressBar).image(data_list.get(position).getGray_image_url());
                            }
                        } else {
                            if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hulu")) {
                                if (data_list.get(position).isSelected()) {
                                    holder.subscription_image.setImageResource(R.drawable.hulu_active);
                                } else {
                                    holder.subscription_image.setImageResource(R.drawable.hulu);
                                }
                            } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("netflix")) {
                                if (data_list.get(position).isSelected()) {
                                    holder.subscription_image.setImageResource(R.drawable.netflix_active);
                                } else {
                                    holder.subscription_image.setImageResource(R.drawable.netflix);
                                }
                            } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("amzn")) {
                                if (data_list.get(position).isSelected()) {
                                    holder.subscription_image.setImageResource(R.drawable.amazon_active);
                                } else {
                                    holder.subscription_image.setImageResource(R.drawable.amazon);
                                }
                            } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("cbs")) {
                                if (data_list.get(position).isSelected()) {
                                    holder.subscription_image.setImageResource(R.drawable.cbs_active);
                                } else {
                                    holder.subscription_image.setImageResource(R.drawable.cbss);
                                }
                            } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hbo_now")) {
                                if (data_list.get(position).isSelected()) {
                                    holder.subscription_image.setImageResource(R.drawable.hbo_active);
                                } else {
                                    holder.subscription_image.setImageResource(R.drawable.hbo);
                                }
                            } else if (data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("showtime")) {
                                if (data_list.get(position).isSelected()) {
                                    holder.subscription_image.setImageResource(R.drawable.sho_active);
                                } else {
                                    holder.subscription_image.setImageResource(R.drawable.sho);
                                }
                            }
                        }


                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return data_list.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            RelativeLayout item;
            ImageView tick_imageButton;
            GridViewItem subscription_image;
            ProgressBar progressBar;

            public DataObjectHolder(View itemView) {
                super(itemView);
                item = (RelativeLayout) itemView.findViewById(R.id.item);
                tick_imageButton = (ImageView) itemView.findViewById(R.id.tick_imageButton);
                subscription_image = (GridViewItem) itemView.findViewById(R.id.subscription_image);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            }
        }
    }

    public static void setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        //v.setAlpha(128);   // 128 = 0.5
    }

    public static void setUnlocked(ImageView v) {
        v.setColorFilter(null);
        //v.setAlpha(255);
    }

    class OnDemandContentAdapter extends RecyclerView.Adapter<OnDemandContentAdapter.DataObjectHolder> {
        ArrayList<UserAllSubscriptionBean> list_data;
        Context context;
        private int mSelectedItem = 0;

        public OnDemandContentAdapter(ArrayList<UserAllSubscriptionBean> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
        }


        @Override
        public OnDemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_item, parent, false);
            return new OnDemandContentAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final OnDemandContentAdapter.DataObjectHolder holder, final int position) {
            try {
                slider_image_width = (int) (list_width);
                holder.horizontal_listview_title.setText(Utils.stripHtml(list_data.get(position).getName()));
                holder.network_imageView.setTag(position);
                holder.installed_textView.setTag(position);

                String image_url = "";
                if (list_data.get(position).isSelected()) {
                    image_url = list_data.get(position).getImage_url();
                } else {
                    image_url = list_data.get(position).getGray_image_url();
                }


                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;

                LinearLayout.LayoutParams ip;
                LinearLayout.LayoutParams tp;

                ip = new LinearLayout.LayoutParams((width / 100) * 10, LinearLayout.LayoutParams.WRAP_CONTENT);

//                if (list_data.get(position).getData_list().get(0).getType().equalsIgnoreCase("s")) {
//                    ip = new LinearLayout.LayoutParams(((slider_image_width - 20) / 3) - 50, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    tp = new LinearLayout.LayoutParams(((slider_image_width - 20) / 3) - 50, 50);
//
//                } else {
//                    ip = new LinearLayout.LayoutParams(((slider_image_width - 20) / 2) - 50, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    tp = new LinearLayout.LayoutParams(((slider_image_width - 20) / 2) - 50, 50);
//                }


                holder.network_imageView.setLayoutParams(ip);
//                holder.installed_textView.setLayoutParams(tp);
                holder.installed_textView.getLayoutParams().width = holder.network_imageView.getLayoutParams().width;
                holder.tick_imageButton.getLayoutParams().height = (width / 100) * 3;
                holder.tick_imageButton.getLayoutParams().width = (width / 100) * 3;


                Log.d("network image:::", ":::url:::" + image_url);
                if (!TextUtils.isEmpty(image_url)) {
                    aq.id(holder.network_imageView).image(image_url);
                }

                try {
                    holder.dynamic_image_layout.removeAllViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (list_data.get(position).isSelected()) {
                    holder.installed_textView.setVisibility(View.VISIBLE);
                    holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
                    holder.installed_textView.setText("Subscribed");
                    holder.tick_imageButton.setVisibility(View.VISIBLE);
                } else {
                    holder.installed_textView.setVisibility(View.VISIBLE);
                    holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
                    holder.installed_textView.setText("UnSubscribed");
                    holder.tick_imageButton.setVisibility(View.GONE);
                }
                holder.installed_textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(holder.installed_textView.getTag().toString());
                        list_data.get(pos).setSelected(!list_data.get(pos).isSelected());

                        if (list_data.get(position).isSelected()) {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
                            holder.installed_textView.setText("Subscribed");
                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(list_data.get(pos).getImage_url())) {
                                aq.id(holder.network_imageView).image(list_data.get(pos).getImage_url());
                            }
                        } else {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
                            holder.installed_textView.setText("UnSubscribed");
                            holder.tick_imageButton.setVisibility(View.GONE);
                            if (!TextUtils.isEmpty(list_data.get(pos).getGray_image_url())) {
                                aq.id(holder.network_imageView).image(list_data.get(pos).getGray_image_url());
                            }
                        }
                    }
                });
                holder.network_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(holder.network_imageView.getTag().toString());
                        list_data.get(pos).setSelected(!list_data.get(pos).isSelected());

                        if (list_data.get(position).isSelected()) {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
                            holder.installed_textView.setText("Subscribed");
                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(list_data.get(pos).getImage_url())) {
                                aq.id(holder.network_imageView).image(list_data.get(pos).getImage_url());
                            }
                        } else {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
                            holder.installed_textView.setText("UnSubscribed");
                            holder.tick_imageButton.setVisibility(View.GONE);
                            if (!TextUtils.isEmpty(list_data.get(pos).getGray_image_url())) {
                                aq.id(holder.network_imageView).image(list_data.get(pos).getGray_image_url());
                            }
                        }
                    }
                });


                for (int l = 0; l < list_data.get(position).getData_list().size(); l++) {
                    final int ll = l;

                    final View itemmlayout = (View) mlayoutinflater.inflate(R.layout.horizontal_list_item, null);
                    itemmlayout.setTag(position);

                    DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                    LinearLayout.LayoutParams vp;
                    if (checkIsTablet()) {
                        vp = new LinearLayout.LayoutParams((slider_image_width - 40) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                        if (l != 0) {
                            vp.setMargins(20, 0, 0, 0);
                        }
                    } else {

                        if (list_data.get(position).getData_list().get(l).getType().equalsIgnoreCase("m")) {
                            vp = new LinearLayout.LayoutParams((slider_image_width - 20) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                        } else {
                            vp = new LinearLayout.LayoutParams((slider_image_width - 20) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);


                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                        }

                    }


                    String image_url_scroll = list_data.get(position).getData_list().get(l).getImage();
                    Log.d("image_url::", "image_url" + image_url);
                    horizontal_imageView.loadImage(image_url_scroll);
                    horizontal_imageView.setLayoutParams(vp);
                    final int finalL = l;
                    itemmlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = Integer.parseInt(itemmlayout.getTag().toString());
                            if (!list_data.get(pos).isSelected()) {
                               /* FragmentManager fm = getActivity().getSupportFragmentManager();
                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+list_data.get(position).getNetwork_name(),""+list_data.get(position).getNetwork_image(),list_data.get(position).getNetwork_link());

                                dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");*/
                            } else {
                                HomeActivity.toolbarSubContent = list_data.get(position).getData_list().get(pos).getName();
                                String type = list_data.get(position).getData_list().get(pos).getType();
                                int id = list_data.get(position).getData_list().get(pos).getId();
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                if (type.equalsIgnoreCase("m") || type.equalsIgnoreCase("movie")) {
                                    FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, id);
                                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                    fragmentTransaction.commit();
                                } else {
                                    FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, id);
                                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                    fragmentTransaction.commit();
                                }

                            }

                        }
                    });
                    holder.dynamic_image_layout.addView(itemmlayout);

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
            HorizontalScrollView horizontal_listview;
            TextView horizontal_listview_title, view_all_text, installed_textView;
            LinearLayout recycler_layout, image_view, dynamic_image_layout;
            ImageView left_slide, right_slide, tick_imageButton;
            GridViewItem network_imageView;
            LinearLayout network_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                network_imageView = (GridViewItem) itemView.findViewById(R.id.network_imageViewv);
                left_slide = (ImageView) itemView.findViewById(R.id.left_slide);
                right_slide = (ImageView) itemView.findViewById(R.id.right_slide);
                tick_imageButton = (ImageView) itemView.findViewById(R.id.tick_imageButton);

                installed_textView = (TextView) itemView.findViewById(R.id.installed_textView);
                view_all_text = (TextView) itemView.findViewById(R.id.view_all_text);
                horizontal_listview_title = (TextView) itemView.findViewById(R.id.horizontal_listview_title);

                dynamic_image_layout = (LinearLayout) itemView.findViewById(R.id.dynamic_image_layout);
                recycler_layout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);
                network_layout = (LinearLayout) itemView.findViewById(R.id.network_layout);
                horizontal_listview = (HorizontalScrollView) itemView.findViewById(R.id.horizontal_listview);

            }
        }
    }

    private boolean checkIsTablet() {

        if (getActivity() != null) {
            try {
                String inputSystem;
                inputSystem = android.os.Build.ID;
                Log.d(":::", inputSystem);
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();  // deprecated
                Log.d("::::", width + "");
                Log.d("::::", height + "");
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(width / dm.xdpi, 2);
                double y = Math.pow(height / dm.ydpi, 2);
                double screenInches = Math.sqrt(x + y);
                Log.d(":::::", "Screen inches : " + screenInches + "");
                return screenInches > 6.0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        return false;
    }

    private class LoadNetworkData extends AsyncTask<String, Object, Object> {

        ArrayList<SliderBean> slider_list = new ArrayList<>();
        private ArrayList<CauroselsItemBean> listdataShow = new ArrayList<>();
        private ArrayList<CauroselsItemBean> listdataMovies = new ArrayList<>();
        ArrayList<HorizontalListitemBean> horizontalListBeans;
        int id;
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                subscription_show_list.setVisibility(View.GONE);
                mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                String code = params[0];

                //JSONArray carousel_array = JSONRPCAPI.getTVNetworkList(g_id, 100, 0, 0);
                JSONObject response_object = JSONRPCAPI.getUserSubscriptionbyCode(code);
                listdataShow = parseArray(response_object, "S");
                listdataMovies = parseArray(response_object, "M");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (listdataShow.size() > 0) {
                    subscription_show_list.setVisibility(View.VISIBLE);
                    GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataShow, getActivity());
                    subscription_show_list.setAdapter(viewAll_adpater);
                    displayShowsTab();
                } else {
                    try {
                        GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataShow, getActivity());
                        subscription_show_list.setAdapter(viewAll_adpater);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (listdataMovies.size() > 0) {
                    subscription_Movie_list.setVisibility(View.GONE);
                    GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataMovies, getActivity());
                    subscription_Movie_list.setAdapter(viewAll_adpater);
                    if (listdataShow.size() == 0) {
                        displayMoviesTab();
                    }
                } else {
                    try {
                        GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataMovies, getActivity());
                        subscription_Movie_list.setAdapter(viewAll_adpater);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mProgressHUD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<CauroselsItemBean> parseArray(JSONObject carousel_array, String type) {
        ArrayList<CauroselsItemBean> parse_list = new ArrayList<>();
        JSONArray show_array;

        try {
            if (carousel_array != null) {
                Log.d("carousel_array::", "::" + carousel_array);
                if (type.equalsIgnoreCase("s")) {
                    show_array = carousel_array.getJSONArray("shows");
                } else {
                    show_array = carousel_array.getJSONArray("movies");
                }


                if (show_array != null && show_array.length() > 0) {
                    for (int i = 0; i < show_array.length(); i++) {
                        int viewAll_id = 0;
                        String viewAll_title = "", viewAll_type = "", viewAll_image = "";
                        JSONObject object = show_array.getJSONObject(i);
                        if (object.has("id")) {
                            viewAll_id = object.getInt("id");
                        }

                        if (object.has("title")) {
                            viewAll_title = object.getString("title");
                            Log.d("title title::", ":::" + viewAll_title);
                        }
                        if (TextUtils.isEmpty(viewAll_title)) {
                            if (object.has("name")) {
                                viewAll_title = object.getString("name");
                                Log.d("name ::", ":::" + viewAll_title);
                            }
                        }
                        if (object.has("type")) {
                            viewAll_type = object.getString("type");
                        }
                        if (object.has("image")) {
                            viewAll_image = object.getString("image");
                        } else if (object.has("carousel_image")) {
                            viewAll_image = object.getString("carousel_image");
                        }

                        parse_list.add(new CauroselsItemBean(viewAll_image, type, viewAll_id, viewAll_title));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parse_list;
    }

    public class GridShowAdapter extends RecyclerView.Adapter<GridShowAdapter.DataObjectHolder> {
        ArrayList<CauroselsItemBean> viewAll_list_data;
        Context context;


        public GridShowAdapter(ArrayList<CauroselsItemBean> viewAll_list_data, Context context) {
            try {
                this.viewAll_list_data = viewAll_list_data;
                this.context = context;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public GridShowAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new GridShowAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final GridShowAdapter.DataObjectHolder holder, final int position) {
            try {
                final String image_url = viewAll_list_data.get(position).getCarousel_image();
                final String type = viewAll_list_data.get(position).getType();

                if (type.equalsIgnoreCase("n")) {
                    holder.gridview_item.setVisibility(View.VISIBLE);
                    holder.imageView.setVisibility(View.GONE);
                    if (image_url != null) {
                        Picasso.with(context
                                .getApplicationContext())
                                .load(image_url)
                                .fit()
                                .placeholder(R.drawable.loader_network).into(holder.gridview_item);
                    }

                } else if (type.equalsIgnoreCase("M")) {
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (image_url != null) {
                        holder.imageView.loadMovieImage(image_url);
                    }
                } else {
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (image_url != null) {
                        holder.imageView.loadImage(image_url);
                    }
                }

                Log.d("categoryimage", "===>" + image_url);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HomeActivity.toolbarSubContent = viewAll_list_data.get(holder.getAdapterPosition()).getName();
                        int id = viewAll_list_data.get(holder.getAdapterPosition()).getId();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, id);
                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                        fragmentTransaction.commit();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return viewAll_list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private final DynamicImageView imageView;
            GridViewItem gridview_item;

            public DataObjectHolder(View itemView) {
                super(itemView);


                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
                gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);

            }
        }
    }

    private void displayShowsTab() {
        view_shows_left.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        view_shows_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        view_movies_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        view_movies_right.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        shows_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        movies_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.white));

        view_shows_bottom.setVisibility(View.GONE);
        view_movies_bottom.setVisibility(View.VISIBLE);

        subscription_Movie_list.setVisibility(View.GONE);
        subscription_show_list.setVisibility(View.VISIBLE);
    }

    private void displayMoviesTab() {
        view_shows_left.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        view_shows_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        view_movies_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        view_movies_right.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        shows_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        movies_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.white));

        view_shows_bottom.setVisibility(View.VISIBLE);
        view_movies_bottom.setVisibility(View.GONE);
        subscription_Movie_list.setVisibility(View.VISIBLE);
        subscription_show_list.setVisibility(View.GONE);
    }

}
