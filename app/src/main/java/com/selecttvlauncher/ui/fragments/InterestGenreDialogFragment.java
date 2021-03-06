package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvlauncher.BeanClass.FavoriteBean;
import com.selecttvlauncher.BeanClass.GenreBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class InterestGenreDialogFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnInterestDialogFragmentInteractionListener mListener;
    private RecyclerView content_recyclerView;
    private ProgressBar list_progressBar;
    private ArrayList<GenreBean> genre_list=new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private ImageView close_imageButton;
    private Button next_button,previous_button;
    private TextView skip_text,desc_textView,title_txt;
    private Typeface OpenSans_Bold,OpenSans_Regular,OpenSans_Semibold,osl_ttf;

    public InterestGenreDialogFragment() {
        // Required empty public constructor
    }

    public static InterestGenreDialogFragment newInstance(String param1, String param2) {
        InterestGenreDialogFragment fragment = new InterestGenreDialogFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interest_dialog, container, false);

        text_font_typeface();


        content_recyclerView=(RecyclerView)view.findViewById(R.id.content_recyclerView);
        list_progressBar=(ProgressBar)view.findViewById(R.id.list_progressBar);
        next_button=(Button)view.findViewById(R.id.next_button);
        previous_button=(Button)view.findViewById(R.id.previous_button);
        skip_text=(TextView)view.findViewById(R.id.skip_text);
        desc_textView=(TextView)view.findViewById(R.id.desc_textView);
        title_txt=(TextView)view.findViewById(R.id.title_txt);

        title_txt.setTypeface(OpenSans_Regular);
        desc_textView.setTypeface(OpenSans_Regular);
        previous_button.setTypeface(OpenSans_Regular);
        next_button.setTypeface(OpenSans_Regular);
        skip_text.setTypeface(OpenSans_Regular);

        gridLayoutManager = new GridLayoutManager(getActivity(), 6);
        content_recyclerView.setLayoutManager(gridLayoutManager);
        content_recyclerView.setHasFixedSize(true);
        int spanCount = 6; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        content_recyclerView.setFocusable(true);
        content_recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                new LoadingMovieGenre().execute();
            }else{
                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChannelDialog();
            }
        });
        skip_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onInterestDialogFragmentSkipSelected();
                }
            }
        });

        Utilities.setViewFocus(getActivity(), next_button);
        Utilities.setTextFocus(getActivity(), skip_text);
        content_recyclerView.setFocusable(true);



        return view;
    }

    private void showChannelDialog() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        InterestChannelsFragment mInterestChannelsFragment = new InterestChannelsFragment();
        fragmentTransaction.replace(R.id.fragment_dialogs, mInterestChannelsFragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInterestDialogFragmentInteractionListener) {
            mListener = (OnInterestDialogFragmentInteractionListener) context;
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

    public interface OnInterestDialogFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInterestDialogFragmentSkipSelected();
    }

    private class LoadingMovieGenre extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            try {
                JSONArray m_jsonMovieGenre = JSONRPCAPI.getMovieGenreList();
                if (  HomeActivity.m_jsonMovieGenre  == null) return null;
                Log.d("m_jsonmovieGenre::", "::" + m_jsonMovieGenre);
                if(m_jsonMovieGenre.length()>0){
                    for(int i=0;i<m_jsonMovieGenre.length();i++){
                        JSONObject obj=m_jsonMovieGenre.getJSONObject(i);
                        int id=obj.getInt("id");
                        String name=obj.getString("name");
                        genre_list.add(new GenreBean(id,name));
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
            list_progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                list_progressBar.setVisibility(View.GONE);
                displayGenreList();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayGenreList() {
        InterestGridAdapter listenGridAdapter = new InterestGridAdapter(genre_list, getActivity());
        content_recyclerView.setAdapter(listenGridAdapter);
    }


    private class InterestGridAdapter extends RecyclerView.Adapter<InterestGridAdapter.DataObjectHolder> {
        ArrayList<GenreBean> listenGridBeans;
        Context context;

        public InterestGridAdapter(ArrayList<GenreBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;
        }

        @Override
        public InterestGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.interest_genre_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final InterestGridAdapter.DataObjectHolder holder, final int position) {
            holder.gridview_text_item.setText(listenGridBeans.get(position).getName());
            holder.genre_name.setText(listenGridBeans.get(position).getName());
            holder.genre_name.setVisibility(View.VISIBLE);
            holder.gridview_text_item.setTypeface(OpenSans_Regular);
            holder.genre_name.setTypeface(OpenSans_Regular);


            if(isfavorited(listenGridBeans.get(position).getId())){
                holder.checkBox.setChecked(true);
                holder.checkBox.setVisibility(View.VISIBLE);
            }else{
                holder.checkBox.setChecked(false);
                holder.checkBox.setVisibility(View.GONE);
            }


            holder.genre_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkBox.isChecked()){
                        new AddFavorites().execute("remove",""+listenGridBeans.get(position).getId(),"");
                        holder.checkBox.setVisibility(View.GONE);
                        holder.checkBox.setChecked(false);

                    }else{
                        new AddFavorites().execute("add",""+listenGridBeans.get(position).getId(),listenGridBeans.get(position).getName());
                        holder.checkBox.setVisibility(View.VISIBLE);
                        holder.checkBox.setChecked(true);
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView gridview_text_item,genre_name;
            CheckBox checkBox;
            LinearLayout genre_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                gridview_text_item = (TextView) itemView.findViewById(R.id.gridview_text_item);
                genre_name = (TextView) itemView.findViewById(R.id.genre_name);
                checkBox=(CheckBox)itemView.findViewById(R.id.checkBox);
                genre_layout=(LinearLayout)itemView.findViewById(R.id.genre_layout);

                Utilities.setViewFocus(getActivity(), genre_layout);
            }
        }
    }

    private boolean isfavorited(int id) {
        boolean result=false;
        for(FavoriteBean b : HomeActivity.movieGenresList){
            if(b.getId()==id){
                result=true;
            }else {

            }
        }
        return result;
    }

    private void text_font_typeface() {
        try {
            OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
            OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
            osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AddFavorites extends AsyncTask<String, Object, Object> {
        JSONObject m_response;
        String task="";
        int id;
        String name="";

        @Override
        protected Object doInBackground(String... params) {
            try {
                Log.d("accesstoken::","::"+ PreferenceManager.getAccessToken(getActivity()));
                task=params[0];
                id=Integer.parseInt(params[1]);
                name=params[2];
                if(params[0].equals("add")){
                    m_response = JSONRPCAPI.addToFavorite(PreferenceManager.getAccessToken(getActivity()),"moviegenre",Integer.parseInt(params[1]));
                    if ( m_response == null) return null;
                    Log.d("m_response::", "::" + m_response);
                }else  if(params[0].equals("remove")){
                    m_response = JSONRPCAPI.removeFavorite(PreferenceManager.getAccessToken(getActivity()), "moviegenre", Integer.parseInt(params[1]));
                    if ( m_response == null) return null;
                    Log.d("m_response::", "::" + m_response);
                }
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
                        if(task.equalsIgnoreCase("add")){
                            HomeActivity.movieGenresList.add(new FavoriteBean("","",name,id,"","",""));
                            Toast.makeText(getActivity(),"Added to Favorite List",Toast.LENGTH_SHORT).show();
                        }else{

                            for(Iterator<FavoriteBean> it = HomeActivity.movieGenresList.iterator(); it.hasNext();) {
                                FavoriteBean s = it.next();
                                if(s.getId() == id) {
                                    it.remove();
                                }
                            }
                            Toast.makeText(getActivity(),"Removed from Favorite List",Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
