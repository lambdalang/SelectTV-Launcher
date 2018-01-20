package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.selecttvlauncher.R;


public class FragmentListenMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int mListenNetworkId;
    public static int mListenViewallId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
public static String mListenMainContent="";
public static String mListenContent="";
    public static String mListenSubContent="";
    public static String mListentempSubContent="";
public static String mRadioMainContent="";
public static String mRadioSubContent="";
public static String mRadioDetailsContent="";
public static FrameLayout fragment_listen_pagerandlist;

    public static boolean isdestryed=true;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListenMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListenMain newInstance(String param1, String param2) {
        FragmentListenMain fragment = new FragmentListenMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListenMain() {
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
        View view=inflater.inflate(R.layout.fragment_fragment_listen_main, container, false);
        fragment_listen_pagerandlist=(FrameLayout)view.findViewById(R.id.fragment_listen_pagerandlist);
        isdestryed=true;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentListenList fragmentHomeScreenGrid = new FragmentListenList();
        fragmentTransaction.replace(R.id.fragment_listen_alllist, fragmentHomeScreenGrid);
        fragmentTransaction.commit();
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
    public void onDestroy() {
        super.onDestroy();
        isdestryed=false;
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
