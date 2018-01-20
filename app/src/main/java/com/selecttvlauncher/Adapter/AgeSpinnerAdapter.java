package com.selecttvlauncher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.selecttvlauncher.R;

import java.util.List;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/25/2016.
 */
public class AgeSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> itemList;
    LayoutInflater inflater;
    public AgeSpinnerAdapter(Context context, int textViewResourceId,
                             List<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.itemList = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinneritem, parent, false);

        /***** Get each Model object from Arraylist ********/
        TextView label        = (TextView)row.findViewById(R.id.textCategory);
        if( itemList != null ){
            String item = itemList.get(position);
            if( item != null )
                label.setText(item);
        }
        return row;
    }

}
