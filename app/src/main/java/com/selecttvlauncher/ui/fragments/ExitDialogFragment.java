package com.selecttvlauncher.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.views.DynamicSquareImageview;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExitDialogFragment.OnExitFragmentInteractionListener } interface
 * to handle interaction events.
 * Use the {@link ExitDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExitDialogFragment extends android.support.v4.app.DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnExitFragmentInteractionListener  mListener;

    private TextView header_textView,note_textView,cancle_textView,install_textView,title_textView;
    private DynamicSquareImageview network_imageView;
    private LinearLayout whole_layout;

    public ExitDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExitDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExitDialogFragment newInstance(String param1, String param2) {
        ExitDialogFragment fragment = new ExitDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.exit_newdailoglayout, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));


        network_imageView=(DynamicSquareImageview) view.findViewById(R.id.network_imageView);
        header_textView=(TextView)view.findViewById(R.id.header_textView);
        title_textView=(TextView)view.findViewById(R.id.title_textView);
        cancle_textView=(TextView) view.findViewById(R.id.cancle_textView);
        note_textView=(TextView)view.findViewById(R.id.note_textView);
        install_textView=(TextView) view.findViewById(R.id.install_textView);

//        title_textView.setText(getString(R.string.exit_title));
        header_textView.setText(getString(R.string.exit_subtitle));
        network_imageView.setImageResource(R.drawable.home_icon);

      /*  BitmapDrawable bd=(BitmapDrawable) ContextCompat.getDrawable(getContext(),R.drawable.pop_bg);
        int height=bd.getBitmap().getHeight();
        int width=bd.getBitmap().getWidth();

        Log.d("layoutwidth::", "width" + width);*/

        String text = "<font color=#ffffff><b>Note: </b>After you exit, you will be prompted to select your favorite </font> <font color=#F8C100> home app </font> <font color=#FFFFFF>which will return to your normal mobile desktop.</font>";
        note_textView.setText(Html.fromHtml(text));

        cancle_textView.setText(getString(R.string.exit_dialog_cancel_btn));
        install_textView.setText(getString(R.string.exit_dialog_exit_btn));

        install_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if(mListener!=null){
                        mListener.exitlauncher();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        cancle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    getDialog().dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExitFragmentInteractionListener ) {
            mListener = (OnExitFragmentInteractionListener ) context;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExitFragmentInteractionListener {
        // TODO: Update argument type and name
        void exitlauncher();
    }
}
