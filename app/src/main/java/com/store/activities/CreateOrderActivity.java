package com.store.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.store.R;
import com.store.fragments.CustomerInfoFragment;
import com.store.fragments.ProductListFragment;
import com.store.pojos.CustomerPOJO;
import com.store.sales.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import com.store.pojos.ProductPOJO;
import com.store.pojos.builders.ProductPOJOBuilder;

import static com.store.constants.Constants.*;


public class CreateOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomerPOJO customerPOJO;
    private ArrayList<ProductPOJO> productPOJOs=new ArrayList<ProductPOJO>();
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customerPOJO=(CustomerPOJO) getIntent().getSerializableExtra(CUSTOMER_POJO_SERIAL_KEY);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        //test data productpojo
        ProductPOJO pPOJO = new ProductPOJOBuilder()
                .item("Super cali fragilistic espealidocious")
                .cv("450.34")
                .price("103455.90")
                .build();

        productPOJOs.add(pPOJO);

        bundle = new Bundle();
        bundle.putSerializable(CUSTOMER_POJO_SERIAL_KEY,customerPOJO);
        bundle.putSerializable(PRODUCT_POJO_SERIAL_KEY,productPOJOs);
        CustomerInfoFragment customer=new CustomerInfoFragment();
        customer.setArguments(bundle);

        ProductListFragment product=new ProductListFragment();
        product.setArguments(bundle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(customer, "Info.");
        adapter.addFragment(product, "Product List");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(null,"");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }else if (id == R.id.create_order_action) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
