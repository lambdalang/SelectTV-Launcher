package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InterestLeftFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnInterestLeftFragmentInteractionListener mListener;

    private CircleImageView profile_imageView;
    private TextView name_textView,exp_date_textView,exp_date_value_textView,myInterest_textView,personalize_textview,addRemove_text;
    private RecyclerView fragment_Interest_list,fragment_Interest_Sublist;
    private ArrayList<String> data=new ArrayList<>();
    private ImageView back_imageView;

    public InterestLeftFragment() {
        // Required empty public constructor
    }


    public static InterestLeftFragment newInstance(String param1, String param2) {
        InterestLeftFragment fragment = new InterestLeftFragment();
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
        View view = inflater.inflate(R.layout.fragment_interest_left, container, false);


        personalize_textview=(TextView)view.findViewById(R.id.personalize_textview);
        addRemove_text=(TextView)view.findViewById(R.id.addRemove_text);
        fragment_Interest_list=(RecyclerView)view.findViewById(R.id.fragment_Interest_list);
        fragment_Interest_Sublist=(RecyclerView)view.findViewById(R.id.fragment_Interest_Sublist);
        back_imageView=(ImageView)view.findViewById(R.id.back_imageView);


        data.add("TV Shows");
        data.add("Movies");
        data.add("Movies Genres");
        data.add("Channels");
        data.add("TV Networks");
        data.add("Video Libraries");

        Utilities.setViewFocus(getActivity(), back_imageView);
        Utilities.setTextFocus(getActivity(), personalize_textview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragment_Interest_list.setLayoutManager(linearLayoutManager);
        fragment_Interest_list.setHasFixedSize(true);

        HomeActivity.toolbarMainContent = data.get(0);
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");

        ListAdapter ladapter = new ListAdapter(data, getActivity());
        fragment_Interest_list.setAdapter(ladapter);

        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.menu_position.equalsIgnoreCase(Constants.MAIN_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.MAIN_CONTENT)) {
                    Log.d("====>", "Go to home page");

                    ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                    fragmentTransaction.commit();
                }
            }
        });

        personalize_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPersonalizeDialog();
            }
        });


        return view;
    }

    private void showPersonalizeDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InterestDialogFragment dialogFragment = InterestDialogFragment.newInstance(""+0,"");
        dialogFragment.show(fm.beginTransaction(), "dialogFragment");
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onInterestLeftFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInterestLeftFragmentInteractionListener) {
            mListener = (OnInterestLeftFragmentInteractionListener) context;
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

    public interface OnInterestLeftFragmentInteractionListener {
        void onInterestLeftFragmentInteraction(Uri uri);
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.DataObjectHolder> {
        ArrayList<String> list;
        Context context;
        int mlistPosition = 0;

        public ListAdapter(ArrayList<String> list, Context context) {
            this.list= list;
            this.context = context;
        }

        @Override
        public ListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ListAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(list.get(position));
            if (position == mlistPosition) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }
            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistPosition = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();
                    HomeActivity.toolbarMainContent = list.get(position);
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    InterestRightFragment mInterestRightFragment= InterestRightFragment.newInstance(""+position,"");
                    fragmentTransaction.replace(R.id.fragment_interest_right, mInterestRightFragment);
                    fragmentTransaction.commit();
                }
            });
            holder.fragment_ondemandlist_items_layout.setFocusable(true);
            Utilities.setViewFocus(context, holder.fragment_ondemandlist_items_layout);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
                Utilities.setViewFocus(getActivity(),fragment_ondemandlist_items_layout);
            }
        }
    }
}
