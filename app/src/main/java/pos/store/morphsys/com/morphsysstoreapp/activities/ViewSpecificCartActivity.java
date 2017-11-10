package pos.store.morphsys.com.morphsysstoreapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.adapters.ItemListAdapter;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.CART_POJO_SERIAL_KEY;
import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.VIEW_CART_REQUEST_CODE;


public class ViewSpecificCartActivity extends AppCompatActivity {

    private ItemListAdapter itemListAdapter;
    private ListView allItemsView;
    private ArrayList<CartPOJO> cartItemList;
    private TextView txtCartId,txtStatus,txtDate;
    private String cartId,status,date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_view_popup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListeners();
        populateTexts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_cart, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }else{
            Bundle bundle = new Bundle();
            bundle.putSerializable(CART_POJO_SERIAL_KEY,cartItemList);

            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.putExtra("isFromUpdate",true);
            intent.putExtra("cartId",cartId);
            setResult(CommonStatusCodes.SUCCESS,intent);

            finish();
            return true;
        }
    }

    private void setListeners(){
        allItemsView = (ListView)findViewById(R.id.allItemsView);

        txtCartId= (TextView) findViewById(R.id.txtCartNo);
        txtStatus= (TextView) findViewById(R.id.txtStatus);
        txtDate= (TextView) findViewById(R.id.txtDate);
    }



    private void populateList(){
        cartItemList = (ArrayList<CartPOJO>)getIntent().getSerializableExtra(CART_POJO_SERIAL_KEY);

        itemListAdapter = new ItemListAdapter(getApplicationContext(),R.layout.cart_view_popup, cartItemList);
        allItemsView.setAdapter(itemListAdapter);
    }

    private void populateTexts(){
        populateList();

        cartId = getIntent().getStringExtra("cartId");
        status = getIntent().getStringExtra("status");
        date = getIntent().getStringExtra("date");

        txtCartId.setText("CART ID #"+cartId);
        txtStatus.setText("STATUS : "+status);
        txtDate.setText("DATE CREATED : "+date);
    }
}
