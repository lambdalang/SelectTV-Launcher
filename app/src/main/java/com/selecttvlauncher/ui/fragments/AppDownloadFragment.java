package com.selecttvlauncher.ui.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvlauncher.BeanClass.CategoryBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.dialogs.OnDemandDialogVideo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppDownloadFragment.OnAppFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppDownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppDownloadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int temp_position=0;

    private OnAppFragmentInteractionListener mListener;

    private RecyclerView dynamic_content_listview;
    private ArrayList<CategoryBean> appCategoriesList = new ArrayList<>();
    private static double list_width = 0;
    private int slider_image_width = 0;
    static LayoutInflater slder_inflate;
    private AQuery aq;
    private LayoutInflater mlayoutinflate, mlayoutinflater;
    private Button done_btn;
    GridLayoutManager gridLayoutManager;
    private RecyclerView categories_list_view;
    int tabwidth = 0;
    ArrayList<CategoryBean> mEssentialarray = new ArrayList<>();
    ArrayList<CategoryBean> mBroadcastarray = new ArrayList<>();
    ArrayList<CategoryBean> mCablearray = new ArrayList<>();
    ArrayList<CategoryBean> mSubcriptionsarray = new ArrayList<>();
    ArrayList<CategoryBean> mMoviesarray = new ArrayList<>();
    ArrayList<CategoryBean> mOthersarray = new ArrayList<>();
    TextView fragment_appmanager_newlayout_note_text;
    TextView fragment_appmanager_newlayout_help_text;
    OnDemandDialogVideo demanddialog;
    public AppDownloadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppDownloadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppDownloadFragment newInstance(String param1, String param2) {
        AppDownloadFragment fragment = new AppDownloadFragment();
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
        View view = inflater.inflate(R.layout.fragment_app_download, container, false);


        aq = new AQuery(getActivity());
//        PreferenceManager.setDemandFirstTime(false, getActivity());
        dynamic_content_listview = (RecyclerView) view.findViewById(R.id.dynamic_content_listview);
        categories_list_view = (RecyclerView) view.findViewById(R.id.categories_list_view);
        fragment_appmanager_newlayout_note_text = (TextView) view.findViewById(R.id.fragment_appmanager_newlayout_note_text);
        fragment_appmanager_newlayout_help_text = (TextView) view.findViewById(R.id.fragment_appmanager_newlayout_help_text);
        done_btn = (Button) view.findViewById(R.id.done_btn);
//        LinearLayoutManager linear_layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        dynamic_content_listview.setLayoutManager(linear_layoutManager1);
        mlayoutinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int nWidth = displayMetrics.widthPixels;
        int nHeight = displayMetrics.heightPixels;
        tabwidth = nWidth / 3;

        LinearLayoutManager mLayoutManagerr = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        categories_list_view.setLayoutManager(mLayoutManagerr);
        categories_list_view.hasFixedSize();
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        dynamic_content_listview.setLayoutManager(gridLayoutManager);
        dynamic_content_listview.hasFixedSize();
        String sourceString = "<b>" + getString(R.string.appmanager_note_first_string) + "</b> " + getString(R.string.appmanager_note_string);
        fragment_appmanager_newlayout_note_text.setText(Html.fromHtml(sourceString));
        fragment_appmanager_newlayout_help_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                demanddialog=new OnDemandDialogVideo();
                demanddialog.show(fragmentManager,"");
//                Fragment fragment = new OnDemandYoutubeFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_body, fragment);
//                fragmentTransaction.commit();
            }
        });

        ((HomeActivity) getActivity()).toolbarTextChange("Fast Download", "", "", "");
        /*list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = list_spinner.getSelectedItem();
                CategoryBean sb = (CategoryBean) obj;
                Log.d("FORM Spin ANS===>", sb.getName());
                String slug = sb.getSlug();
                new LoadingCategoryItems().execute(sb.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mListener.onAppFragmentInteraction();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                OnDemandFavoriteFragment mfragmentOnDemandAll = new OnDemandFavoriteFragment();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mfragmentOnDemandAll);
                fragmentTransaction.commit();
            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int widtht = displaymetrics.widthPixels;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            list_width = (0.9 * height * 0.8);
        } else {
            list_width = (0.9 * widtht * 0.8);
        }
        slider_image_width = (int) (list_width);

//        new getAppCatgoryList().execute();
        appCategoriesList = new ArrayList<>();
        appCategoriesList.add(new CategoryBean("", "ESSENTIALS", 0));
        appCategoriesList.add(new CategoryBean("", "BROADCAST", 0));
        appCategoriesList.add(new CategoryBean("", "CABLE", 0));
        appCategoriesList.add(new CategoryBean("", "SUBSCRIPTIONS", 0));
        appCategoriesList.add(new CategoryBean("", "MOVIES", 0));
        appCategoriesList.add(new CategoryBean("", "OTHER", 0));
        mEssentialarray = new ArrayList<>();
        mEssentialarray.add(new CategoryBean("apks/abc.apk", "ABC", R.drawable.abc));
        mEssentialarray.add(new CategoryBean("apks/nbc.apk", "NBC", R.drawable.nbc));
        mEssentialarray.add(new CategoryBean("apks/cbs.apk", "CBS", R.drawable.cbs));
        mEssentialarray.add(new CategoryBean("apks/crackle.apk", "Crackle", R.drawable.crackle));
        mEssentialarray.add(new CategoryBean("apks/fox.apk", "FOX", R.drawable.fox));
        mEssentialarray.add(new CategoryBean("apks/popcornflix.apk", "Popcornflix", R.drawable.popcorn_flix));
        mEssentialarray.add(new CategoryBean("apks/netflix.apk", "Netflix_v4", R.drawable.netflix_active));
        mEssentialarray.add(new CategoryBean("apks/pbs.apk", "PBS", R.drawable.pbs_network));
        mBroadcastarray = new ArrayList<>();
        mBroadcastarray.add(new CategoryBean("apks/abc.apk", "ABC", R.drawable.abc));
        mBroadcastarray.add(new CategoryBean("apks/cbs.apk", "CBS", R.drawable.cbs));
        mBroadcastarray.add(new CategoryBean("apks/cw.apk", "The CW", R.drawable.cw));
        mBroadcastarray.add(new CategoryBean("apks/fox.apk", "FOX", R.drawable.fox));
        mBroadcastarray.add(new CategoryBean("apks/nbc.apk", "NBC", R.drawable.nbc));
        mBroadcastarray.add(new CategoryBean("apks/pbs.apk", "PBS", R.drawable.pbs_network));
        mCablearray = new ArrayList<>();
        mCablearray.add(new CategoryBean("apks/ae.apk", "A&E", R.drawable.a_e));
        mCablearray.add(new CategoryBean("apks/amc.apk", "AMC", R.drawable.amc));
        mCablearray.add(new CategoryBean("apks/cartoonnetwork.apk", "Cartoon Network", R.drawable.cartoon_network));
        mCablearray.add(new CategoryBean("apks/comedycentral.apk", "Comedy Central", R.drawable.comedy_central));
        mCablearray.add(new CategoryBean("apks/disneychannel.apk", "Disney Channel", R.drawable.disney_channel));
        mCablearray.add(new CategoryBean("apks/disneyjunior.apk", "Disney Junior", R.drawable.disney_junior));
        mCablearray.add(new CategoryBean("apks/epix.apk", "EPIX", R.drawable.epix));
        mCablearray.add(new CategoryBean("apks/fxnow.apk", "FXNOW", R.drawable.fx_networks));
        mCablearray.add(new CategoryBean("apks/hgtv.apk", "HGTV", R.drawable.hgtv));
        mCablearray.add(new CategoryBean("apks/history.apk", "HISTORY", R.drawable.history));
        mCablearray.add(new CategoryBean("apks/ifc.apk", "Watch IFC", R.drawable.ifc));
        mCablearray.add(new CategoryBean("apks/lifetime.apk", "Lifetime", R.drawable.lifetime));
        mCablearray.add(new CategoryBean("apks/maxgo.apk", "MAX GO", R.drawable.max_go));
        mCablearray.add(new CategoryBean("apks/mtv.apk", "MTV", R.drawable.mtv));
        mCablearray.add(new CategoryBean("apks/nickstudio.apk", "Nick Studio", R.drawable.nick));
        mCablearray.add(new CategoryBean("apks/nickstudio.apk", "Nick Studio", R.drawable.nick_jr));
        mCablearray.add(new CategoryBean("apks/redbulltv.apk", "Redbull", R.drawable.redbull_tv));
        mCablearray.add(new CategoryBean("apks/spike.apk", "Spike", R.drawable.spike));
        mCablearray.add(new CategoryBean("apks/starz.apk", "Starz", R.drawable.starz_icon));
        mCablearray.add(new CategoryBean("apks/tbs.apk", "TBS", R.drawable.tbs_icon));
        mCablearray.add(new CategoryBean("apks/tnt.apk", "TNT", R.drawable.tnt_icon));
        mCablearray.add(new CategoryBean("apks/trutv.apk", "TruTV", R.drawable.trutv_icon));
        mCablearray.add(new CategoryBean("apks/usanow.apk", "USA", R.drawable.usa_icon));
        mCablearray.add(new CategoryBean("apks/vh1.apk", "VH1", R.drawable.vh1_icon));
        mCablearray.add(new CategoryBean("apks/viceland.apk", "Viceland", R.drawable.viceland_icon));
        mCablearray.add(new CategoryBean("apks/xfinity.apk", "Xfinity", R.drawable.xfinity_icon));

        mSubcriptionsarray = new ArrayList<>();

        mSubcriptionsarray.add(new CategoryBean("apks/amazonprimenow.apk", "amazon_prime", R.drawable.amazon_prime));
        mSubcriptionsarray.add(new CategoryBean("apks/cbs.apk", "CBS_ALL_ACCESS", R.drawable.cbs_all_access));
        mSubcriptionsarray.add(new CategoryBean("apks/feeln.apk", "Feeln", R.drawable.feeln));
        mSubcriptionsarray.add(new CategoryBean("apks/hbo.apk", "HBO_Now", R.drawable.hbo_now));
        mSubcriptionsarray.add(new CategoryBean("apks/netflix.apk", "Netflix", R.drawable.netflix_active));
        mSubcriptionsarray.add(new CategoryBean("apks/seeso.apk", "seeso", R.drawable.seeso));
        mSubcriptionsarray.add(new CategoryBean("apks/sundance.apk", "Sundance_Now", R.drawable.sundance_now));

        mMoviesarray = new ArrayList<>();
        mMoviesarray.add(new CategoryBean("apks/crackle.apk", "CRANCKLE", R.drawable.crackle));
        mMoviesarray.add(new CategoryBean("apks/epix.apk", "epix", R.drawable.epix));
        mMoviesarray.add(new CategoryBean("apks/hulu.apk", "hulu", R.drawable.hulu_active));
        mMoviesarray.add(new CategoryBean("apks/popcornflix.apk", "Popcorn-Flix", R.drawable.popcorn_flix));
        mMoviesarray.add(new CategoryBean("apks/vudu.apk", "Vudu", R.drawable.vudu_icon));

        mOthersarray = new ArrayList<>();
        mOthersarray.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.imdb.mobile&hl=en", "IMDb Movies & TV", R.drawable.imdb_app_image));
        mOthersarray.add(new CategoryBean("https://play.google.com/store/apps/details?id=org.xbmc.kodi&hl=en", "Kodi", R.drawable.kodi));
        mOthersarray.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.samsung.android.video360&hl=en", "Samsung VR", R.drawable.vr_app_image));
        mOthersarray.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.foxsports.videogo&hl=en", "FOX Sports GO", R.drawable.fox_sports_go_app));
        mOthersarray.add(new CategoryBean("https://play.google.com/store/apps/details?id=com.tekoia.sure.activities&hl=en", "SURE Universal Remote for TV", R.drawable.sure_universalremotetv));

        CategoryAdapter mCategoryAdapter = new CategoryAdapter(appCategoriesList, getActivity());
        categories_list_view.setAdapter(mCategoryAdapter);
        OnDemandContentAdapter mOnDemandContentAdapter = new OnDemandContentAdapter(mEssentialarray, getActivity());
        dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppFragmentInteractionListener) {
            mListener = (OnAppFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnAppFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAppFragmentInteraction();
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.DataObjectHolder> {
        ArrayList<CategoryBean> category_list;
        Context context;
        int mSelectedItem;
        Drawable img;
        ScaleDrawable sd;

        public CategoryAdapter(ArrayList<CategoryBean> category_list, Context context) {
            this.category_list = category_list;
            this.context = context;
            this.mSelectedItem = 0;
            try {
                //text_font_typeface();
                // img = ContextCompat.getDrawable(getActivity(), R.drawable.spinner_icon);
                if (img != null) {
                    img.setBounds(0, 0, 12, 12);
                    img.setBounds(0, 0, (int) (img.getIntrinsicWidth() * 0.5),
                            (int) (img.getIntrinsicHeight() * 0.5));
                    sd = new ScaleDrawable(img, 0, 12, 12);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public CategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_list_layout, parent, false);
            return new CategoryAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final CategoryAdapter.DataObjectHolder holder, final int position) {
            try {
                holder.fragment_ondemandlist_items.setText(category_list.get(position).getName());
//                ViewGroup.LayoutParams params = holder.fragment_ondemandlist_items_layout.getLayoutParams();
//                params.width = tabwidth;
//                holder.fragment_ondemandlist_items_layout.setLayoutParams(params);

                //holder.fragment_ondemandlist_items.setCompoundDrawables(null, null, sd.getDrawable(), null);
                if (position == mSelectedItem) {
                    Log.d("drawable", "position  " + mSelectedItem);
//                    holder.bottom_line.setVisibility(View.GONE);
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                } else {
//                    holder.bottom_line.setVisibility(View.GONE);
                    holder.fragment_ondemandlist_items.setBackgroundResource(0);
                }
//                if (position == mSelectedItem) {
//                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
//                } else {
//                    holder.fragment_ondemandlist_items.setBackgroundResource(0);
//                }
             /*   holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.bottom_line.setVisibility(View.VISIBLE);
                        //notifyDataSetChanged();
                    }
                });*/

                holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("FORM Spin ANS===>", category_list.get(position).getName());

//                        String slug = application_list.get(position).getSlug();
//                        new LoadingCategoryItems().execute(application_list.get(position).getId());
                        try {
                            mLoadSubAdapter(holder.getAdapterPosition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mSelectedItem = position;
                        notifyDataSetChanged();


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        public int getItemCount() {
            return category_list.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;
//            View bottom_line;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
//                bottom_line = (View) itemView.findViewById(R.id.bottom_line);

            }
        }
    }

    public void mLoadSubAdapter(int position) {

        switch (position) {

            case 0:
                OnDemandContentAdapter mOnDemandContentAdapter = new OnDemandContentAdapter(mEssentialarray, getActivity());
                dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
                temp_position=position;
                break;
            case 1:
                mOnDemandContentAdapter = new OnDemandContentAdapter(mBroadcastarray, getActivity());
                dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
                temp_position=position;
                break;
            case 2:
                mOnDemandContentAdapter = new OnDemandContentAdapter(mCablearray, getActivity());
                dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
                temp_position=position;
                break;

            case 3:
                mOnDemandContentAdapter = new OnDemandContentAdapter(mSubcriptionsarray, getActivity());
                dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
                temp_position=position;
                break;

            case 4:
                mOnDemandContentAdapter = new OnDemandContentAdapter(mMoviesarray, getActivity());
                dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
                temp_position=position;
                break;

            case 5:
                mOnDemandContentAdapter = new OnDemandContentAdapter(mOthersarray, getActivity());
                dynamic_content_listview.setAdapter(mOnDemandContentAdapter);
                Log.d("order","order");
                temp_position=position;
                break;
        }
    }


    private class OnDemandContentAdapter extends RecyclerView.Adapter<OnDemandContentAdapter.DataObjectHolder> {
        ArrayList<CategoryBean> application_list;
        Context context;
        int mSelectedItem;
        Drawable img;
        ScaleDrawable sd;

        public OnDemandContentAdapter(ArrayList<CategoryBean> category_list, Context context) {
            this.application_list = category_list;
            this.context = context;
            this.mSelectedItem = 0;
//            try {
//                //text_font_typeface();
//                // img = ContextCompat.getDrawable(getActivity(), R.drawable.spinner_icon);
//                if (img != null) {
//                    img.setBounds(0, 0, 12, 12);
//                    img.setBounds(0, 0, (int) (img.getIntrinsicWidth() * 0.5),
//                            (int) (img.getIntrinsicHeight() * 0.5));
//                    sd = new ScaleDrawable(img, 0, 12, 12);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }


        @Override
        public OnDemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.appmanager_adapter_layout, parent, false);
            return new OnDemandContentAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final OnDemandContentAdapter.DataObjectHolder holder, final int position) {
            try {
                holder.appmanager_adapter_network_images.setImageResource(application_list.get(position).getId());
                holder.appmanager_adapter_network_install_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if(temp_position== appCategoriesList.size()-1)
                            {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+application_list.get(position).getName(),""+application_list.get(position).getId(),application_list.get(position).getSlug());

                                dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                            }
                            else {
                                mDownloadApk("http://tvappsplus.com/" + application_list.get(holder.getAdapterPosition()).getSlug().replaceAll(" ", "%20"), getActivity(), application_list.get(holder.getAdapterPosition()).getName());


                            }



//                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://indiawebcoders.com/mobileapps/tvappsnow/"+application_list.get(holder.getAdapterPosition()).getSlug().replaceAll(" ", "%20")));
//                            if (myIntent != null) {
//                                startActivity(myIntent);
//                            } else {
//                                Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return application_list.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            ImageView appmanager_adapter_network_images;
            TextView appmanager_adapter_network_install_text;

            public DataObjectHolder(View itemView) {
                super(itemView);
                appmanager_adapter_network_images = (ImageView) itemView.findViewById(R.id.appmanager_adapter_network_images);
                appmanager_adapter_network_install_text = (TextView) itemView.findViewById(R.id.appmanager_adapter_network_install_text);

            }
        }
    }

    public void mDownloadApk(String url, Context context, String name) {
        if (Build.VERSION.SDK_INT < 9) {
            throw new RuntimeException("Method requires API level 9 or above");
        }

        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= 11) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setTitle(URLUtil.guessFileName(url, "", Intent.ACTION_VIEW));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, "", Intent.ACTION_VIEW));

        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            try {
                dm.enqueue(request);
                Toast.makeText(getActivity(), "Downloading File", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                if (Build.VERSION.SDK_INT >= 11) {
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                }
                dm.enqueue(request);
                Toast.makeText(getActivity(), "Downloading File", Toast.LENGTH_LONG).show();
            }

        }
        // if the download manager app has been disabled on the device
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
