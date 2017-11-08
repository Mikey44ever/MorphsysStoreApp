package pos.store.morphsys.com.morphsysstoreapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.adapters.ItemListAdapter;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.CART_POJO_SERIAL_KEY;
import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.VIEW_CART_REQUEST_CODE;


public class ViewSpecificCartActivity extends AppCompatActivity {

    ItemListAdapter itemListAdapter;
    ListView allItemsView;
    ArrayList<CartPOJO> cartItemList;
    TextView txtCartId,txtStatus,txtDate;
    ImageView imgBtnBack;
    Button btnUpdate;
    private String cartId,status,date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_view_popup);

        setListeners();
        populateTexts();
    }

    private void setListeners(){
        allItemsView = (ListView)findViewById(R.id.allItemsView);

        txtCartId= (TextView) findViewById(R.id.txtCartNo);
        txtStatus= (TextView) findViewById(R.id.txtStatus);
        txtDate= (TextView) findViewById(R.id.txtDate);
        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(CART_POJO_SERIAL_KEY,cartItemList);

                Intent cartIntent = new Intent(getApplicationContext(),ViewCartActivity.class);
                cartIntent.putExtras(bundle);
                cartIntent.putExtra("userId",getIntent().getStringExtra("userId"));
                cartIntent.putExtra("cartId",cartId);
                cartIntent.putExtra("isFromUpdate",true);
                startActivityForResult(cartIntent,VIEW_CART_REQUEST_CODE);
            }
        });
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
