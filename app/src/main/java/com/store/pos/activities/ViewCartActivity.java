package com.store.pos.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.store.pos.R;
import com.store.pos.adapters.CartAdapter;
import com.store.pos.pojo.cart.CartPOJO;

import static com.store.pos.util.Constants.*;

/**
 * Created by MorphsysLaptop on 26/10/2017.
 */

public class ViewCartActivity extends AppCompatActivity{

    private ListView cartView;
    private Button btnBackToMain, btnCheckout;
    private TextView txtTotal;
    private ArrayList<CartPOJO> cartList;
    private String cartId;
    private Handler mHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CHECKOUT_REQUEST_CODE){
            displayMessage(data.getStringExtra("message"));
        }else if(requestCode==MAIN_ACTIVITY_REQUEST_CODE){

        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CART_POJO_SERIAL_KEY,cartList);

            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.putExtra("cartId",cartId);
            setResult(CommonStatusCodes.SUCCESS, intent);

            onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CART_POJO_SERIAL_KEY,cartList);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtra("cartId",cartId);
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.view_cart_title);
        cartList = (ArrayList<CartPOJO>)getIntent().getSerializableExtra(CART_POJO_SERIAL_KEY);
        cartId = getIntent().getStringExtra("cartId") != null ? getIntent().getStringExtra("cartId"):"";
        mHandler = new Handler();

        setListeners();
        generateList();
    }
    private void displayMessage(String message){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(message);
        JsonObject userObj = tradeElement.getAsJsonObject().getAsJsonObject("user");

        String status = userObj.get("status").toString().replaceAll("\"","");
        String statusMsg = userObj.get("status_msg").toString().replaceAll("\"","");
        Intent intent = new Intent();
        showConstantDialog(ViewCartActivity.this,"CHECKOUT",statusMsg,intent,status,true);
    }

    private void setListeners(){
        cartView=(ListView) findViewById(R.id.cartListView);
        btnBackToMain = (Button) findViewById(R.id.btnBackToMain);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(CART_POJO_SERIAL_KEY,cartList);

                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.putExtra("cartId",cartId);
                setResult(CommonStatusCodes.SUCCESS, intent);
                finish();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CART_POJO_SERIAL_KEY,cartList);

            Intent checkoutIntent = new Intent(getApplicationContext(),CheckoutActivity.class);
            checkoutIntent.putExtras(bundle);
            checkoutIntent.putExtra("userId",getIntent().getStringExtra("userId"));
            checkoutIntent.putExtra("cartId",cartId);
            startActivityForResult(checkoutIntent,CHECKOUT_REQUEST_CODE);
            }
        });

        cartView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long l) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ViewCartActivity.this);
                adb.setTitle("ITEMS");
                final CartPOJO cPOJO = (CartPOJO) parent.getItemAtPosition(position);
                adb.setMessage(""+cPOJO);
                adb.setPositiveButton("OK", null);
                adb.setNegativeButton("REMOVE ITEM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartList.remove(cPOJO);
                        generateList();
                    }
                });
                    adb.show();
                }
        });
    }
    private void computeTotal(List<CartPOJO> cartList){
        double total = 0;
        Iterator<CartPOJO> ite = cartList.iterator();
        while(ite.hasNext()){
            CartPOJO cart = (CartPOJO) ite.next();
            total+=(cart.getBasePrice() * cart.getQuantity());
        }

        txtTotal.setText(String.valueOf(total));
    }
    private void generateList(){
        computeTotal(cartList);
        try{
            CartAdapter arrayAdapter = new CartAdapter(ViewCartActivity.this,android.R.layout.simple_list_item_1,cartList);

            cartView.setAdapter(arrayAdapter);
        }catch(Exception e){
            Log.i("",e.getMessage());
        }
    }
}
