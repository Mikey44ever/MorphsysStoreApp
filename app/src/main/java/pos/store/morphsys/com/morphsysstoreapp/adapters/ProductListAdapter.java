package pos.store.morphsys.com.morphsysstoreapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import com.store.R;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJOBuilder;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;

public class ProductListAdapter extends ArrayAdapter{

    ArrayList<ProductPOJO> originalCopy;
    ArrayList<ProductPOJO> list = new ArrayList<ProductPOJO>();
    ArrayList suggestions = new ArrayList();
    private ArrayList<CartPOJO> cartList = new ArrayList<CartPOJO>();
    private CartPOJOBuilder cBuilder;
    private CartPOJO cart;
    TextView txtQty;
    Activity activity;

    public ProductListAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        list = objects;
        originalCopy = new ArrayList<>(objects);
    }

    public void setList(ArrayList<CartPOJO>list){
        if(list!=null && list.size()>0)
            this.cartList=list;
    }

    public void setActivity(Activity activity){
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.product_custom_array_adapter, null);
        final TextView textView = (TextView) row.findViewById(R.id.txtProduct);
        ProductPOJO productPOJO = (ProductPOJO) list.get(position);
        textView.setText(productPOJO.getProductName());
        row.setTag(productPOJO);
        final View hiddenRow = row;

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductPOJO rowProduct = (ProductPOJO)  hiddenRow.getTag();
                View itemView = View.inflate(activity, R.layout.item_popup, null);
                TextView txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
                TextView txtPrice = (TextView) itemView.findViewById(R.id.txtPriceQty);
                txtQty = (TextView) itemView.findViewById(R.id.txtItemNumber);

                txtItemName.setText(rowProduct.getProductName());
                txtPrice.setText(String.valueOf(rowProduct.getProductPrice()));

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setView(itemView);
                alert.setPositiveButton("ADD TO CART", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addToCart(rowProduct,txtQty);
                    }
                });
                alert.setNegativeButton("CLOSE",null);

                AlertDialog mainDialog = alert.create();
                mainDialog.show();
                final Button positiveButton = mainDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setEnabled(false);

                txtQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        String qtyValue = txtQty.getText().toString();
                        int qty = qtyValue.equalsIgnoreCase("")?0:Integer.valueOf(qtyValue);
                        if(qty>0)
                            positiveButton.setEnabled(true);
                        else
                            positiveButton.setEnabled(false);
                    }
                });
            }
        });
        return row;
    }

    public ArrayList<CartPOJO> getCartList(){
        return cartList;
    }

    private void addToCart(ProductPOJO rowProduct,TextView txtQty){
        try{
            if(rowProduct!=null){
                cBuilder = new CartPOJOBuilder();
                cart = cBuilder
                        .item(rowProduct.getProductId())
                        .desc(rowProduct.getProductName())
                        .quantity(Integer.valueOf(txtQty.getText().toString()))
                        .basePrice(rowProduct.getProductPrice())
                        .build();
                cartList.add(cart);
            }
        }catch(NumberFormatException e){
            Log.e(null,e.getMessage());
        }
    }

    @NonNull
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            final ArrayList<ProductPOJO> filterList = new ArrayList<>(originalCopy);
            if (charSequence != null) {
                suggestions.clear();
                for (ProductPOJO product : filterList) {
                    if (product.getProductName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }

                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ProductPOJO> c =  (ArrayList<ProductPOJO> )filterResults.values ;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ProductPOJO product : c) {
                    add(product);
                    notifyDataSetChanged();
                }
            } else{
                clear();
                notifyDataSetChanged();
            }
        }
    };
}
