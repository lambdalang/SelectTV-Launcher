package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.SubScriptionListBean;
import com.selecttvlauncher.BeanClass.SubScriptionsubList;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentSubScriptionHorizontalList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LayoutInflater inflate;
    private int spacing;
    private int width;
    private int height;
    private LinearLayout dynamic_subscription_horizontalViews_layout;
    private String name;
    public static String list = "";
    public static int mposition = 0;
    private ArrayList<SubScriptionsubList> mSubscriptionSubList = new ArrayList<>();
    private ArrayList<SubScriptionListBean> mSubscriptiondataList = new ArrayList<>();

    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSubScriptionHorizontalList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSubScriptionHorizontalList newInstance(String param1, String param2) {
        FragmentSubScriptionHorizontalList fragment = new FragmentSubScriptionHorizontalList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSubScriptionHorizontalList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_sub_scription_horizontal_list, container, false);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        dynamic_subscription_horizontalViews_layout = (LinearLayout) view.findViewById(R.id.dynamic_subscription_horizontalViews_layout);
        try {
            dynamic_subscription_horizontalViews_layout.removeAllViews();
            mSubscriptionSubList = HomeActivity.subScriptionBeans.get(Integer.parseInt(list)).getSubScriptionSubLists();

            for (int i = 0; i < mSubscriptionSubList.size(); i++) {
                addlayouts(dynamic_subscription_horizontalViews_layout, mSubscriptionSubList.get(i).getName(), mSubscriptionSubList.get(i).getSubScriptionSubLists(), i);

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


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

    @Override
    public void onResume() {
        super.onResume();

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

    private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final ArrayList<SubScriptionListBean> listData, final int position) {
        final View itemlayout = (View) inflate.inflate(R.layout.horizontal_test, null);
        final TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
        TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
        horizontal_listview_title.setTypeface(OpenSans_Regular);
        view_all_text.setTypeface(OpenSans_Regular);
        final HorizontalScrollView horizontal_listview = (HorizontalScrollView) itemlayout.findViewById(R.id.horizontal_listview);
        final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
        final LinearLayout image_view = (LinearLayout) itemlayout.findViewById(R.id.image_view);

        ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
        ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);


        horizontal_listview_title.setText(Utilities.stripHtml(name));


        final LinearLayoutManager linear_layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        int spanCount = listData.size(); // 3 columns
        spacing = 20; // 50px

        boolean includeEdge = false;
        int layoutwidth = recycler_layout.getLayoutParams().width;
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);
        Log.d("listData::", "size" + listData.size());

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    height = recycler_layout.getMeasuredHeight();


                    for (int l = 0; l < listData.size(); l++) {
                        final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                        DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                        LinearLayout.LayoutParams vp;
                        if(listData.get(l).getType().equalsIgnoreCase("l")||listData.get(l).getType().equalsIgnoreCase("m"))
                        {
                             vp = new LinearLayout.LayoutParams((width - 80) / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }else {
                            vp = new LinearLayout.LayoutParams((width - 60) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);

                        }



                        if (l != 0) {
                            vp.setMargins(20, 0, 0, 0);
                        }
                        String image_url = listData.get(l).getImage();
                        Log.d("image_url::", "image_url" + image_url);
                        horizontal_imageView.loadImage(image_url);
                        horizontal_imageView.setLayoutParams(vp);
                        final int finalL = l;
                        itemmlayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("subscription", "id" + listData.get(finalL).getId());
                                if (getActivity() != null) {
                                    if (listData.get(finalL).getType().equalsIgnoreCase("n") || listData.get(finalL).getType().equalsIgnoreCase("l")) {
                                        new LoadingTVNetworkList().execute(String.valueOf(listData.get(finalL).getId()));
                                    } else if (listData.get(finalL).getType().equalsIgnoreCase("s")) {
                                        FragmentSubScriptionList.sSelectedMenu = "";
                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, listData.get(finalL).getId());
                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                        fragmentTransaction.commit();
                                    } else if (listData.get(finalL).getType().equalsIgnoreCase("m")) {
                                        FragmentSubScriptionList.sSelectedMenu = "";
                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, listData.get(finalL).getId());
                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                        fragmentTransaction.commit();
                                    }

                                }
                            }
                        });
                        itemmlayout.setFocusable(true);
                        Utilities.setViewFocus(getActivity(),itemmlayout);
                        image_view.addView(itemmlayout);

                    }


                    Log.d("layoutwidth::", "width" + width);

                }
            });

            Utilities.setViewFocus(getActivity(),right_slide);
            Utilities.setViewFocus(getActivity(),left_slide);
            Utilities.setTextFocus(getActivity(),view_all_text);


            right_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("Scroll x position::", ":::" + horizontal_listview.getScrollX());
                        horizontal_listview.post(new Runnable() {
                            public void run() {
                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() + width + 20, horizontal_listview.getScrollY());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            left_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("Scroll x position::", ":::" + horizontal_listview.getScrollX());
                        horizontal_listview.post(new Runnable() {
                            public void run() {
                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() - width - 20, horizontal_listview.getScrollY());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            view_all_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentSubScriptionList.sSelectedMenu = "";
                    mposition = position;
                    FragmentSubScriptions.sContent_position = Constants.SUBSCRIPTION_SUBCONTENT;
                    HomeActivity.toolbarSubContent = name;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentSubscriptionsGrid fragmentSubscriptionsGrid = FragmentSubscriptionsGrid.newInstance(list, String.valueOf(position));
                    fragmentTransaction_list.replace(R.id.fragment_subscription_content, fragmentSubscriptionsGrid);
                    fragmentTransaction_list.commit();
                    
                    
                    changeMenulist(Integer.parseInt(list),true,position);
                    
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }

    private void changeMenulist(int position, boolean b, int s) {
        ((HomeActivity) getActivity()).changeSubscriptionList(position, b,s);

    }

    private void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }


    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;
        int id;
        String name, poster_url, description;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            dynamic_subscription_horizontalViews_layout.setVisibility(View.GONE);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0, 2);
                if (m_jsonNetworkList  == null) return null;
                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
                HomeActivity.mSubscriptiondataList.clear();
                if(m_jsonNetworkList.length()>0){
                    for (int i = 0; i < m_jsonNetworkList.length(); i++) {
                        try {
                            JSONObject jSubscriptionCategory = m_jsonNetworkList.getJSONObject(i);

                            if (jSubscriptionCategory.has("id")) {
                                id = jSubscriptionCategory.getInt("id");
                            }
                            if (jSubscriptionCategory.has("name")) {
                                name = jSubscriptionCategory.getString("name");
                            }
                            if (jSubscriptionCategory.has("description")) {
                                description = jSubscriptionCategory.getString("description");
                            }

                            if (jSubscriptionCategory.has("poster_url")) {
                                poster_url = jSubscriptionCategory.getString("poster_url");
                            }
                            HomeActivity.mSubscriptiondataList.add(new SubScriptionListBean(id, name, poster_url, description));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar.setVisibility(View.GONE);
                if (m_jsonNetworkList.length() > 0) {
                    FragmentSubScriptionList.sSelectedMenu = Constants.NETWORK;
                    FragmentSubScriptions.sContent_position = Constants.SUBSCRIPTION_SUBCONTENT;
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentSubscriptionsGrid fragmentSubscriptionsGrid = FragmentSubscriptionsGrid.newInstance(list, String.valueOf(mposition));
                    fragmentTransaction_list.replace(R.id.fragment_subscription_content, fragmentSubscriptionsGrid);
                    fragmentTransaction_list.commit();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }
}
