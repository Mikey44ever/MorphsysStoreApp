package pos.store.morphsys.com.morphsysstoreapp.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.google.android.gms.vision.barcode.Barcode;
import android.graphics.Point;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBHelper;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.cart.CartPOJOBuilder;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJOBuilder;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DBHelper mydb;
    private TextView txtProductName, txtProductPrice, txtQty, txtNoProduct;
    private Button scanBarcodeButton, btnAddToCart, btnClear, btnViewCart, btnExit, btnProductList,
    btnDummy;
    private Barcode barcode;
    private Intent data;
    private CartPOJOBuilder cBuilder;
    private ProductPOJOBuilder pBuilder;
    private ProductPOJO pPOJO;
    private CartPOJO cart;
    private ArrayList<CartPOJO> cartList = new ArrayList<CartPOJO>();
    private boolean isFromUpdate;
    private String cartId,userId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    setButtonEnabled("scan");
                    this.data=data;
                    getProductDetails();
                }
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else if(requestCode == VIEW_CART_REQUEST_CODE){
            if (data != null)
                cartList = (ArrayList<CartPOJO>)data.getSerializableExtra(CART_POJO_SERIAL_KEY);
            setButtonEnabled("view");
        } else if(requestCode == ALL_PRODUCTS_REQUEST_CODE){
            if (data != null)
                cartList.addAll((ArrayList<CartPOJO>)data.getSerializableExtra(CART_POJO_SERIAL_KEY));
            setButtonEnabled("list");
        } else super.onActivityResult(requestCode, resultCode, data);
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
            bundle.putString("userId",userId);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mydb = new DBHelper(this);
        setListeners();

        isFromUpdate = getIntent().getBooleanExtra("isFromUpdate",false);
        if(isFromUpdate)
            cartList =(ArrayList<CartPOJO>) getIntent().getSerializableExtra(CART_POJO_SERIAL_KEY);

        userId = getIntent().getStringExtra("userId");
        cartId = getIntent().getStringExtra("cartId") != null ? getIntent().getStringExtra("cartId") : "";
    }

    private void setListeners(){
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtQty = (TextView) findViewById(R.id.txtQty);
        txtNoProduct = (TextView) findViewById(R.id.txtNoProduct);
        scanBarcodeButton = (Button) findViewById(R.id.scan_barcode_button);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        btnViewCart = (Button) findViewById(R.id.btnViewCart);
        btnExit=(Button) findViewById(R.id.btnExit);
        btnProductList = (Button) findViewById(R.id.btnProductList);
        btnDummy = (Button) findViewById(R.id.btnDummy);

        btnViewCart.setEnabled(false);
        btnAddToCart.setEnabled(false);
        btnClear.setEnabled(false);

        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartList.size()>0){
                    txtNoProduct.setText("");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CART_POJO_SERIAL_KEY,cartList);

                    Intent cartIntent = new Intent(getApplicationContext(),ViewCartActivity.class);
                    cartIntent.putExtras(bundle);
                    cartIntent.putExtra("userId",getIntent().getStringExtra("userId"));
                    cartIntent.putExtra("cartId",getIntent().getStringExtra("cartId"));
                    cartIntent.putExtra("isFromUpdate",isFromUpdate);
                    if(!isFromUpdate){
                        startActivityForResult(cartIntent,VIEW_CART_REQUEST_CODE);
                    }else{
                        startActivityForResult(cartIntent,SCAN_FOR_UPDATE_REQUEST_CODE);
                    }
                }else{
                    txtNoProduct.setText("Cart is empty");
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNoProduct.setText("");
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra("userId",getIntent().getStringExtra("userId"));
                intent.putExtra("cartId",getIntent().getStringExtra("cartId"));
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(0);
                Log.i(null,cartList.toArray().toString());
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonEnabled("clear");
                clear();
            }
        });

    }
    private void setButtonEnabled(String activity){
        if("scan".equalsIgnoreCase(activity)){
            scanBarcodeButton.setEnabled(false);
            scanBarcodeButton.setAlpha(0.2f);
            btnAddToCart.setEnabled(true);
            btnClear.setEnabled(true);
        }else if("add".equalsIgnoreCase(activity)){
            scanBarcodeButton.setEnabled(true);
            scanBarcodeButton.setAlpha(1f);
            btnViewCart.setEnabled(true);
            btnAddToCart.setEnabled(false);
            btnClear.setEnabled(false);
        }else if("clear".equalsIgnoreCase(activity)){
            scanBarcodeButton.setEnabled(true);
            scanBarcodeButton.setAlpha(1f);
            btnAddToCart.setEnabled(false);
            btnClear.setEnabled(false);
        }else if("view".equalsIgnoreCase(activity)){
            txtQty.setText("");
            txtProductName.setText("");
            txtProductPrice.setText("");
            scanBarcodeButton.setEnabled(true);
            scanBarcodeButton.setAlpha(1f);
            btnAddToCart.setEnabled(false);
            btnClear.setEnabled(false);
        } else if("list".equalsIgnoreCase(activity)){
            txtQty.setText("");
            btnViewCart.setEnabled(true);
        }
    }

    private void clear(){
        cart = null;
        pPOJO=null;
        txtNoProduct.setText("");
        txtProductPrice.setText("");
        txtProductName.setText("");
        txtQty.setText("");
    }

    private void addToCart(int qty){
        try{
            if(pPOJO!=null){
                setButtonEnabled("add");
                if(qty==0)
                    qty=txtQty.getText().toString().equals("") ? 1 : Integer.parseInt(txtQty.getText().toString());

                cBuilder = new CartPOJOBuilder();
                cart = cBuilder
                        .item(pPOJO.getProductId())
                        .desc(pPOJO.getProductName())
                        .quantity(qty)
                        .basePrice(pPOJO.getProductPrice())
                        .build();
                cartList.add(cart);

                clear();
            }else{
                txtNoProduct.setText("Please scan product first....!");
            }
        }catch(NumberFormatException e){
            addToCart(1);
            Log.e(null,e.getMessage());
        }
    }

    private void getProductDetails(){
        barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
        Point[] p = barcode.cornerPoints;
        String strBarcode = barcode.displayValue;
        Cursor product=mydb.getSpecificProduct(mydb.PRODUCTS_TABLE_NAME,BARCODE,strBarcode);
        pBuilder = new ProductPOJOBuilder();
        while (product.moveToNext()) {
            String productId=product.getString(product.getColumnIndex(PRODUCT_ID));
            Cursor price=mydb.getSpecificProduct(mydb.PRODUCTS_PRICE_TABLE_NAME,PRODUCT_ID,productId);

            while(price.moveToNext()){
                pPOJO = pBuilder
                        .productId(productId)
                        .productName(product.getString(product.getColumnIndex(PRODUCT_NAME)))
                        .price(price.getDouble(price.getColumnIndex(PRICE)))
                        .build();
            }
        }
        txtProductName.setText(pPOJO.getProductName());
        txtProductPrice.setText(new Double(pPOJO.getProductPrice()).toString());
    }
}
