package com.store.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;

import java.util.ArrayList;

public class CODAreasAdapter extends ArrayAdapter {

    private ArrayList<String> mItems;

    public CODAreasAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        mItems = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.cod_area, null);
        TextView txtArea = (TextView) view.findViewById(R.id.txtItemNumber);

        txtArea.setText(mItems.get(position));

        return view;
    }
}
