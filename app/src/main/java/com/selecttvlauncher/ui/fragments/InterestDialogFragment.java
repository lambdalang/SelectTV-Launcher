package com.selecttvlauncher.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Utilities;


public class InterestDialogFragment extends android.support.v4.app.DialogFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnDialogFragmentInteractionListener mListener;
    private ImageView close_imageButton;

    public InterestDialogFragment() {
        // Required empty public constructor
    }


    public static InterestDialogFragment newInstance(String param1, String param2) {
        InterestDialogFragment fragment = new InterestDialogFragment();
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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_dialog_main, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        close_imageButton=(ImageView)view.findViewById(R.id.close_imageButton);

        Utilities.setViewFocus(getActivity(), close_imageButton);
        try {
            final int selected_pos=Integer.parseInt(mParam1);

            view.post(new Runnable() {
                @Override
                public void run() {
                    if (selected_pos == 2) {
                        showGenreDialog();
                    } else if (selected_pos == 3) {
                        showChannelDialog();
                    } else if (selected_pos == 4) {
                        showNetworkDialog();
                    } else {
                        showGenreDialog();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        close_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getDialog().dismiss();
                    mListener.onDialogFragmentInteraction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
    private void showNetworkDialog() {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            InterestNetworkDialogFragment mInterestNetworkDialogFragment = new InterestNetworkDialogFragment();
            fragmentTransaction.replace(R.id.fragment_dialogs, mInterestNetworkDialogFragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showGenreDialog() {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            InterestGenreDialogFragment mInterestGenreDialogFragment = new InterestGenreDialogFragment();
            fragmentTransaction.replace(R.id.fragment_dialogs, mInterestGenreDialogFragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showChannelDialog() {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            InterestChannelsFragment mInterestChannelsFragment = new InterestChannelsFragment();
            fragmentTransaction.replace(R.id.fragment_dialogs, mInterestChannelsFragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDialog() {
        getDialog().dismiss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogFragmentInteractionListener) {
            mListener = (OnDialogFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnDialogFragmentInteractionListener {
        void onDialogFragmentInteraction();
    }
}
