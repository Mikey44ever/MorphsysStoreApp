package com.store.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.CODShippingRatesPOJO;

import java.util.ArrayList;

public class CODShippingRatesAdapter extends ArrayAdapter {

    private ArrayList<CODShippingRatesPOJO> mItems;

    public CODShippingRatesAdapter(Context context, int textViewResourceId, ArrayList<CODShippingRatesPOJO> objects) {
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
        view = inflater.inflate(R.layout.cod_shipping_rates, null);
        TextView txtQTY = (TextView) view.findViewById(R.id.txtItemNumber);
        TextView txtRates = (TextView) view.findViewById(R.id.txtItem);
        CODShippingRatesPOJO csrPOJO = mItems.get(position);

        txtQTY.setText(csrPOJO.getQty());
        txtRates.setText(csrPOJO.getRates());

        return view;
    }
}
