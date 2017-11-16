package com.store.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.CustomerPOJO;

import java.util.ArrayList;

public class CustomerListAdapter extends ArrayAdapter {

    private ArrayList<CustomerPOJO> mItems;

    public CustomerListAdapter(Context context, int textViewResourceId, ArrayList<CustomerPOJO> objects) {
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
        view = inflater.inflate(R.layout.customer, null);

        TextView txtName = (TextView) view.findViewById(R.id.txtCustomerheader);
        TextView txtMails = (TextView) view.findViewById(R.id.txtItem);
        TextView txtMobile = (TextView) view.findViewById(R.id.txtMobile) ;

        CustomerPOJO cPOJO = mItems.get(position);

        txtName.setText(cPOJO.getFirstName() +" "+cPOJO.getLastName());
        txtMails.setText("Email : "+cPOJO.getEmail()+" \nFB : "+cPOJO.getFb());
        txtMobile.setText("Mobile Number : "+cPOJO.getMobileNo());

        txtName.setFocusable(false);
        txtMails.setFocusable(false);
        txtMobile.setFocusable(false);

        view.setTag(cPOJO);
        return view;
    }
}
