package pos.store.morphsys.com.morphsysstoreapp.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;


import java.util.ArrayList;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.adapters.ProductListAdapter;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBHelper;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJOBuilder;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.PRICE;
import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.PRODUCT_ID;
import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.PRODUCT_NAME;

public class ProductListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView productListView;
    ArrayList<ProductPOJO> list = new ArrayList<ProductPOJO>();
    ProductListAdapter customProductAdapter;
    ImageButton imgBtnBack;
    SearchView searchView;
    SearchManager searchManager;
    Toolbar toolbar;

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
                try{
                    searchView = (SearchView) MenuItemCompat.getActionView(item);
                    searchView.setIconified(false);
                    searchView.setQueryHint("Search Item here...");
                }catch (Exception e){
                    Log.i(e.toString(),null);
                }

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

    private void setListeners(View view){
        final Bundle bundleFromActivity = this.getArguments();
        Log.i(null,this.toString());
        productListView = (ListView) view.findViewById(R.id.productList);
        imgBtnBack = (ImageButton) view.findViewById(R.id.imgBtnBack);

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
