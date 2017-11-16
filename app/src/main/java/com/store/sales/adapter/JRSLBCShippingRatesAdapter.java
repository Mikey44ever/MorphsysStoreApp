package com.store.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.JRSLBCShippingRatesPOJO;

import java.util.ArrayList;

public class JRSLBCShippingRatesAdapter extends ArrayAdapter {

    private ArrayList<JRSLBCShippingRatesPOJO> mItems;

    public JRSLBCShippingRatesAdapter(Context context, int textViewResourceId, ArrayList<JRSLBCShippingRatesPOJO> objects) {
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
        view = inflater.inflate(R.layout.lj_shipping_rates, null);
        TextView txtQTY = (TextView) view.findViewById(R.id.txtItemNumber);
        TextView txtLBCRates = (TextView) view.findViewById(R.id.txtItem);
        TextView txtJRSRates = (TextView) view.findViewById(R.id.txtJRS);
        JRSLBCShippingRatesPOJO ljPOJO = mItems.get(position);

        txtQTY.setText(ljPOJO.getQty());
        txtLBCRates.setText(ljPOJO.getLbcRate());
        txtJRSRates.setText(ljPOJO.getJrsRate());

        return view;
    }
}
