package pos.store.morphsys.com.morphsysstoreapp.adapters;

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
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;


public class ItemListAdapter extends ArrayAdapter {

    ArrayList<CartPOJO> list;
    ItemListAdapter customItemListAdapter;

    public ItemListAdapter(Context context, int textViewResourceId, ArrayList objects) {
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
        row = inflater.inflate(R.layout.cart_item_custom_array_adapter, null);

        TextView txtItem = (TextView) row.findViewById(R.id.txtItem);
        TextView txtPriceQty = (TextView) row.findViewById(R.id.txtPriceQty);

        final CartPOJO cartPOJO = list.get(position);

        double total = cartPOJO.getBasePrice()*cartPOJO.getQuantity();

        txtItem.setText(cartPOJO.getDescription());
        txtPriceQty.setText(cartPOJO.getBasePrice() +" * "+cartPOJO.getQuantity()+" = "+total);

        row.setBackgroundColor(Color.LTGRAY);
        if (position % 2 == 1) {
            row.setAlpha(.75f);
        }
        return row;
    }
}
