package pos.store.morphsys.com.morphsysstoreapp.acitivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;

/**
 * Created by MorphsysLaptop on 26/10/2017.
 */

public class ViewCartActivity extends AppCompatActivity{

    ListView cartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cart);

        cartView=(ListView) findViewById(R.id.cartListView);

        List<CartPOJO> cartList = (ArrayList<CartPOJO>)getIntent().getSerializableExtra(CART_POJO_SERIAL_KEY);
        CartPOJO[] carts = cartList.toArray(new CartPOJO[cartList.size()]);
        try{
            ArrayAdapter<CartPOJO> arrayAdapter = new ArrayAdapter<CartPOJO>(
                    this,
                    android.R.layout.simple_list_item_1,
                    carts);

            cartView.setAdapter(arrayAdapter);
        }catch(Exception e){
            Log.i("",e.getMessage());
        }
        //finish();
    }
}
