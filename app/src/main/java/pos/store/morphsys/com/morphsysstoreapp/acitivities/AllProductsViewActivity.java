package pos.store.morphsys.com.morphsysstoreapp.acitivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Filter;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.Map;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.adapters.ProductListAdapter;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBHelper;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJOBuilder;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;

public class AllProductsViewActivity extends AppCompatActivity {

    ListView productListView;
    ArrayList<ProductPOJO> list = new ArrayList<ProductPOJO>();
    ProductListAdapter customProductAdapter;
    Button btnBackToMain;
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list_view);

        setListeners();
    }

    private void setListeners(){
        productListView = (ListView) findViewById(R.id.productList);
        btnBackToMain = (Button) findViewById(R.id.btnBackToMain);
        searchView = (SearchView) findViewById(R.id.searchView);
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
        customProductAdapter = new ProductListAdapter(this,R.layout.product_custom_array_adapter, list);
        customProductAdapter.setActivity(AllProductsViewActivity.this);
        productListView.setAdapter(customProductAdapter);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(CART_POJO_SERIAL_KEY, customProductAdapter.getCartList());

                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(CommonStatusCodes.SUCCESS, intent);
                finish();
            }
        });

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
    }

    private Cursor getAllData(){
        DBHelper db = new DBHelper(this);
        return db.getAllProductsWithPrice();
    }
}
