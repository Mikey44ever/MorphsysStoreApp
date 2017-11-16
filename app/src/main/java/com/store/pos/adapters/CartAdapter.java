package com.store.pos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.store.pos.R;

import com.store.pos.pojo.cart.CartPOJO;

public class CartAdapter extends ArrayAdapter {

    ArrayList<CartPOJO> list;

    public CartAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        row.setBackgroundResource(R.drawable.background_border);

        TextView textView = (TextView) row.findViewById(android.R.id.text1) ;

        CartPOJO cPOJO = list.get(position);
        textView.setText(cPOJO.toString());
        return row;
    }
}
