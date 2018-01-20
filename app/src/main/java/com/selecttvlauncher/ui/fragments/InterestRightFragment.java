package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvlauncher.BeanClass.FavoriteBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class InterestRightFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView add_remove_text;
    private RecyclerView fragment_Interest_content_list;

    private OnInterestRightFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    ListContentAdapter ListContentAdapter;
    private int selected_pos;
    private ProgressBar load_progressBar;


    public InterestRightFragment() {
        // Required empty public constructor
    }


    public static InterestRightFragment newInstance(String param1, String param2) {
        InterestRightFragment fragment = new InterestRightFragment();
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
        View view = inflater.inflate(R.layout.fragment_interest_right, container, false);

        add_remove_text=(TextView)view.findViewById(R.id.add_remove_text);
        fragment_Interest_content_list=(RecyclerView)view.findViewById(R.id.fragment_Interest_content_list);
        load_progressBar=(ProgressBar)view.findViewById(R.id.load_progressBar);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragment_Interest_content_list.setLayoutManager(linearLayoutManager);
        ((HomeActivity)getActivity()).setmInterestRightFragment(this);

        selected_pos=Integer.parseInt(mParam1);

        if(selected_pos==0) {
            ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.tvShowsList,"show");
            fragment_Interest_content_list.setAdapter(ListContentAdapter);
        }else if(selected_pos==1) {
            ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.moviesList,"movie");
            fragment_Interest_content_list.setAdapter(ListContentAdapter);
        }else if(selected_pos==2) {
             ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.movieGenresList,"moviegenre");
            fragment_Interest_content_list.setAdapter(ListContentAdapter);
        }else if(selected_pos==3) {
             ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.channelsList,"channel");
            fragment_Interest_content_list.setAdapter(ListContentAdapter);
        }else if(selected_pos==4) {
             ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.tvnetworksList,"network");
            fragment_Interest_content_list.setAdapter(ListContentAdapter);
        }else if(selected_pos==5) {
             ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.videoLibrariesList,"videolibrary");
            fragment_Interest_content_list.setAdapter(ListContentAdapter);
        }




        add_remove_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPersonalizeDialog(selected_pos);
            }
        });

        Utilities.setViewFocus(getActivity(), add_remove_text);



        return view;
    }

    private void showPersonalizeDialog(int selected_pos) {
        try {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            InterestDialogFragment dialogFragment = InterestDialogFragment.newInstance(""+selected_pos,"");
            dialogFragment.show(fm.beginTransaction(), "dialogFragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onInterestRightFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInterestRightFragmentInteractionListener) {
            mListener = (OnInterestRightFragmentInteractionListener) context;
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

    public void refreshadapter() {
        try {
            //ListContentAdapter.notifyDataSetChanged();
            new reLoadFavoriteList().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnInterestRightFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInterestRightFragmentInteraction(Uri uri);
    }

    public class ListContentAdapter extends RecyclerView.Adapter<ListContentAdapter.DataObjectHolder> {
        ArrayList<String> list;
        Context context;
        int mlistPosition = 0;
        ArrayList<FavoriteBean> tvShowsList;
        String type;

        public ListContentAdapter(Context context, ArrayList<FavoriteBean> tvShowsList,String type) {
            this.context = context;
            this.tvShowsList=tvShowsList;
            this.type=type;
        }

        @Override
        public ListContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.interest_content_list_item, parent, false);
            return new DataObjectHolder(view);
        }


        @Override
        public void onBindViewHolder(final ListContentAdapter.DataObjectHolder holder, final int position) {

            holder.entity_imageview.loadImage(tvShowsList.get(position).getImage());
            holder.title_textView.setText(tvShowsList.get(position).getName());

            String rating =tvShowsList.get(position).getRating();
            String runtime=tvShowsList.get(position).getRuntime();
            String description=tvShowsList.get(position).getDescription();

            if(!TextUtils.isEmpty(rating)&&!rating.equalsIgnoreCase("null")){
                holder.rating_textView_value.setText(tvShowsList.get(position).getRating());
                holder.rating_textView_value.setVisibility(View.VISIBLE);
                holder.rating_tex.setVisibility(View.VISIBLE);
            }else{
                holder.rating_textView_value.setVisibility(View.GONE);
                holder.rating_tex.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(runtime)&&!runtime.equalsIgnoreCase("null")){
                holder.runtime_textView_value.setText(runtime);
                holder.runtime_textView_value.setVisibility(View.VISIBLE);
                holder.runtime_text.setVisibility(View.VISIBLE);
            }else{
                holder.runtime_textView_value.setVisibility(View.GONE);
                holder.runtime_text.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(description)&&!description.equalsIgnoreCase("null")){
                holder.description_textView.setText(description);
                holder.description_textView.setVisibility(View.VISIBLE);
            }else{
                holder.description_textView.setVisibility(View.GONE);
            }

            holder.runtime_textView_value.setText(tvShowsList.get(position).getRuntime());
            holder.description_textView.setText(tvShowsList.get(position).getDescription());

            holder.entity_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type.equalsIgnoreCase("show")){
                        FragmentSubScriptionList.sSelectedMenu = "";
                        HomeActivity.toolbarSubContent = tvShowsList.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, tvShowsList.get(position).getId());
                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                        fragmentTransaction.commit();

                    }else if(type.equalsIgnoreCase("movie")){
                        FragmentSubScriptionList.sSelectedMenu = "";
                        HomeActivity.toolbarSubContent = tvShowsList.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, tvShowsList.get(position).getId());
                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                        fragmentTransaction.commit();
                    }else if(type.equalsIgnoreCase("moviegenre")){
                        HomeActivity.isPayperview="On-Demand";
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentOnDemandAll fragmentOnDemandAll = FragmentOnDemandAll.newInstance("moviegenre",""+tvShowsList.get(position).getId());
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                        fragmentTransaction.commit();
                    }else if(type.equalsIgnoreCase("network")){
                        HomeActivity.isPayperview="On-Demand";
                        HomeActivity.networkImage=tvShowsList.get(position).getImage();
                        HomeActivity.toolbarSubContent=tvShowsList.get(position).getName();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentOnDemandAll fragmentOnDemandAll = FragmentOnDemandAll.newInstance("network",""+tvShowsList.get(position).getId());
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                        fragmentTransaction.commit();
                    }

                }
            });
            holder.remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Log.d("::selected id::", "::::" + tvShowsList.get(position).getId());
                            new RemoveFavorites().execute(type, "" + tvShowsList.get(position).getId());
                        tvShowsList.remove(position);
                        notifyDataSetChanged();
                       /* switch(type){
                            case "show":
                                HomeActivity.tvShowsList.clear();
                                HomeActivity.tvShowsList.addAll(tvShowsList);
                                break;
                            case "movie":
                                HomeActivity.moviesList.clear();
                                HomeActivity.moviesList.addAll(tvShowsList);
                                break;
                            case "moviegenre":
                               *//* HomeActivity.movieGenresList.clear();
                                HomeActivity.movieGenresList.addAll(tvShowsList);*//*
                                break;
                            case "channel":
                                HomeActivity.channelsList.clear();
                                HomeActivity.channelsList.addAll(tvShowsList);
                                break;
                            case "network":
                                HomeActivity.tvnetworksList.clear();
                                HomeActivity.tvnetworksList.addAll(tvShowsList);
                                break;
                            case "videolibrary":
                                HomeActivity.videoLibrariesList.clear();
                                HomeActivity.videoLibrariesList.addAll(tvShowsList);
                                break;
                        }*/




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return tvShowsList.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView entity_imageview;
            TextView title_textView,rating_tex,rating_textView_value,runtime_text,runtime_textView_value,description_textView;
            Button remove_button;


            public DataObjectHolder(View itemView) {
                super(itemView);

                entity_imageview=(DynamicImageView)itemView.findViewById(R.id.entity_imageview);
                title_textView=(TextView)itemView.findViewById(R.id.title_textView);
                rating_textView_value=(TextView)itemView.findViewById(R.id.rating_textView_value);
                rating_tex=(TextView)itemView.findViewById(R.id.rating_tex);
                runtime_text=(TextView)itemView.findViewById(R.id.runtime_text);
                runtime_textView_value=(TextView)itemView.findViewById(R.id.runtime_textView_value);
                description_textView=(TextView)itemView.findViewById(R.id.description_textView);
                remove_button=(Button)itemView.findViewById(R.id.remove_button);

            }
        }
    }

    private class RemoveFavorites extends AsyncTask<String, Object, Object> {
        JSONObject m_response;

        @Override
        protected Object doInBackground(String... params) {
            try {
                Log.d("accesstoken::","::"+ PreferenceManager.getAccessToken(getActivity()));

                m_response = JSONRPCAPI.removeFavorite(PreferenceManager.getAccessToken(getActivity()), params[0], Integer.parseInt(params[1]));
                if ( m_response == null) return null;
                Log.d("m_response::", "::" + m_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if(m_response.has("sucess")){
                    if(m_response.getBoolean("sucess")){
                        Toast.makeText(getActivity(), "Removed from Favorite List", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class reLoadFavoriteList extends AsyncTask<String, Object, Object> {
        JSONObject m_response;

        @Override
        protected Object doInBackground(String... params) {
            try {
                JSONObject test_arry= JSONRPCAPI.getFavoriteList(PreferenceManager.getAccessToken(getActivity()));
                if ( test_arry == null) return null;
                Log.d("selecttv::==>", "favorite.list::" + test_arry);

                if(test_arry.has("tvnetworks")){
                    JSONArray tvnetworks_array=test_arry.getJSONArray("tvnetworks");
                    if(tvnetworks_array.length()>0){
                        for(int i=0;i<tvnetworks_array.length();i++){
                            JSONObject tvnetworks_object=tvnetworks_array.getJSONObject(i);
                            String slug=tvnetworks_object.getString("slug");
                            int id=tvnetworks_object.getInt("id");
                            String image=tvnetworks_object.getString("image");
                            String name=tvnetworks_object.getString("name");
                            String rating=tvnetworks_object.getString("rating");
                            String runtime=tvnetworks_object.getString("runtime");
                            String description=tvnetworks_object.getString("description");

                            HomeActivity.tvnetworksList.add(new FavoriteBean(slug, image, name, id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("channels")){
                    JSONArray channels_array=test_arry.getJSONArray("channels");
                    if(channels_array.length()>0){
                        for(int i=0;i<channels_array.length();i++){
                            JSONObject channels_array_object=channels_array.getJSONObject(i);
                            String slug=channels_array_object.getString("slug");
                            int id=channels_array_object.getInt("id");
                            String image=channels_array_object.getString("image");
                            String name=channels_array_object.getString("name");
                            String rating=channels_array_object.getString("rating");
                            String runtime=channels_array_object.getString("runtime");
                            String description=channels_array_object.getString("description");

                            HomeActivity.channelsList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("movies")){
                    JSONArray movies_array=test_arry.getJSONArray("movies");
                    if(movies_array.length()>0){
                        for(int i=0;i<movies_array.length();i++){
                            JSONObject movies_object=movies_array.getJSONObject(i);
                            String slug=movies_object.getString("slug");
                            int id=movies_object.getInt("id");
                            String image=movies_object.getString("image");
                            String name=movies_object.getString("name");
                            String rating=movies_object.getString("rating");
                            String runtime=movies_object.getString("runtime");
                            String description=movies_object.getString("description");

                            HomeActivity.moviesList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("videolibraries")){
                    JSONArray videolibraries_array=test_arry.getJSONArray("videolibraries");
                    if(videolibraries_array.length()>0){
                        for(int i=0;i<videolibraries_array.length();i++){
                            JSONObject videolibraries_object=videolibraries_array.getJSONObject(i);
                            String slug=videolibraries_object.getString("slug");
                            int id=videolibraries_object.getInt("id");
                            String image=videolibraries_object.getString("image");
                            String name=videolibraries_object.getString("name");
                            String rating=videolibraries_object.getString("rating");
                            String runtime=videolibraries_object.getString("runtime");
                            String description=videolibraries_object.getString("description");

                            HomeActivity.videoLibrariesList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("moviegenres")){
                    JSONArray moviegenres_array=test_arry.getJSONArray("moviegenres");
                    if(moviegenres_array.length()>0){
                        for(int i=0;i<moviegenres_array.length();i++){
                            JSONObject moviegenres_object=moviegenres_array.getJSONObject(i);
                            String slug=moviegenres_object.getString("slug");
                            int id=moviegenres_object.getInt("id");
                            String image=moviegenres_object.getString("image");
                            String name=moviegenres_object.getString("name");
                            String rating=moviegenres_object.getString("rating");
                            String runtime=moviegenres_object.getString("runtime");
                            String description=moviegenres_object.getString("description");

                            HomeActivity.movieGenresList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("shows")){
                    JSONArray shows_array=test_arry.getJSONArray("shows");
                    if(shows_array.length()>0){
                        for(int i=0;i<shows_array.length();i++){
                            JSONObject shows_object=shows_array.getJSONObject(i);
                            String slug=shows_object.getString("slug");
                            int id=shows_object.getInt("id");
                            String image=shows_object.getString("image");
                            String name=shows_object.getString("name");
                            String rating=shows_object.getString("rating");
                            String runtime=shows_object.getString("runtime");
                            String description=shows_object.getString("description");

                            HomeActivity.tvShowsList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                load_progressBar.setVisibility(View.VISIBLE);
                fragment_Interest_content_list.setVisibility(View.GONE);
                HomeActivity.tvShowsList=new ArrayList<>();
                HomeActivity.moviesList=new ArrayList<>();
                HomeActivity.movieGenresList=new ArrayList<>();
                HomeActivity.channelsList=new ArrayList<>();
                HomeActivity.tvnetworksList=new ArrayList<>();
                HomeActivity.videoLibrariesList=new ArrayList<>();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                load_progressBar.setVisibility(View.GONE);
                fragment_Interest_content_list.setVisibility(View.VISIBLE);

                if(selected_pos==0) {
                    ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.tvShowsList,"show");
                    fragment_Interest_content_list.setAdapter(ListContentAdapter);
                }else if(selected_pos==1) {
                    ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.moviesList,"movie");
                    fragment_Interest_content_list.setAdapter(ListContentAdapter);
                }else if(selected_pos==2) {
                    ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.movieGenresList,"moviegenre");
                    fragment_Interest_content_list.setAdapter(ListContentAdapter);
                }else if(selected_pos==3) {
                    ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.channelsList,"channel");
                    fragment_Interest_content_list.setAdapter(ListContentAdapter);
                }else if(selected_pos==4) {
                    ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.tvnetworksList,"network");
                    fragment_Interest_content_list.setAdapter(ListContentAdapter);
                }else if(selected_pos==5) {
                    ListContentAdapter=new ListContentAdapter(getActivity(), HomeActivity.videoLibrariesList,"videolibrary");
                    fragment_Interest_content_list.setAdapter(ListContentAdapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
