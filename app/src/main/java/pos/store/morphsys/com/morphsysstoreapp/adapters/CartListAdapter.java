package pos.store.morphsys.com.morphsysstoreapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.store.R;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartListPOJO;

public class CartListAdapter  extends ArrayAdapter {

    ArrayList<CartListPOJO> list;
    TextView txtCartId,txtQty,txtCost,txtDate,txtStatus;
    Activity activity;

    public CartListAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    public void setActivity(final Activity activity){
        this.activity=activity;
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
        row = inflater.inflate(R.layout.carts_custom_array_adapter, null);
        txtCartId = (TextView) row.findViewById(R.id.txtCartId);
        txtQty = (TextView) row.findViewById(R.id.txtItemNumber);
        txtCost = (TextView) row.findViewById(R.id.txtCost);
        txtDate = (TextView) row.findViewById(R.id.txtItem);
        txtStatus= (TextView) row.findViewById(R.id.txtStatus);

        CartListPOJO cListPOJO = list.get(position);
        txtCartId.setText("CART ID : "+cListPOJO.getCartId());
        txtQty.setText("QUANTITY : "+String.valueOf(cListPOJO.getQuantity()));
        txtCost.setText("TOTAL : "+String.valueOf(cListPOJO.getCost()));
        txtDate.setText("TRANSACTION DATE : "+cListPOJO.getDate());
        txtStatus.setText("STATUS : "+cListPOJO.getStatus());

        row.setTag(cListPOJO);
        row.setBackgroundColor(Color.LTGRAY);
        if (position % 2 == 1) {
            row.setAlpha(.75f);
        }
        return row;
    }
}
