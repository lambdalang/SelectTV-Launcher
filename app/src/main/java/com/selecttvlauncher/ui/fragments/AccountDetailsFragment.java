package com.selecttvlauncher.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountDetailsFragment.OnAccontDetailFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView basic_title_textView,first_name_textView,last_name_textView,email_textView,zip_textView,optional_title_textView,im_am_textView,dob_textView,addr_textView,suite_textView,
            city_textView,state_textView,phone_textView,password_title_textView,old_pswd_textView,new_pswd_textView,confirm_pswd_textView,email_editText;
    private EditText first_name_editText,last_name_editText,zip_editText,addr_editText,suite_editText,city_editText,phone_editText,old_pswd_editText,new_pswd_editText,confirm_pswd_editText;
    private Spinner iam_spinner,month_spinner,date_spinner,year_spinner,state_spinner;
    private Button save_button;

    private Typeface OpenSans_Bold,OpenSans_Regular,OpenSans_Semibold,osl_ttf;

    private OnAccontDetailFragmentInteractionListener mListener;
    private List<String> day_list;
    ArrayList<String> years = new ArrayList<String>();

    private String first_name_input="",last_name_input="",email_input="",zip_input="",sex_input="",date_input="",month_input="",
            year_input="",street_input="",suite_input="",city_input="",phone_input="",state_input="",old_pass_input="",new_pass_input="",confirm_pass_input="";
    JSONObject data;

    public AccountDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountDetailsFragment newInstance(String param1, String param2) {
        AccountDetailsFragment fragment = new AccountDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
        text_font_typeface();

        String[] sex = new String[] {"", "Male", "Female"};
        day_list=new ArrayList<>();
        for(int i=0;i<=31;i++){
            if(i==0){
                day_list.add("--");
            }else{
                day_list.add(""+i);
            }

        }


        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1900; i <= thisYear-16; i++) {
            if(i==1900){
                years.add("--");
            }else {
                years.add(Integer.toString(i));
            }
        }

        basic_title_textView=(TextView)view.findViewById(R.id.basic_title_textView);
        first_name_textView=(TextView)view.findViewById(R.id.first_name_textView);
        last_name_textView=(TextView)view.findViewById(R.id.last_name_textView);
        email_textView=(TextView)view.findViewById(R.id.email_textView);
        zip_textView=(TextView)view.findViewById(R.id.zip_textView);
        optional_title_textView=(TextView)view.findViewById(R.id.optional_title_textView);
        im_am_textView=(TextView)view.findViewById(R.id.im_am_textView);
        city_textView=(TextView)view.findViewById(R.id.city_textView);
        dob_textView=(TextView)view.findViewById(R.id.dob_textView);
        addr_textView=(TextView)view.findViewById(R.id.addr_textView);
        suite_textView=(TextView)view.findViewById(R.id.suite_textView);
        state_textView=(TextView)view.findViewById(R.id.state_textView);
        phone_textView=(TextView)view.findViewById(R.id.phone_textView);
        password_title_textView=(TextView)view.findViewById(R.id.password_title_textView);
        old_pswd_textView=(TextView)view.findViewById(R.id.old_pswd_textView);
        new_pswd_textView=(TextView)view.findViewById(R.id.new_pswd_textView);
        confirm_pswd_textView=(TextView)view.findViewById(R.id.confirm_pswd_textView);

        first_name_editText=(EditText)view.findViewById(R.id.first_name_editText);
        last_name_editText=(EditText)view.findViewById(R.id.last_name_editText);
        email_editText=(TextView)view.findViewById(R.id.email_editText);
        zip_editText=(EditText)view.findViewById(R.id.zip_editText);
        addr_editText=(EditText)view.findViewById(R.id.addr_editText);
        suite_editText=(EditText)view.findViewById(R.id.suite_editText);
        city_editText=(EditText)view.findViewById(R.id.city_editText);
        phone_editText=(EditText)view.findViewById(R.id.phone_editText);
        old_pswd_editText=(EditText)view.findViewById(R.id.old_pswd_editText);
        new_pswd_editText=(EditText)view.findViewById(R.id.new_pswd_editText);
        confirm_pswd_editText=(EditText)view.findViewById(R.id.confirm_pswd_editText);

        save_button=(Button)view.findViewById(R.id.save_button);

        iam_spinner=(Spinner)view.findViewById(R.id.iam_spinner);
        state_spinner=(Spinner)view.findViewById(R.id.state_spinner);
        month_spinner=(Spinner)view.findViewById(R.id.month_spinner);
        date_spinner=(Spinner)view.findViewById(R.id.date_spinner);
        year_spinner=(Spinner)view.findViewById(R.id.year_spinner);

        basic_title_textView.setTypeface(OpenSans_Regular);
        first_name_textView.setTypeface(OpenSans_Regular);
        last_name_textView.setTypeface(OpenSans_Regular);
        email_textView.setTypeface(OpenSans_Regular);
        zip_textView.setTypeface(OpenSans_Regular);
        optional_title_textView.setTypeface(OpenSans_Regular);
        im_am_textView.setTypeface(OpenSans_Regular);
        dob_textView.setTypeface(OpenSans_Regular);
        addr_textView.setTypeface(OpenSans_Regular);
        suite_textView.setTypeface(OpenSans_Regular);
        city_textView.setTypeface(OpenSans_Regular);
        state_textView.setTypeface(OpenSans_Regular);
        phone_textView.setTypeface(OpenSans_Regular);
        password_title_textView.setTypeface(OpenSans_Regular);
        old_pswd_textView.setTypeface(OpenSans_Regular);
        new_pswd_textView.setTypeface(OpenSans_Regular);
        confirm_pswd_textView.setTypeface(OpenSans_Regular);

        first_name_editText.setTypeface(OpenSans_Regular);
        last_name_editText.setTypeface(OpenSans_Regular);
        email_editText.setTypeface(OpenSans_Regular);
        zip_editText.setTypeface(OpenSans_Regular);
        addr_editText.setTypeface(OpenSans_Regular);
        suite_editText.setTypeface(OpenSans_Regular);
        city_editText.setTypeface(OpenSans_Regular);
        phone_editText.setTypeface(OpenSans_Regular);
        old_pswd_editText.setTypeface(OpenSans_Regular);
        new_pswd_editText.setTypeface(OpenSans_Regular);
        confirm_pswd_editText.setTypeface(OpenSans_Regular);

        save_button.setTypeface(OpenSans_Regular);

        final String first_name=PreferenceManager.getfirst_name(getActivity());
        if(!TextUtils.isEmpty(first_name)&&(!first_name.equalsIgnoreCase("null"))){
            first_name_editText.setText(first_name);
        }
        final String last_name=PreferenceManager.getlast_name(getActivity());
        if(!TextUtils.isEmpty(last_name)&&(!last_name.equalsIgnoreCase("null"))){
            last_name_editText.setText(last_name);
        }
        final String email=PreferenceManager.getemail(getActivity());
        if(!TextUtils.isEmpty(email)&&(!email.equalsIgnoreCase("null"))){
            email_editText.setText(email);
        }
        String post_code=PreferenceManager.getpostal_code(getActivity());
        if(!TextUtils.isEmpty(post_code)&&(!post_code.equalsIgnoreCase("null"))){
            zip_editText.setText(post_code);
        }

        final String city=PreferenceManager.getcity(getActivity());
        if(!TextUtils.isEmpty(city)&&(!city.equalsIgnoreCase("null"))){
            city_editText.setText(city);
        }

        String phone=PreferenceManager.getphone_number(getActivity());
        if(!TextUtils.isEmpty(phone)&&(!phone.equalsIgnoreCase("null"))){
            phone_editText.setText(phone);
        }
        String street=PreferenceManager.getaddress_1(getActivity());
        if(!TextUtils.isEmpty(street)&&(!street.equalsIgnoreCase("null"))){
            addr_editText.setText(street);
        }
        String suite=PreferenceManager.getaddress_2(getActivity());
        if(!TextUtils.isEmpty(suite)&&(!suite.equalsIgnoreCase("null"))){
            suite_editText.setText(suite);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnerlayout, sex);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        iam_spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinnerlayout, getResources().getStringArray(R.array.state_array));
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
        state_spinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinnerlayout, getResources().getStringArray(R.array.month_array));
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        month_spinner.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), R.layout.spinnerlayout, day_list);
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown);
        date_spinner.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), R.layout.spinnerlayout, years);
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown);
        year_spinner.setAdapter(adapter4);

        String gender=PreferenceManager.getgender(getActivity());
        if(!TextUtils.isEmpty(gender)&&(!gender.equalsIgnoreCase("null"))){
            try {
                if(gender.equalsIgnoreCase("M")||gender.equalsIgnoreCase("male")){
                    iam_spinner.setSelection(1);
                }


                if(gender.equalsIgnoreCase("f")||gender.equalsIgnoreCase("female")){
                    iam_spinner.setSelection(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String dob1=PreferenceManager.getdate_of_birth(getActivity());
        if(!TextUtils.isEmpty(dob1)&&(!dob1.equalsIgnoreCase("null"))){
            try {
                String[] separated = dob1.split("-");
                int month_pos=Integer.parseInt(separated[1]);
                String[] month_array=getResources().getStringArray(R.array.month_array);
                int spinnerPosition = adapter2.getPosition(month_array[month_pos + 1]);
                month_spinner.setSelection(spinnerPosition);

                int day_pos=Integer.parseInt(separated[2]);
                // int spinnPosition = adapter3.getPosition(day_pos+1);
                date_spinner.setSelection(day_pos+1);

                String year_pos=separated[0];
                int spinPosition = adapter4.getPosition(year_pos);
                year_spinner.setSelection(spinPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data=new JSONObject();

                first_name_input=first_name_editText.getText().toString();
                last_name_input=last_name_editText.getText().toString();
                email_input=email_editText.getText().toString();
                zip_input=zip_editText.getText().toString();
                sex_input=iam_spinner.getSelectedItem().toString();
                date_input=date_spinner.getSelectedItem().toString();
                month_input=month_spinner.getSelectedItem().toString();
                year_input=year_spinner.getSelectedItem().toString();
                street_input=addr_editText.getText().toString();
                suite_input=suite_editText.getText().toString();
                city_input=city_editText.getText().toString();
                phone_input=phone_editText.getText().toString();
                state_input=state_spinner.getSelectedItem().toString();

                old_pass_input=old_pswd_editText.getText().toString();
                new_pass_input=new_pswd_editText.getText().toString();
                confirm_pass_input=confirm_pswd_editText.getText().toString();

                if(TextUtils.isEmpty(first_name_input)){
                    Utilities.showAlert(getActivity(),"Enter First Name.");
                }else if(TextUtils.isEmpty(last_name_input)){
                    Utilities.showAlert(getActivity(),"Enter Last Name.");
                }else if(TextUtils.isEmpty(zip_input)){
                    Utilities.showAlert(getActivity(),"Enter Zip Code.");
                }else{
                    try {
                        data.put("first_name",first_name_input);
                        data.put("last_name",last_name_input);

                        if(iam_spinner.getSelectedItemPosition()!=0){
                            if(!TextUtils.isEmpty(sex_input)){
                                data.put("gender",sex_input);
                            }
                        }

                        if((month_spinner.getSelectedItemPosition()!=0)&&(date_spinner.getSelectedItemPosition()!=0)&&(year_spinner.getSelectedItemPosition()!=0)){
                            if(!TextUtils.isEmpty(date_input)||!TextUtils.isEmpty(month_input)||!TextUtils.isEmpty(year_input)){
                                int dob=month_spinner.getSelectedItemPosition()+1;
                                data.put("date_of_birth",year_input+"-"+dob+"-"+date_input);
                            }
                        }

                        if(!TextUtils.isEmpty(zip_input)){
                            data.put("postal_code",zip_input);
                        }
                        if(!TextUtils.isEmpty(street_input)){
                            data.put("address_1",street_input);
                        }
                        if(!TextUtils.isEmpty(suite_input)){
                            data.put("address_2",suite_input);
                        }
                        if(!TextUtils.isEmpty(city_input)){
                            data.put("city",city_input);
                        }
                        if(state_spinner.getSelectedItemPosition()!=0){
                            if(!TextUtils.isEmpty(state_input)){
                                data.put("state",state_input);
                            }
                        }

                        if(!TextUtils.isEmpty(phone_input)){
                            data.put("phone_number", phone_input);
                        }

                        Log.d("data::","data:::"+data);
                        updateProfiledata(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(!TextUtils.isEmpty(old_pass_input)){
                    if(TextUtils.isEmpty(new_pass_input)||TextUtils.isEmpty(confirm_pass_input)){
                        Utilities.showAlert(getActivity(),"Password field is Empty.");
                    }else if(!new_pass_input.equalsIgnoreCase(confirm_pass_input)){
                        Utilities.showAlert(getActivity(),"New password not matched");
                    }else{
                        updatePassword(old_pass_input,new_pass_input);
                    }

                }

            }
        });

        return view;
    }

    private void updatePassword(String old_pass_input1, String new_pass_input1) {
        new Updatepassword().execute(old_pass_input1,new_pass_input1);
    }

    private void updateProfiledata(JSONObject data) {
        new Updateprofileinfo().execute();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAccontDetailFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccontDetailFragmentInteractionListener) {
            mListener = (OnAccontDetailFragmentInteractionListener) context;
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
    public interface OnAccontDetailFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAccontDetailFragmentInteraction(Uri uri);
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

    private class Updateprofileinfo extends AsyncTask<Object, Object, Object> {
        ProgressDialog progressDialog;
        JSONObject response;
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(getActivity(), "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            response = JSONRPCAPI.updateUserProfile(PreferenceManager.getAccessToken(getActivity()),data);
            if ( response == null) return null;
            Log.d("response::","update:::"+response);

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            boolean succ_status=false;
            try {
                if(response!=null){
                    try {
                        if(response.has("success")){
                            if(response.getBoolean("success")){
                                succ_status=true;
                                Toast.makeText(getActivity(),"Profile Updated Sucessfullly",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"Profile Updation Failed",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"Profile Updation Failed",Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if(succ_status){
                    new getUserDetails().execute();
                }

            }
        }
    }

    private class getUserDetails extends AsyncTask<String, Object, Object> {
        JSONObject m_response;

        ProgressDialog progressDialog;

        @Override
        protected Object doInBackground(String... params) {
            try {
                Log.d("accesstoken::", "::" + PreferenceManager.getAccessToken(getActivity()));

                m_response = JSONRPCAPI.getUserProfile(PreferenceManager.getAccessToken(getActivity()));
                if (m_response == null) return null;
                Log.d("m_response::", "::" + m_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = ProgressDialog.show(getActivity(), "Please wait", "...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (m_response != null) {
                    if (m_response.has("username")) {
                        String username = m_response.getString("username");
                        PreferenceManager.setusername(username, getActivity());
                    }
                    if (m_response.has("city")) {
                        String city = m_response.getString("city");
                        PreferenceManager.setcity(city, getActivity());
                    }
                    if (m_response.has("first_name")) {
                        String first_name = m_response.getString("first_name");
                        PreferenceManager.setfirst_name(first_name, getActivity());
                    }
                    if (m_response.has("last_name")) {
                        String last_name = m_response.getString("last_name");
                        PreferenceManager.setlast_name(last_name, getActivity());
                    }
                    if (m_response.has("gender")) {
                        String gender = m_response.getString("gender");
                        PreferenceManager.setgender(gender, getActivity());
                    }
                    if (m_response.has("email")) {
                        String email = m_response.getString("email");
                        PreferenceManager.setemail(email, getActivity());
                    }
                    if (m_response.has("state")) {
                        String state = m_response.getString("state");
                        PreferenceManager.setstate(state, getActivity());
                    }
                    if (m_response.has("date_of_birth")) {
                        String date_of_birth = m_response.getString("date_of_birth");
                        PreferenceManager.setdate_of_birth(date_of_birth, getActivity());
                    }
                    if (m_response.has("last_login")) {
                        String last_login = m_response.getString("last_login");
                        PreferenceManager.setlast_login(last_login, getActivity());
                    }
                    if (m_response.has("address_1")) {
                        String address_1 = m_response.getString("address_1");
                        PreferenceManager.setaddress_1(address_1, getActivity());
                    }
                    if (m_response.has("address_2")) {
                        String address_2 = m_response.getString("address_2");
                        PreferenceManager.setaddress_2(address_2, getActivity());

                    }
                    if (m_response.has("postal_code")) {
                        String postal_code = m_response.getString("postal_code");
                        PreferenceManager.setpostal_code(postal_code, getActivity());
                    }
                    if (m_response.has("phone_number")) {
                        String phone_number = m_response.getString("phone_number");
                        PreferenceManager.setphone_number(phone_number, getActivity());
                    }
                    if (m_response.has("id")) {
                        int id = m_response.getInt("id");
                        PreferenceManager.setid(id, getActivity());
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

            }
        }
    }



    private class Updatepassword extends AsyncTask<String, Object, Object> {
        ProgressDialog progressDialog1;
        JSONObject response;
        @Override
        protected void onPreExecute() {
            try {
                progressDialog1 = ProgressDialog.show(getActivity(), "Please wait", "...", false);
                progressDialog1.setCanceledOnTouchOutside(false);
                progressDialog1.setCancelable(false);
                progressDialog1.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            response = JSONRPCAPI.updatepassword(PreferenceManager.getAccessToken(getActivity()),params[0],params[1]);
            if ( response == null) return null;
            Log.d("response::","update:::"+response);

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if(response!=null){
                    try {
                        if(response.has("success")){
                            if(response.getBoolean("success")){
                                Toast.makeText(getActivity(),"Password Updated Sucessfullly",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"Password Updation Failed",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"Password Updation Failed",Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                if (progressDialog1 != null) {
                    progressDialog1.dismiss();
                }
            }
        }
    }
}
