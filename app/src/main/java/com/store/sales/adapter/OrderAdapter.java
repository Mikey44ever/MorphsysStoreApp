package com.store.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.OrderPOJO;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter {

    private ArrayList<OrderPOJO> mItems;

    public OrderAdapter(Context context, int textViewResourceId, ArrayList<OrderPOJO> objects) {
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
        view = inflater.inflate(R.layout.orders, null);
        TextView txtPOID = (TextView) view.findViewById(R.id.txtItemNumber);
        TextView txtDate = (TextView) view.findViewById(R.id.txtItem);
        TextView txtName = (TextView) view.findViewById(R.id.txtCustomerheader);
        TextView txtQty = (TextView) view.findViewById(R.id.txtQty);
        OrderPOJO orderPOJO = mItems.get(position);

        txtPOID.setText(orderPOJO.getPoId());
        txtDate.setText(orderPOJO.getDate());
        txtName.setText(orderPOJO.getName());
        txtQty.setText(orderPOJO.getQty());
        return view;
    }
}
