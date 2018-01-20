package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CircleImageView profile_imageView;
    private TextView name_textView,exp_date_textView,exp_date_value_textView;
    private RecyclerView fragment_account_list;
    private ArrayList<String> data=new ArrayList<>();

    private OnAccountFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        profile_imageView=(CircleImageView)view.findViewById(R.id.profile_image);
        name_textView=(TextView)view.findViewById(R.id.name_textView);
        exp_date_textView=(TextView)view.findViewById(R.id.exp_date_textView);
        exp_date_value_textView=(TextView)view.findViewById(R.id.exp_date_value_textView);
        fragment_account_list=(RecyclerView)view.findViewById(R.id.fragment_account_list);

        name_textView.setText(PreferenceManager.getfirst_name(getActivity())+" "+PreferenceManager.getlast_name(getActivity()));


        data.add("My Account");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragment_account_list.setLayoutManager(linearLayoutManager);
        fragment_account_list.setHasFixedSize(true);

        ListAdapter ladapter = new ListAdapter(data, getActivity());
        fragment_account_list.setAdapter(ladapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAccountFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountFragmentInteractionListener) {
            mListener = (OnAccountFragmentInteractionListener) context;
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


    public interface OnAccountFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAccountFragmentInteraction(Uri uri);
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
                    switch (position) {
                        case 0:


                            break;
                        case 1:

                            break;

                    }
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
            }
        }
    }
}
