package com.store.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.store.R;
import com.store.pojos.CustomerPOJO;
import com.store.pojos.builders.CustomerPOJOBuilder;
import com.store.sales.adapter.CustomerListAdapter;

import static com.store.constants.Constants.*;

import java.util.ArrayList;

public class CustomersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<CustomerPOJO> customerPOJOs = new ArrayList<CustomerPOJO>();
        final CustomerPOJO cPOJO = new CustomerPOJOBuilder()
                .fName("JonBon")
                .lName("Libreja")
                .mobile("0956900000000")
                .email("jon@bon.com")
                .fb("jon@fb.com")
                .address("jon street brgy bon")
                .cityTown("libreja city")
                .brgy("brgy bonchon")
                .landmark("bonland")
                .note("bon note")
                .build();

        customerPOJOs.add(cPOJO);
        ListView listView = (ListView) findViewById(R.id.commonListView);
        ArrayAdapter adapter = new CustomerListAdapter(CustomersActivity.this,R.layout.customer,customerPOJOs);
        listView.setAdapter(adapter);

        View header = (View) getLayoutInflater().inflate(R.layout.customer_header,null);
        listView.addHeaderView(header);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerPOJO cPOJO = (CustomerPOJO) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putSerializable(CUSTOMER_POJO_SERIAL_KEY,cPOJO);

                Intent customerIntent = new Intent(getApplicationContext(),CustomerActivity.class);
                customerIntent.putExtras(bundle);
                startActivityForResult(customerIntent,CUSTOMER_REQUEST_CODE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }else if(id == R.id.add_customer_action){
            //TODO add customer here
            Intent addCustomerIntent = new Intent(getApplicationContext(),AddCustomerActivity.class);
            startActivityForResult(addCustomerIntent,ADD_CUSTOMER_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }
}
