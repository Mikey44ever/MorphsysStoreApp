package com.store.sales.adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;

import java.util.ArrayList;

public class DashBoardAdapter  extends ArrayAdapter {

    final ArrayList<String> mItems;

    public DashBoardAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        mItems = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        final TextView text = (TextView) view.findViewById(android.R.id.text1);
        text.setText(mItems.get(position) != null ? mItems.get(position) : "");
        text.setGravity(Gravity.CENTER_VERTICAL);
        view.setBackgroundResource(R.drawable.dashboard_screen__bg);

        return view;
    }
}
