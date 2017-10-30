package pos.store.morphsys.com.morphsysstoreapp.acitivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;

/**
 * Created by MorphsysLaptop on 26/10/2017.
 */

public class ViewCartActivity extends AppCompatActivity{

    ListView cartView;
    Button btnBackToMain, btnCheckout;
    TextView txtTotal;
    ArrayList<CartPOJO> cartList;
    ArrayAdapter<CartPOJO> arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cart);
        cartList = (ArrayList<CartPOJO>)getIntent().getSerializableExtra(CART_POJO_SERIAL_KEY);

        setListeners();
        generateList();
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
                setResult(CommonStatusCodes.SUCCESS, intent);
                finish();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO checkout API
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
                        arrayAdapter.notifyDataSetInvalidated();
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
        CartPOJO[] carts = cartList.toArray(new CartPOJO[cartList.size()]);
        computeTotal(cartList);
        try{
            arrayAdapter = new ArrayAdapter<CartPOJO>(
                    this,
                    android.R.layout.simple_list_item_1,
                    carts);

            cartView.setAdapter(arrayAdapter);
        }catch(Exception e){
            Log.i("",e.getMessage());
        }
    }
}
