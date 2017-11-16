package com.store.sales.adapter;

import android.content.Context;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.VectorEnabledTintResources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.store.R;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter {

    final ArrayList<String> mItems;
    private Activity activity;
    private int textViewResourceId;

    public GridAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId=textViewResourceId;
        mItems = objects;
    }

    public void setActivity(final Activity activity){
        this.activity=activity;
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

        LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
        view = inflater.inflate(textViewResourceId, parent, false);

        final TextView text = (TextView) view.findViewById(R.id.item_text);
        text.setText(mItems.get(position) != null ? mItems.get(position) : "");
        text.setGravity(Gravity.CENTER);
        view.setBackgroundResource(R.drawable.square);

        final ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
        iconSetter(imageView, text.getText().toString());

        return view;
    }

    private void iconSetter(final ImageView imageView,final String text){
        switch (text.toLowerCase()){
            case "customers" :
                imageView.setBackgroundResource(R.drawable.people);
                break;
            case "cod areas" :
                imageView.setBackgroundResource(R.drawable.cod_areas);
                break;
            case "cod shipping rates":
                imageView.setBackgroundResource(R.drawable.rider);
                break;
            case "income" :
                imageView.setBackgroundResource(R.drawable.money);
                break;
            case "orders" :
                imageView.setBackgroundResource(R.drawable.orders);
                break;
            case "lbc/jrs shipping rates":
                imageView.setBackgroundResource(R.drawable.delivery);
                break;
            default:
                imageView.setBackgroundResource(R.drawable.people);
                break;
        }
    }
}
