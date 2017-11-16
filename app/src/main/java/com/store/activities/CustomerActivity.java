package com.store.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.CustomerPOJO;
import com.store.pojos.OrderPOJO;
import com.store.pojos.builders.OrderPOJOBuilder;
import com.store.sales.adapter.OrderAdapter;

import java.util.ArrayList;

import static com.store.constants.Constants.*;

public class CustomerActivity extends AppCompatActivity {

    private ListView commonListView;
    private View commonView, commonTitleView;
    private TextView txtCustomer,txtAddress,txtCity,txtBrgy,txtMobile,txtLandmark;
    private CustomerPOJO customerPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtCustomer = (TextView) findViewById(R.id.txtCustomer);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtBrgy = (TextView) findViewById(R.id.txtCityTown);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtLandmark = (TextView) findViewById(R.id.txtLandmark);

        customerPOJO=(CustomerPOJO) getIntent().getSerializableExtra(CUSTOMER_POJO_SERIAL_KEY);

        txtCustomer.setText(customerPOJO.getFirstName()+" "+customerPOJO.getLastName());
        txtAddress.setText(customerPOJO.getAddress());
        txtCity.setText(customerPOJO.getCityTown());
        txtBrgy.setText(customerPOJO.getBrgy());
        txtMobile.setText(customerPOJO.getMobileNo());
        txtLandmark.setText(customerPOJO.getLandmark());

    }

    private void showCommonPopUp(int headerId, ArrayAdapter adapter, int popTitleId, int popUpBtn, View.OnClickListener listener){
        commonView = View.inflate(CustomerActivity.this, R.layout.common_list_view, null);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
        builder.setView(commonView);
        builder.setCustomTitle(title);
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_cart, menu);
        getMenuInflater().inflate(R.menu.view_orders, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }else if(id == R.id.add_cart_action){
            Bundle bundle = new Bundle();
            bundle.putSerializable(CUSTOMER_POJO_SERIAL_KEY,customerPOJO);
            Intent createOrderIntent = new Intent(getApplicationContext(),CreateOrderActivity.class);
            createOrderIntent.putExtras(bundle);
            startActivityForResult(createOrderIntent,CREATE_ORDER_REQUEST_CODE);
        }else if(id == R.id.view_orders_action){
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

            ArrayAdapter adapter = new OrderAdapter(CustomerActivity.this,R.layout.orders,orderPOJOs);
            showCommonPopUp(R.layout.orders_header, adapter, R.string.order_history, 0, null);
        }
        return super.onOptionsItemSelected(item);
    }
}
