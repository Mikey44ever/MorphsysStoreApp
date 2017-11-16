package com.store.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.ProductPOJO;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter {

    private ArrayList<ProductPOJO> mItems;

    public ProductListAdapter(Context context, int textViewResourceId, ArrayList<ProductPOJO> objects) {
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
        view = inflater.inflate(R.layout.product_list, null);

        TextView txtItemNumber = (TextView) view.findViewById(R.id.txtItemNumber);
        TextView txtItem = (TextView) view.findViewById(R.id.txtItem);
        TextView txtCV = (TextView) view.findViewById(R.id.txtCV);
        final TextView txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        final TextView txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        final TextView txtQty = (TextView) view.findViewById(R.id.txtQty);

        ProductPOJO pPOJO = mItems.get(position);

        int itemNumber = position;

        txtItemNumber.setText(String.valueOf(itemNumber+1));
        txtItem.setText(pPOJO.getItem());
        txtCV.setText(pPOJO.getCv());
        txtPrice.setText(pPOJO.getPrice());
        txtAmount.setText(pPOJO.getPrice());

        txtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int qty = !txtQty.getText().toString().equalsIgnoreCase("") ?Integer.parseInt(txtQty.getText().toString()):0;
                double price = Double.parseDouble(txtPrice.getText().toString());

                if(price>0)
                    txtAmount.setText(String.valueOf(qty*price));
                else
                    txtAmount.setText(txtPrice.getText().toString());
            }
        });

        return view;
    }
}
