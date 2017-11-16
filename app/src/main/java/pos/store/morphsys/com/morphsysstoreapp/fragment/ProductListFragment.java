package pos.store.morphsys.com.morphsysstoreapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.github.kimkevin.cachepot.CachePot;

import java.util.ArrayList;

import com.store.R;
import pos.store.morphsys.com.morphsysstoreapp.adapters.ProductListAdapter;
import com.store.dbs.DBHelper;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJOBuilder;

import static com.store.constants.Constants.CART_POJO_SERIAL_KEY;
import static com.store.constants.Constants.PRICE;
import static com.store.constants.Constants.PRODUCT_ID;
import static com.store.constants.Constants.PRODUCT_NAME;
import static com.store.constants.Constants.VIEW_CART_REQUEST_CODE;

public class ProductListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView productListView;
    private ArrayList<ProductPOJO> list = new ArrayList<ProductPOJO>();
    private ProductListAdapter customProductAdapter;
    private SearchView searchView;
    private String cartId;
    private Bundle bundle;
    private ArrayList<CartPOJO> cartList = new ArrayList<CartPOJO>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VIEW_CART_REQUEST_CODE){
            if (data != null){
                customProductAdapter.setList((ArrayList<CartPOJO>) data.getSerializableExtra(CART_POJO_SERIAL_KEY));
                updateArgumentsForCartList();
            }
        }else super.onActivityResult(requestCode, resultCode, data);
    }

    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_product_list, container, false);
        setListeners(view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setIconified(false);
                searchView.setQueryHint("Search Item here...");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        customProductAdapter.getFilter().filter(s);
                        customProductAdapter.notifyDataSetChanged();
                        return false;
                    }
                });
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem pinMenuItem = menu.findItem(R.id.action_view);

        if(pinMenuItem!=null){
            pinMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    cartList = new ArrayList<CartPOJO>(customProductAdapter.getCartList());
                    return false;
                }
            });
        }
    }

    private void setListeners(View view){
        productListView = (ListView) view.findViewById(R.id.productList);

        ProductPOJO productPOJO = null;
        ProductPOJOBuilder pBuilder = null;

        Cursor products = getAllData();
        while (products.moveToNext()) {
            pBuilder = new ProductPOJOBuilder();
            productPOJO = pBuilder
                    .productId(products.getString(products.getColumnIndex(PRODUCT_ID)))
                    .productName(products.getString(products.getColumnIndex(PRODUCT_NAME)))
                    .price(Double.valueOf(products.getString(products.getColumnIndex(PRICE))))
                    .build();
            list.add(productPOJO);
            Log.i(null,productPOJO.toString());
        }
        customProductAdapter = new ProductListAdapter(getActivity(),R.layout.product_custom_array_adapter, list);
        customProductAdapter.setActivity(getActivity());
        productListView.setAdapter(customProductAdapter);

        getCartList();
        updateArgumentsForCartList();
    }

    private void getCartList(){
        try{
            cartId =CachePot.getInstance().pop(String.class);
            customProductAdapter.setList((ArrayList<CartPOJO>) CachePot.getInstance().pop(ArrayList.class));
            updateArgumentsForCartList();
        }catch (NullPointerException e){
            cartId= "";
            customProductAdapter.setList(new ArrayList<CartPOJO>());
            Log.i(null,e.getMessage());
        }catch (Exception e){
            customProductAdapter.setList(new ArrayList<CartPOJO>());
            Log.i(null,e.getMessage());
        }
    }

    private void updateArgumentsForCartList(){
        try{
            CachePot.getInstance().clear();
            CachePot.getInstance().push(cartId);
            CachePot.getInstance().push(customProductAdapter.getCartList());
        }catch (NullPointerException e){
            Log.i(null,e.getMessage());
        }catch (Exception e){
            Log.i(null,e.getMessage());
        }
    }

    private Cursor getAllData(){
        DBHelper db = new DBHelper(getActivity());
        return db.getAllProductsWithPrice();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
