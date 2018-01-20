package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.service.RadioService;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;


public class FragmentRadioDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1,mParam2;
    TextView radio_name;
    ImageView fragment_radio_left_prev_icon;
    DynamicImageView banner_image;
    private TextView radio_description,Slogan_text,slogan,address_text,address,phone_text,phone,email_text,email,web_text,bitrate,radio_play_button,radio_status;
    private LinearLayout slogan_layout,address_layout,phone_layout,email_layout,web_layout;
    private Intent radioService;
    boolean bNewRadio = false;
    Runnable run;
    Handler handler;
    RadioService mServer;
    boolean mBounded;



    public static FragmentRadioDetails newInstance(String param1, String param2) {
        FragmentRadioDetails fragment = new FragmentRadioDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentRadioDetails() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_radio_details, container, false);
        ((HomeActivity)getActivity()).setmFragmentRadioDetails(this);

        radioService = new Intent(getActivity(), RadioService.class);
       // getActivity().bindService(radioService, mConnection, Context.BIND_AUTO_CREATE);


        banner_image                    =   (DynamicImageView) view.findViewById(R.id.banner_image);
        slogan_layout                   =   (LinearLayout)view.findViewById(R.id.slogan_layout);
        address_layout                  =   (LinearLayout)view.findViewById(R.id.address_layout);
        phone_layout                    =   (LinearLayout)view.findViewById(R.id.phone_layout);
        email_layout                    =   (LinearLayout)view.findViewById(R.id.email_layout);
        web_layout                      =   (LinearLayout)view.findViewById(R.id.web_layout);
        fragment_radio_left_prev_icon   =   (ImageView) view.findViewById(R.id.fragment_radio_left_prev_icon);
        radio_name                      =   (TextView) view.findViewById(R.id.radio_name);
        radio_description               =   (TextView) view.findViewById(R.id.radio_description);
        Slogan_text                     =   (TextView) view.findViewById(R.id.Slogan_text);
        slogan                          =   (TextView) view.findViewById(R.id.slogan);
        address_text                    =   (TextView) view.findViewById(R.id.address_text);
        address                         =   (TextView) view.findViewById(R.id.address);
        phone_text                      =   (TextView) view.findViewById(R.id.phone_text);
        phone                           =   (TextView) view.findViewById(R.id.phone);
        email_text                      =   (TextView) view.findViewById(R.id.email_text);
        email                           =   (TextView) view.findViewById(R.id.email);
        web_text                        =   (TextView) view.findViewById(R.id.web_text);
        bitrate                         =   (TextView) view.findViewById(R.id.bitrate);
        radio_play_button               =   (TextView) view.findViewById(R.id.radio_play_button);
        radio_status                    =   (TextView) view.findViewById(R.id.radio_status);

        if (FragmentListenGrid.radioDetailBeans != null && FragmentListenGrid.radioDetailBeans.size() != 0) {
            banner_image.loadImage(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getImage());
            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getName().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getName().trim().equals("null")) {
                radio_name.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getName());
                radio_name.setVisibility(View.VISIBLE);
            } else {
                radio_name.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getDescription().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getDescription().trim().equals("null")) {
                radio_description.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getDescription());
                radio_description.setVisibility(View.VISIBLE);
            } else {
                radio_description.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getSlogan().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getSlogan().trim().equals("null")) {
                slogan.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getSlogan());
                Slogan_text.setVisibility(View.VISIBLE);
                slogan.setVisibility(View.VISIBLE);
                slogan_layout.setVisibility(View.VISIBLE);
            } else {
                slogan_layout.setVisibility(View.GONE);
                Slogan_text.setVisibility(View.GONE);
                slogan.setVisibility(View.GONE);

            }
            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getAddress().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getAddress().trim().equals("null")) {
                address.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getAddress());
                address_text.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
                address_layout.setVisibility(View.VISIBLE);
            } else {
                address_layout.setVisibility(View.GONE);
                address_text.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getPhone().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getPhone().trim().equals("null")) {
                phone.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getPhone());
                phone_text.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);
                phone_layout.setVisibility(View.VISIBLE);
            } else {
                phone_layout.setVisibility(View.GONE);
                phone_text.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getEmail().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getEmail().trim().equals("null")) {
                email.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getEmail());
                email.setVisibility(View.VISIBLE);
                email_text.setVisibility(View.VISIBLE);
                email_layout.setVisibility(View.VISIBLE);
            } else {

                email_layout.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                email_text.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getBitrate().trim())&&!FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getBitrate().trim().equals("null")) {
                bitrate.setText(FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getBitrate());
                bitrate.setVisibility(View.VISIBLE);
                web_text.setVisibility(View.VISIBLE);
                web_layout.setVisibility(View.VISIBLE);
            } else {
                web_layout.setVisibility(View.GONE);
                bitrate.setVisibility(View.GONE);
                web_text.setVisibility(View.GONE);

            }
        }

        fragment_radio_left_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).swapRadioFragment(false);
            }
        });

        radio_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   /* if(mBounded&&mServer.player.isPlaying()){
                        if( radio_play_button.getText().toString().equalsIgnoreCase("Play") ){
                            mServer.playerResume();
                        }else{
                            mServer.playerPause();
                        }

                    }else{*/
                        String stream_url= FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getStream();
                        String image= FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getImage();
                        String name= FragmentListenGrid.radioDetailBeans.get(Integer.parseInt(mParam1)).getName();
                        Log.d("stream_url:::", ":::" + stream_url);

                        if( radio_play_button.getText().toString().equalsIgnoreCase("Play") ){

                            try {
                                bNewRadio=true;
                                getActivity().stopService(radioService);
                                radioService.putExtra("url", stream_url);
                                radioService.putExtra("image", image);
                                radioService.putExtra("name", name);
                                radioService.putExtra("height",200);
                                if( bNewRadio == true ) {
                                    radioService.setAction(RadioService.ACTION_SETUP_AND_PLAY);

                                }else {
                                    radioService.setAction(RadioService.ACTION_RESUME);
                                }
                                getActivity().startService(radioService);

                                bNewRadio = false;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                radioService.putExtra("url", stream_url);
                                radioService.putExtra("image", image);
                                radioService.putExtra("name", name);
                                radioService.putExtra("height",200);
                                radioService.setAction(RadioService.ACTION_PAUSE);
                                getActivity().startService(radioService);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                  //  }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Utilities.setViewFocus(getActivity(), fragment_radio_left_prev_icon);
        Utilities.setViewFocus(getActivity(),radio_play_button);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            // Toast.makeText(getActivity(), "Service is disconnected", 1000).show();
            mBounded = false;
            mServer = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            RadioService.LocalBinder mLocalBinder = (RadioService.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
        }
    };


    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            if(handler!=null)
            handler.removeCallbacks(run);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(mBounded) {
                getActivity().unbindService(mConnection);
                mBounded = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setRadioBar(Intent intent) {

        try {
            if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_PREPARING)) {
                radio_status.setText("Buffering...(please wait)");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                radio_play_button.setText("Pause");
                radio_play_button.setEnabled(false);
                radio_play_button.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                try {
                    if(handler!=null)
                    handler.removeCallbacks(run);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_PLAYING)) {
                radio_status.setText("Playing...");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.letter_bg_color));
                radio_play_button.setText("Pause");
                radio_play_button.setEnabled(true);
                radio_play_button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                try {
                    if(handler!=null)
                    handler.removeCallbacks(run);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_STOPPED)) {
                radio_status.setText("Stopped");
                radio_play_button.setText("Play");
                radio_play_button.setEnabled(true);
                radio_play_button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                try {
                    if(handler!=null)
                    handler.removeCallbacks(run);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_PREPARE_ERROR)) {
                radio_status.setText("Connecting to station...");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                radio_play_button.setText("Play");
                radio_play_button.setEnabled(true);

                handler = new Handler();
                try {
                    run=new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handler.removeCallbacks(run);

                                if(radio_status.getText().toString().equalsIgnoreCase("Connecting to station... Please wait...")){
                                    radio_status.setText("Error Occured");
                                    radio_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                                    radio_play_button.setEnabled(true);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    handler.postDelayed(run, 60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if( intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_CLOSE)){
                radio_status.setText(" ");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                radio_play_button.setText("Play");
                radio_play_button.setEnabled(true);
                bNewRadio = true;
                try {
                    if(handler!=null)
                    handler.removeCallbacks(run);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_PREPARING)) {
                radio_status.setText("Buffering...(please wait)");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_dark_gray));
                radio_play_button.setText("Stop");
                radio_play_button.setEnabled(false);
                radio_play_button.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_dark_gray));
            } else if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_PLAYING)) {
                radio_status.setText("Playing...");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.letter_bg_color));
                radio_play_button.setText("Stop");
                radio_play_button.setEnabled(true);
                radio_play_button.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            } else if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_STOPPED)) {
                radio_status.setText("Stopped");
                radio_play_button.setText("Play");
                radio_play_button.setEnabled(true);
                radio_play_button.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            } else if (intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_PREPARE_ERROR)) {
                radio_status.setText("Error occured");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
                radio_play_button.setText("Play");
                radio_play_button.setEnabled(true);
            }else if( intent.getAction().equalsIgnoreCase(RadioService.RECIEVER_ACTION_CLOSE)){
                radio_status.setText(" ");
                radio_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
                radio_play_button.setText("Play");
                radio_play_button.setEnabled(true);
                bNewRadio = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }



}
