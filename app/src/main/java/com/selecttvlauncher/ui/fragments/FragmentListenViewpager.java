package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.SliderCommonBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import java.util.ArrayList;
import java.util.Timer;


public class FragmentListenViewpager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView fragment_ondemand_all_pagercontent_subheading;
    private TextView fragment_ondemand_all_pagercontent_heading;
    private TextView fragment_ondemand_all_pagercontent_caption;
    private DynamicImageView slider_image;
    private TextView fragment_ondemand_all_pagercontent_watchnow_text;
    private ArrayList<SliderCommonBean> slider_list=new ArrayList<>();
    private Timer timer;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListenViewpager.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListenViewpager newInstance(String param1, String param2) {
        FragmentListenViewpager fragment = new FragmentListenViewpager();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListenViewpager() {
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
        View view=inflater.inflate(R.layout.fragment_fragment_listen_viewpager, container, false);
        try {
            /*if (getArguments() != null) {
                c_id = getArguments().getInt("msg");
            }
*/

            fragment_ondemand_all_pagercontent_subheading = (TextView) view.findViewById(R.id.fragment_ondemand_all_pagercontent_subheading);
            fragment_ondemand_all_pagercontent_heading = (TextView) view.findViewById(R.id.fragment_ondemand_all_pagercontent_heading);
            fragment_ondemand_all_pagercontent_caption = (TextView) view.findViewById(R.id.fragment_ondemand_all_pagercontent_caption);
            fragment_ondemand_all_pagercontent_watchnow_text = (TextView) view.findViewById(R.id.fragment_ondemand_all_pagercontent_watchnow_text);
            slider_image = (DynamicImageView) view.findViewById(R.id.slider_image);

            Typeface OpenSans_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
            Typeface OpenSans_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");

            fragment_ondemand_all_pagercontent_heading.setTypeface(OpenSans_bold);
            fragment_ondemand_all_pagercontent_subheading.setTypeface(OpenSans_regular);
            fragment_ondemand_all_pagercontent_caption.setTypeface(OpenSans_regular);
            fragment_ondemand_all_pagercontent_watchnow_text.setTypeface(OpenSans_regular);

            /*try
            {
                int id = 0;
                String name = "", image = "", description = "";
                String title = null;
                for (int i = 0; i < HomeActivity.m_jsonHomeSlider.length(); i++) {
                    JSONObject slider_object = HomeActivity.m_jsonHomeSlider.getJSONObject(i);
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

                    slider_list.add(new SliderBean(id, description, title, image, name));

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
*/

            fragment_ondemand_all_pagercontent_heading.setText(HomeActivity.slider_list.get(Integer.parseInt(mParam2)).getName());
            fragment_ondemand_all_pagercontent_subheading.setText(HomeActivity.slider_list.get(Integer.parseInt(mParam2)).getTitle());
            fragment_ondemand_all_pagercontent_caption.setText(HomeActivity.slider_list.get(Integer.parseInt(mParam2)).getDescription());
            slider_image.loadSliderImage(HomeActivity.slider_list.get(Integer.parseInt(mParam2)).getImage());

            Utilities.setViewFocus(getActivity(),fragment_ondemand_all_pagercontent_watchnow_text);

            fragment_ondemand_all_pagercontent_watchnow_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.toolbarMainContent="TV Shows";
                    HomeActivity.toolbarSubContent= HomeActivity.slider_list.get(Integer.parseInt(mParam2)).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

//                    new GetSliderEntity().execute(String.valueOf(HomeActivity.slider_list.get(Integer.parseInt(mParam2)).getId()));
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private class GetSliderEntity extends AsyncTask<Object,Object,Object>{
        @Override
        protected Object doInBackground(Object... params) {
            return null;
        }
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


}
