package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.selecttvlauncher.BeanClass.SliderBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ${Madhan} on 5/20/2016.
 */
public class FragmentMovieViewPagerItem extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView fragment_ondemand_all_pagercontent_heading;
    TextView fragment_ondemand_all_pagercontent_subheading;
    TextView fragment_ondemand_all_pagercontent_caption;
    TextView fragment_ondemand_all_pagercontent_watchnow_text;
    DynamicImageView slider_image;
    int c_id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static ArrayList<SliderBean> slider_list = new ArrayList<>();
    private String list;
    private String pos;
    private JSONObject m_jsonSlideEntity;
    private JSONArray mSliderArray;
    private AQuery aq;
    // TODO: Rename and change types and number of parameters
    public static FragmentMovieViewPagerItem newInstance(String param1, String param2) {
        FragmentMovieViewPagerItem fragment = new FragmentMovieViewPagerItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public FragmentMovieViewPagerItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getString(ARG_PARAM1);
            pos = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewpager_item, container, false);

        try {
            if (getArguments() != null) {
                c_id = getArguments().getInt("msg");
            }
            aq=new AQuery(getActivity());

            fragment_ondemand_all_pagercontent_subheading = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_subheading);
            fragment_ondemand_all_pagercontent_heading = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_heading);
            fragment_ondemand_all_pagercontent_caption = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_caption);
            fragment_ondemand_all_pagercontent_watchnow_text = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_watchnow_text);
            slider_image = (DynamicImageView) v.findViewById(R.id.slider_image);
            Utilities.setViewFocus(getActivity(),fragment_ondemand_all_pagercontent_watchnow_text);

            Typeface OpenSans_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
            Typeface OpenSans_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");

            fragment_ondemand_all_pagercontent_heading.setTypeface(OpenSans_bold);
            fragment_ondemand_all_pagercontent_subheading.setTypeface(OpenSans_regular);
            fragment_ondemand_all_pagercontent_caption.setTypeface(OpenSans_regular);
            fragment_ondemand_all_pagercontent_watchnow_text.setTypeface(OpenSans_regular);

            try
            {
                int id = 0;
                String name = "", image = "", description = "",etype="";
                String title = null;
                if(HomeActivity.isPayperview.equals("Pay Per View")) {

                    mSliderArray = HomeActivity.m_jsonSlider;
                    Log.d("adapter::::","Pay Per View"+mSliderArray.toString());
                }else{

                    mSliderArray= HomeActivity.m_jsonMovieSlider;
                    Log.d("adapter::::","No Pay Per View"+mSliderArray.toString());
                }

                slider_list=new ArrayList<>();
                for (int i = 0; i < mSliderArray.length(); i++) {
                    JSONObject slider_object = mSliderArray.getJSONObject(i);
                    if (slider_object.has("description")) {
                        description = slider_object.getString("description");
                    }
                    if (slider_object.has("title")) {
                        title = slider_object.getString("title");
                    }
                    if (slider_object.has("id")) {
                        id = slider_object.getInt("id");
                    }
                    if (slider_object.has("image")) {
                        image = slider_object.getString("image");
                    }
                    if (slider_object.has("name")) {
                        name = slider_object.getString("name");
                    }
                    if (slider_object.has("type")) {
                        etype= slider_object.getString("type");
                    }

                    slider_list.add(new SliderBean(id, description, title, image, name,etype));

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            fragment_ondemand_all_pagercontent_heading.setText(slider_list.get(Integer.parseInt(pos)).getName());
            Log.d("adapter::::","::"+slider_list.get(Integer.parseInt(pos)).getName());
            fragment_ondemand_all_pagercontent_subheading.setText(slider_list.get(Integer.parseInt(pos)).getTitle());
            fragment_ondemand_all_pagercontent_caption.setText(slider_list.get(Integer.parseInt(pos)).getDescription());
            //slider_image.loadSliderImage(slider_list.get(Integer.parseInt(pos)).getImage());
            try {
                aq.id(slider_image).image(slider_list.get(Integer.parseInt(pos)).getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragment_ondemand_all_pagercontent_watchnow_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.toolbarMainContent="Movies";
                    HomeActivity.toolbarSubContent=slider_list.get(Integer.parseInt(pos)).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent,"");

                    new GetSliderEntity().execute(String.valueOf(slider_list.get(Integer.parseInt(pos)).getId()));
                       /* ((HomeActivity)getActivity()).SwapMovieFragment(true);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMovieMain  fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, slider_list.get(c_id).getId());
                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                        fragmentTransaction.commit();*/

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


            /*fragment_ondemand_all_pagercontent_subheading
                    fragment_ondemand_all_pagercontent_heading.setTypeface(OpenSans_Bold);
            fragment_ondemand_all_pagercontent_caption.setTypeface();*/
        return v;
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


    public class GetSliderEntity extends AsyncTask<String, Object, Object> {




        @Override
        protected String doInBackground(String... params) {
            try {
                int nIndex = 1;
                m_jsonSlideEntity = JSONRPCAPI.getMovieSlideEntity(Integer.parseInt(params[0]));
                if (m_jsonSlideEntity  == null) return null;
                Log.d("Show::", "Selected Slider list::" + m_jsonSlideEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            try {


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Object params) {
            try {


                ((HomeActivity)getActivity()).mMovieSlidertodeatails(m_jsonSlideEntity);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}



