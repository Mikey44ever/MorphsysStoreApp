package com.store.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.CODShippingRatesPOJO;
import com.store.pojos.JRSLBCShippingRatesPOJO;
import com.store.pojos.OrderPOJO;
import com.store.pojos.builders.CODShippingRatesPOJOBuilder;
import com.store.pojos.builders.JRSLBCShippingRatePOJOBuilder;
import com.store.pojos.builders.OrderPOJOBuilder;
import com.store.sales.adapter.CODAreasAdapter;
import com.store.sales.adapter.CODShippingRatesAdapter;
import com.store.sales.adapter.GridAdapter;
import com.store.sales.adapter.JRSLBCShippingRatesAdapter;
import com.store.sales.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.store.constants.Constants.*;


public class MainScreenActivity extends AppCompatActivity {

    private GridView gridview;
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    private ListView commonListView;
    private View commonView, commonTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundResource(R.drawable.business);
        setSupportActionBar(toolbar);

        ArrayList mThumbs= new ArrayList(Arrays.asList(getResources().getStringArray(R.array.main_grid_array)));

        gridview = (GridView) findViewById(R.id.gridView);
        GridAdapter gridAdapter = new GridAdapter(MainScreenActivity.this,R.layout.row_grid, mThumbs);
        gridAdapter.setActivity(MainScreenActivity.this);
        gridview.setAdapter(gridAdapter);

        setUpGridViewView();
    }

    private void showJRSLBCShippingRates(){
        ArrayList<JRSLBCShippingRatesPOJO> ljRatesPOJOs = new ArrayList<JRSLBCShippingRatesPOJO>();
        JRSLBCShippingRatesPOJO ljPOJO = new JRSLBCShippingRatePOJOBuilder()
                .qty("5 bxs & below")
                .lbcRate("185.00")
                .jrsRate("155.00")
                .build();

        ljRatesPOJOs.add(ljPOJO);

        ljPOJO = new JRSLBCShippingRatePOJOBuilder()
                .qty("14 bxs & below")
                .lbcRate("200.00")
                .jrsRate("170.00")
                .build();

        ljRatesPOJOs.add(ljPOJO);

        ArrayAdapter adapter = new JRSLBCShippingRatesAdapter(MainScreenActivity.this,R.layout.lj_shipping_rates_header,ljRatesPOJOs);
        showCommonPopUp(R.layout.lj_shipping_rates_header, adapter, R.string.lbc_jrc_shipping_rate,0,null);
    }

    private void showCODShippingRates(){
        ArrayList<CODShippingRatesPOJO> srPOJOs = new ArrayList<CODShippingRatesPOJO>();
        CODShippingRatesPOJO csrPOJO = new CODShippingRatesPOJOBuilder()
                .qty("10 bxs & below")
                .rates("170.00")
                .build();

        srPOJOs.add(csrPOJO);

        csrPOJO = new CODShippingRatesPOJOBuilder()
                .qty("20 bxs & below")
                .rates("210.00")
                .build();

        srPOJOs.add(csrPOJO);
        ArrayAdapter adapter = new CODShippingRatesAdapter(MainScreenActivity.this,R.layout.cod_shipping_rates,srPOJOs);
        showCommonPopUp(R.layout.cod_shipping_rates_header, adapter, R.string.cod_shipping_rate,0,null);
    }

    private void showOrderHistory(){
        ArrayList<OrderPOJO> orderPOJOs = new ArrayList<OrderPOJO>();
        OrderPOJO orderPOJO = new OrderPOJOBuilder()
                .poId("100001")
                .date("2017-11-15")
                .name("Geroge E. W. Bush")
                .qty("1")
                .build();

        orderPOJOs.add(orderPOJO);

        orderPOJO = new OrderPOJOBuilder()
                .poId("100002")
                .date("2017-11-15")
                .name("The quick brown fox jumps over the lazy dog")
                .qty("44")
                .build();

        orderPOJOs.add(orderPOJO);

        ArrayAdapter adapter = new OrderAdapter(MainScreenActivity.this,R.layout.orders,orderPOJOs);
        showCommonPopUp(R.layout.orders_header, adapter, R.string.order_history, 0, null);
    }

    private void showCODAreas(){
        ArrayList codAreas = new ArrayList();
        codAreas.add("doon");
        codAreas.add("diyan");
        codAreas.add("dito");
        codAreas.add("dine");
        codAreas.add("doon");
        codAreas.add("diyan");
        codAreas.add("dito");
        codAreas.add("dine");
        codAreas.add("doon");
        codAreas.add("diyan");
        codAreas.add("dito");
        codAreas.add("dine");

        ArrayAdapter adapter = new CODAreasAdapter(MainScreenActivity.this,R.layout.cod_area, codAreas);
        showCommonPopUp(R.layout.cod_areas_header, adapter, R.string.cod_areas,0,null);
    }

    private void showCommonPopUp(int headerId, ArrayAdapter adapter, int popTitleId, int popUpBtn, View.OnClickListener listener){
        commonView = View.inflate(MainScreenActivity.this, R.layout.common_list_view, null);
        commonListView = (ListView) commonView.findViewById(R.id.commonListView);

        commonListView.setAdapter(adapter);
        View headerView = (View) getLayoutInflater().inflate(headerId,null);
        commonListView.addHeaderView(headerView);

        commonTitleView=(View) getLayoutInflater().inflate(R.layout.custom_title,null);
        ImageButton action = (ImageButton) commonTitleView.findViewById(R.id.imgActionBtn);
        TextView titleText = (TextView) commonTitleView.findViewById(R.id.txtTitle);

        if(popUpBtn==0)
            action.setImageResource(0);
        else{
            action.setImageResource(popUpBtn);
            action.setOnClickListener(listener);
        }
        titleText.setText(popTitleId);

        showPopUp(commonTitleView);
    }

    private void showPopUp(View title){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
        builder.setView(commonView);
        builder.setCustomTitle(title);
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    private void setUpGridViewView() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value =(String) parent.getItemAtPosition(position);
                switch (value.toLowerCase()) {
                    case "customers":
                        Intent customersIntent = new Intent(getApplicationContext(),CustomersActivity.class);
                        startActivityForResult(customersIntent,CUSTOMERS_LIST_REQUEST_CODE);
                        break;
                    case "cod areas":
                        showCODAreas();
                        break;
                    case "cod shipping rates":
                        showCODShippingRates();
                        break;
                    case "customer order":
                        navItemIndex = 3;
                        break;
                    case "orders":
                        showOrderHistory();
                        break;
                    case "lbc/jrs shipping rates":
                        showJRSLBCShippingRates();
                        navItemIndex = 5;
                        break;
                    default:
                        navItemIndex = 0;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
