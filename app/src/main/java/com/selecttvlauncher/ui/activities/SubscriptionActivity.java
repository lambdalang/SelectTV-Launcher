package com.selecttvlauncher.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.BeanClass.UserAllSubscriptionBean;
import com.selecttvlauncher.BeanClass.UserSubscriptionBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utils;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SubscriptionActivity extends AppCompatActivity {

    private Context mContext;
    private ArrayList<UserSubscriptionBean> subscriptionList=new ArrayList<>();
    private RecyclerView subscription_list;
    private AQuery aq;
    private Button set_button,skip_button;
    private ImageView switch_image;

    private boolean iscableSelected=false;
    private ArrayList<HorizontalListitemBean> horizontal_list_date=new ArrayList<>();
    private HashMap<String,UserSubscriptionBean> user_map=new HashMap<String,UserSubscriptionBean>();
    private ArrayList<UserAllSubscriptionBean> all_subscriptionList=new ArrayList<>();

    private static double list_width=0;
    private int slider_image_width = 0;
    private LayoutInflater mlayoutinflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_layout);

        mContext=this;
        PreferenceManager.setFirstLogin(false,mContext);
        aq=new AQuery(this);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        int listwidth=(width/5)*3;

        mlayoutinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            list_width= (0.9 * height*0.8);
        } else {
            list_width= (0.9 * width*0.8);
        }
        slider_image_width = (int) (list_width);



        subscription_list=(RecyclerView)findViewById(R.id.subscription_list);
        set_button=(Button)findViewById(R.id.set_button);
        skip_button=(Button)findViewById(R.id.skip_button);
        switch_image=(ImageView)findViewById(R.id.switch_image);

        subscription_list.setNestedScrollingEnabled(false);

        /*ViewGroup.LayoutParams params=subscription_list.getLayoutParams();
        params.width=listwidth;
        subscription_list.setLayoutParams(params);
        GridLayoutManager manager = new GridLayoutManager(mContext, 5);


        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);*/
        LinearLayoutManager linear_layoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        subscription_list.setLayoutManager(linear_layoutManager1);
        subscription_list.hasFixedSize();
        int spanCount = 3; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        subscription_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new updateSubscriptions().execute();
                /*Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        switch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap_switch();
            }
        });

        new LoadSubscriptionAPI().execute();
    }

    private void swap_switch() {
        if (iscableSelected) {
            iscableSelected = false;
            switch_image.setImageResource(R.drawable.off);


        } else {
            iscableSelected = true;
            switch_image.setImageResource(R.drawable.on);
        }
    }
    private class updateSubscriptions extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        JSONObject result_array;
        JSONObject mJsonobj;
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(mContext, "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            String value="";


            try {
                if(subscriptionList!=null&&subscriptionList.size()>0){
                    for(int i=0;i<subscriptionList.size();i++){
                        if(subscriptionList.get(i).isSelected()){
                            if(!TextUtils.isEmpty(value)){
                                value+=",";
                            }
                            value+=subscriptionList.get(i).getCode();

                        }

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(iscableSelected){
                if(!TextUtils.isEmpty(value)){
                    value="CABLE,"+value;
                }else{
                    value+="CABLE";
                }
            }
            if(!TextUtils.isEmpty(value)){
                String list[]=value.split(",");
                PreferenceManager.setSubscribedList(list,mContext);
            }else{
                String list[]=new String[1];
                PreferenceManager.setSubscribedList(list,mContext);
            }


            result_array = JSONRPCAPI.setUserSubscription(PreferenceManager.getAccessToken(mContext),value);

            /*try {
                if(result_array!=null&&result_array.length()>0){
                    Log.d("result_array:::",":::notnull");
                    Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();

                }else{
                    Log.d("result_array:::",":::null");
                    Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
               /* if (result_array == null) return;



                if(subscriptionList!=null&&subscriptionList.size()>0){

                    SubscriptionAdapter mAdapter=new SubscriptionAdapter(mContext,subscriptionList);
                    subscription_list.setAdapter(mAdapter);





                }*/
                try {
                    try {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(result_array!=null&&result_array.length()>0){
                        Log.d("result_array:::",":::notnull");
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        intent.putExtra("mode",20);
                        startActivity(intent);
                        finish();

                    }else{
                        Log.d("result_array:::",":::null");
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        intent.putExtra("mode",20);
                        startActivity(intent);
                        finish();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LoadSubscriptionAPI extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        JSONArray result_array;
        JSONObject mJsonobj;
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(mContext, "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            result_array = JSONRPCAPI.getUserSubscription(PreferenceManager.getAccessToken(mContext));


            try {
                if(result_array!=null&&result_array.length()>0){
                   for(int i=0;i<result_array.length();i++){
                                        JSONObject result_object=result_array.getJSONObject(i);
                                        String code=result_object.getString("code");
                                        final String image=result_object.getString("image_url");
                                        String name=result_object.getString("name");
                                        String slug=result_object.getString("slug");
                                        final String gray_image_url=result_object.getString("gray_image_url");
                                        boolean subscribed=result_object.getBoolean("subscribed");

                       Log.d("sunscribed::::","::::::"+subscribed);

                       if(code.equalsIgnoreCase("CABLE")){
                           iscableSelected=subscribed;

                       }else{
                           subscriptionList.add(new UserSubscriptionBean(code,image,name,slug,gray_image_url,subscribed));
                       }

                       user_map.put(code,new UserSubscriptionBean(code,image,name,slug,gray_image_url,subscribed));



                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {

                               aq.id(new ImageView(mContext)).image(image);
                               aq.id(new ImageView(mContext)).image(gray_image_url);
                           }
                       });

                                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
               /* if (result_array == null) return;

                if(iscableSelected){
                    switch_image.setImageResource(R.drawable.on);
                }



                    if(subscriptionList!=null&&subscriptionList.size()>0){


                        SubscriptionAdapter mAdapter=new SubscriptionAdapter(mContext,subscriptionList);
                        subscription_list.setAdapter(mAdapter);





                }*/
                if (result_array == null){
                    Toast.makeText(getApplicationContext(),"No Data Found/Session Expired",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    new LoadAllSubscriptionAPI().execute();
                }

                if(iscableSelected){
                    switch_image.setImageResource(R.drawable.on);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class LoadAllSubscriptionAPI extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        JSONArray result_array;
        JSONObject mJsonobj;
        UserSubscriptionBean ub;
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(mContext, "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            result_array = JSONRPCAPI.getAllUserSubscription();


            try {


                if(result_array!=null&&result_array.length()>0){
                    for(int i=0;i<result_array.length();i++){
                        horizontal_list_date=new ArrayList<>();
                        JSONObject result_object=result_array.getJSONObject(i);
                        String image="",name="",slug="",gray_image_url="";
                        boolean subscribed=false;
                        ub=null;

                        String code=result_object.getString("code");


                        if(user_map!=null&&user_map.containsKey(code)){
                            ub=user_map.get(code);
                        }
                        if(result_object.has("image_url")){
                            image=result_object.getString("image_url");
                        }
                        if(TextUtils.isEmpty(image)&&ub!=null){
                            image=ub.getImage_url();
                        }


                        name=result_object.getString("name");
                        slug=result_object.getString("slug");

                        if(result_object.has("gray_image_url")){
                            gray_image_url=result_object.getString("gray_image_url");
                        }
                        if(TextUtils.isEmpty(gray_image_url)&&ub!=null){
                            gray_image_url=ub.getGray_image_url();
                        }

                        if(result_object.has("subscribed")){
                            subscribed=result_object.getBoolean("subscribed");
                        }else if(ub!=null){

                            subscribed=ub.isSelected();
                        }


                        Log.d("sunscribed::::","::::::"+subscribed);


                        if(result_object.has("movies")){
                            JSONArray movies_array=result_object.getJSONArray("movies");
                            if(movies_array!=null&&movies_array.length()>0){
                                for(int j=0;j<movies_array.length();j++){
                                    JSONObject movies_object=movies_array.getJSONObject(j);

                                    int id=0;
                                    String itemsname="",itemscarousel_image="";

                                    if (movies_object.has("id")) {
                                        id = movies_object.getInt("id");
                                    }
                                    if (movies_object.has("name")) {
                                        itemsname = movies_object.getString("name");
                                    }
                                    if (movies_object.has("image")) {
                                        itemscarousel_image = movies_object.getString("image");
                                    }

                                    horizontal_list_date.add(new HorizontalListitemBean(id,itemscarousel_image,"M",itemsname));
                                }
                            }
                        }

                        if(result_object.has("shows")){
                            JSONArray show_array=result_object.getJSONArray("shows");
                            if(show_array!=null&&show_array.length()>0){
                                for(int j=0;j<show_array.length();j++){
                                    JSONObject show_object=show_array.getJSONObject(j);

                                    int id=0;
                                    String itemsname="",itemscarousel_image="";

                                    if (show_object.has("id")) {
                                        id = show_object.getInt("id");
                                    }
                                    if (show_object.has("name")) {
                                        itemsname = show_object.getString("name");
                                    }
                                    if (show_object.has("image")) {
                                        itemscarousel_image = show_object.getString("image");
                                    }

                                    horizontal_list_date.add(new HorizontalListitemBean(id,itemscarousel_image,"S",itemsname));
                                }
                            }
                        }



                        if(horizontal_list_date.size()>0){
                            //all_subscriptionList.add(new UserAllSubscriptionBean(code,image,name,slug,gray_image_url,subscribed,horizontal_list_date));
                        }








                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (result_array == null){
                    Toast.makeText(getApplicationContext(),"No Data Found/Session Expired",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(iscableSelected){
                    switch_image.setImageResource(R.drawable.on);
                }



                if(all_subscriptionList!=null&&all_subscriptionList.size()>0){

                    OnDemandContentAdapter mOnDemandContentAdapter = new OnDemandContentAdapter(all_subscriptionList, mContext);
                    subscription_list.setAdapter(mOnDemandContentAdapter);
                    /*SubscriptionAdapter mAdapter=new SubscriptionAdapter(mContext,subscriptionList);
                    subscription_list.setAdapter(mAdapter);*/





                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.DataObjectHolder> {
        Context context;
        JSONArray jsonBuyItmes;
        ArrayList<UserSubscriptionBean> data_list;
        private String[] sub_list;

        public SubscriptionAdapter(Context context, ArrayList<UserSubscriptionBean> data_list) {
            this.context = context;
            this.data_list = data_list;
            sub_list= PreferenceManager.geSubscribedList(context);
            Log.d("sublist:::",":::start");
            for(int i=0;i<sub_list.length;i++){
                Log.d("sublist:::",":::"+sub_list[i]);
            }

        }

        @Override
        public SubscriptionAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.subscription_item, parent, false);

            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final SubscriptionAdapter.DataObjectHolder holder, final int position) {

            String image = "";
            String grey_image = "";
            String deepLink = "";
            String app_download_link = "";
            String display_name = "";
            String app_required = "";
            String app_link = "";
            String price = "";
            try {
                image = data_list.get(position).getImage_url();
                grey_image = data_list.get(position).getGray_image_url();


                if(!TextUtils.isEmpty(image)){

                    if(data_list.get(position).isSelected()){
                        aq.id(holder.subscription_image).progress(holder.progressBar).image(image);
                    }else{
                        aq.id(holder.subscription_image).progress(holder.progressBar).image(grey_image);
                    }
                }else{
                    if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hulu")){
                        if(data_list.get(position).isSelected()){

                            holder.subscription_image.setImageResource(R.drawable.hulu_active);
                        }else{
                            holder.subscription_image.setImageResource(R.drawable.hulu);
                        }
                    }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("netflix")){
                        if(data_list.get(position).isSelected()){
                            holder.subscription_image.setImageResource(R.drawable.netflix_active);
                        }else{
                            holder.subscription_image.setImageResource(R.drawable.netflix);
                        }
                    }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("amzn")){
                        if(data_list.get(position).isSelected()){
                            holder.subscription_image.setImageResource(R.drawable.amazon_active);
                        }else{
                            holder.subscription_image.setImageResource(R.drawable.amazon);
                        }
                    }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("cbs")){
                        if(data_list.get(position).isSelected()){
                            holder.subscription_image.setImageResource(R.drawable.cbs_active);
                        }else{
                            holder.subscription_image.setImageResource(R.drawable.cbss);
                        }
                    }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hbo_now")){
                        if(data_list.get(position).isSelected()){
                            holder.subscription_image.setImageResource(R.drawable.hbo_active);
                        }else{
                            holder.subscription_image.setImageResource(R.drawable.hbo);
                        }
                    }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("showtime")){
                        if(data_list.get(position).isSelected()){
                            holder.subscription_image.setImageResource(R.drawable.sho_active);
                        }else{
                            holder.subscription_image.setImageResource(R.drawable.sho);
                        }
                    }else if(!TextUtils.isEmpty(image)){
                        aq.id(holder.subscription_image).progress(holder.progressBar).image(image);
                    }

                }

                if(data_list.get(position).isSelected()){
                    holder.tick_imageButton.setVisibility(View.VISIBLE);
                }else{
                    holder.tick_imageButton.setVisibility(View.GONE);
                }



                holder.item.setTag(position);



                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int p=Integer.parseInt(holder.item.getTag().toString());
                        if(data_list.get(p).isSelected()){
                            holder.tick_imageButton.setVisibility(View.GONE);
                            subscriptionList.get(p).setSelected(false);
                           // setLocked(holder.subscription_image);
                        }else{
                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                            subscriptionList.get(p).setSelected(true);
                            //setUnlocked(holder.subscription_image);
                        }

                        if(!TextUtils.isEmpty(data_list.get(position).getImage_url())){

                            if(data_list.get(position).isSelected()){
                                aq.id(holder.subscription_image).progress(holder.progressBar).image(data_list.get(position).getImage_url());
                            }else{
                                aq.id(holder.subscription_image).progress(holder.progressBar).image(data_list.get(position).getGray_image_url());
                            }
                        }else{
                            if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hulu")){
                                if(data_list.get(position).isSelected()){
                                    holder.subscription_image.setImageResource(R.drawable.hulu_active);
                                }else{
                                    holder.subscription_image.setImageResource(R.drawable.hulu);
                                }
                            }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("netflix")){
                                if(data_list.get(position).isSelected()){
                                    holder.subscription_image.setImageResource(R.drawable.netflix_active);
                                }else{
                                    holder.subscription_image.setImageResource(R.drawable.netflix);
                                }
                            }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("amzn")){
                                if(data_list.get(position).isSelected()){
                                    holder.subscription_image.setImageResource(R.drawable.amazon_active);
                                }else{
                                    holder.subscription_image.setImageResource(R.drawable.amazon);
                                }
                            }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("cbs")){
                                if(data_list.get(position).isSelected()){
                                    holder.subscription_image.setImageResource(R.drawable.cbs_active);
                                }else{
                                    holder.subscription_image.setImageResource(R.drawable.cbss);
                                }
                            }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("hbo_now")){
                                if(data_list.get(position).isSelected()){
                                    holder.subscription_image.setImageResource(R.drawable.hbo_active);
                                }else{
                                    holder.subscription_image.setImageResource(R.drawable.hbo);
                                }
                            }else if(data_list.get(position).getCode().toLowerCase().equalsIgnoreCase("showtime")){
                                if(data_list.get(position).isSelected()){
                                    holder.subscription_image.setImageResource(R.drawable.sho_active);
                                }else{
                                    holder.subscription_image.setImageResource(R.drawable.sho);
                                }
                            }
                        }



                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return data_list.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            RelativeLayout item;
            ImageView tick_imageButton;
            GridViewItem subscription_image;
            ProgressBar progressBar;

            public DataObjectHolder(View itemView) {
                super(itemView);
                item = (RelativeLayout) itemView.findViewById(R.id.item);
                tick_imageButton = (ImageView) itemView.findViewById(R.id.tick_imageButton);
                subscription_image = (GridViewItem) itemView.findViewById(R.id.subscription_image);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            }
        }
    }

    public static void  setLocked(ImageView v)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        //v.setAlpha(128);   // 128 = 0.5
    }

    public static void  setUnlocked(ImageView v)
    {
        v.setColorFilter(null);
        //v.setAlpha(255);
    }

    class OnDemandContentAdapter extends RecyclerView.Adapter<OnDemandContentAdapter.DataObjectHolder> {
        ArrayList<UserAllSubscriptionBean> list_data;
        Context context;
        private int mSelectedItem = 0;

        public OnDemandContentAdapter(ArrayList<UserAllSubscriptionBean> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
        }


        @Override
        public OnDemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_item, parent, false);
            return new OnDemandContentAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final OnDemandContentAdapter.DataObjectHolder holder, final int position) {
            try {
                slider_image_width = (int) (list_width);
                holder.horizontal_listview_title.setText(Utils.stripHtml(list_data.get(position).getName()));
                holder.network_imageView.setTag(position);
                holder.installed_textView.setTag(position);

                String image_url="";
                if(list_data.get(position).isSelected()){
                    image_url=list_data.get(position).getImage_url();
                }else{
                    image_url=list_data.get(position).getGray_image_url();
                }

                Log.d("network image:::",":::url:::"+image_url);
                if(!TextUtils.isEmpty(image_url)){
                    aq.id(holder.network_imageView).image(image_url);
                }

                try {
                    holder.dynamic_image_layout.removeAllViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }



                if (list_data.get(position).isSelected()) {
                    holder.installed_textView.setVisibility(View.VISIBLE);
                    holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
                    holder.installed_textView.setText("Subscribed");
                    holder.tick_imageButton.setVisibility(View.VISIBLE);
                } else {
                    holder.installed_textView.setVisibility(View.VISIBLE);
                    holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
                    holder.installed_textView.setText("UnSubscribed");
                    holder.tick_imageButton.setVisibility(View.GONE);
                }
                holder.installed_textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=Integer.parseInt(holder.installed_textView.getTag().toString());
                        list_data.get(pos).setSelected(!list_data.get(pos).isSelected());

                        if (list_data.get(position).isSelected()) {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
                            holder.installed_textView.setText("Subscribed");
                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                            if(!TextUtils.isEmpty(list_data.get(pos).getImage_url())){
                                aq.id(holder.network_imageView).image(list_data.get(pos).getImage_url());
                            }
                        } else {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
                            holder.installed_textView.setText("UnSubscribed");
                            holder.tick_imageButton.setVisibility(View.GONE);
                            if(!TextUtils.isEmpty(list_data.get(pos).getGray_image_url())){
                                aq.id(holder.network_imageView).image(list_data.get(pos).getGray_image_url());
                            }
                        }
                    }
                });
                holder.network_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=Integer.parseInt(holder.network_imageView.getTag().toString());
                        list_data.get(pos).setSelected(!list_data.get(pos).isSelected());

                        if (list_data.get(position).isSelected()) {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.subscribed_selected);
                            holder.installed_textView.setText("Subscribed");
                            holder.tick_imageButton.setVisibility(View.VISIBLE);
                            if(!TextUtils.isEmpty(list_data.get(pos).getImage_url())){
                                aq.id(holder.network_imageView).image(list_data.get(pos).getImage_url());
                            }
                        } else {
                            holder.installed_textView.setVisibility(View.VISIBLE);
                            holder.installed_textView.setBackgroundResource(R.drawable.bg_installed_app);
                            holder.installed_textView.setText("UnSubscribed");
                            holder.tick_imageButton.setVisibility(View.GONE);
                            if(!TextUtils.isEmpty(list_data.get(pos).getGray_image_url())){
                                aq.id(holder.network_imageView).image(list_data.get(pos).getGray_image_url());
                            }
                        }
                    }
                });


                for (int l = 0; l < list_data.get(position).getData_list().size(); l++) {
                    final int ll=l;

                    final View itemmlayout = (View) mlayoutinflater.inflate(R.layout.horizontal_list_item, null);
                    itemmlayout.setTag(position);

                    DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                    LinearLayout.LayoutParams vp;
                    if(checkIsTablet()){
                        vp = new LinearLayout.LayoutParams((slider_image_width - 40) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                        if (l != 0) {
                            vp.setMargins(20, 0, 0, 0);
                        }
                    }else{

                        if(list_data.get(position).getData_list().get(l).getType().equalsIgnoreCase("m"))
                        {
                            vp = new LinearLayout.LayoutParams((slider_image_width - 20) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                        }else
                        {
                            vp = new LinearLayout.LayoutParams((slider_image_width - 20) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);


                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                        }

                    }


                    String image_url_scroll = list_data.get(position).getData_list().get(l).getImage();
                    Log.d("image_url::", "image_url" + image_url);
                    horizontal_imageView.loadImage(image_url_scroll);
                    horizontal_imageView.setLayoutParams(vp);
                    final int finalL = l;
                    itemmlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos=Integer.parseInt(itemmlayout.getTag().toString());
                            if (!list_data.get(pos).isSelected()) {
                               /* FragmentManager fm = getActivity().getSupportFragmentManager();
                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+list_data.get(position).getNetwork_name(),""+list_data.get(position).getNetwork_image(),list_data.get(position).getNetwork_link());

                                dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");*/
                            }else{
                                /*Intent intent = new Intent(SubscriptionActivity.this, DetailsActivity.class);
                                intent.putExtra("showid",list_data.get(position).getData_list().get(ll).getId());
                                intent.putExtra("type",list_data.get(position).getData_list().get(ll).getType());
                                intent.putExtra("name",list_data.get(position).getData_list().get(ll).getName());
                                Log.d("type",""+list_data.get(position).getData_list().get(ll).getType());
                                startActivity(intent);*/
                            }

                        }
                    });
                    holder.dynamic_image_layout.addView(itemmlayout);

                }





            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            HorizontalScrollView horizontal_listview;
            TextView horizontal_listview_title, view_all_text,installed_textView;
            LinearLayout recycler_layout, image_view,dynamic_image_layout;
            ImageView left_slide, right_slide,tick_imageButton;
            GridViewItem network_imageView;
            LinearLayout network_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                network_imageView = (GridViewItem) itemView.findViewById(R.id.network_imageViewv);
                left_slide = (ImageView) itemView.findViewById(R.id.left_slide);
                right_slide = (ImageView) itemView.findViewById(R.id.right_slide);
                tick_imageButton = (ImageView) itemView.findViewById(R.id.tick_imageButton);

                installed_textView = (TextView) itemView.findViewById(R.id.installed_textView);
                view_all_text = (TextView) itemView.findViewById(R.id.view_all_text);
                horizontal_listview_title = (TextView) itemView.findViewById(R.id.horizontal_listview_title);

                dynamic_image_layout = (LinearLayout) itemView.findViewById(R.id.dynamic_image_layout);
                recycler_layout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);
                network_layout = (LinearLayout) itemView.findViewById(R.id.network_layout);
                horizontal_listview = (HorizontalScrollView) itemView.findViewById(R.id.horizontal_listview);

            }
        }
    }

    private boolean checkIsTablet() {

        try {
            String inputSystem;
            inputSystem = android.os.Build.ID;
            Log.d("hai", inputSystem);
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();  // deprecated
            Log.d("hai", width + "");
            Log.d("hai", height + "");
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(width / dm.xdpi, 2);
            double y = Math.pow(height / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            Log.d("hai", "Screen inches : " + screenInches + "");
            return screenInches > 6.0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
