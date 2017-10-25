package pos.store.morphsys.com.morphsysstoreapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.google.android.gms.vision.barcode.Barcode;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import pos.store.morphsys.com.morphsysstoreapp.api.ProductsRetrievalActivity;
import pos.store.morphsys.com.morphsysstoreapp.api.RegistrationActivity;
import pos.store.morphsys.com.morphsysstoreapp.barcode.capture.BarcodeCaptureActivity;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBCreateActivity;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBHelper;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DBHelper mydb;
    private TextView mResultTextView;
    TextView txtRes;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    mResultTextView.setText(barcode.displayValue);
                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        }  else if(requestCode == DB_CREATE_REQUEST_CODE){
            mydb = new DBHelper(this);
        }  else if(requestCode == REGISTRATION_REQUEST_CODE){
            //do something after registration
        }  else if(requestCode == PRODUCT_RETRIEVAL_REQUEST_CODE){
            String jsonString=data.getStringExtra("products") != null ? data.getStringExtra("products") : "";
            initializeDB(true,jsonString);//true for testing only
        }  else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.registration);

        mResultTextView = (TextView) findViewById(R.id.result_textview);
        txtRes = (TextView) findViewById(R.id.txtRes);

        //initialize resources here
        if(haveNetworkConnection()) {//check first if device is connected to the internet
            callProductRetrievalAct();
        }

        mydb = new DBHelper(this);

        Button scanBarcodeButton = (Button) findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        mResultTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String barcode = mResultTextView.getText().toString();
                Cursor rs=mydb.getSpecificProduct(mydb.PRODUCTS_TABLE_NAME,barcode);
                while (rs.moveToNext()) {
                    String targetValue=rs.getString(rs.getColumnIndex("PRODUCT_NAME"));
                }

                //tempo
                /*Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REGISTRATION_REQUEST_CODE);*/
            }
        });
    }

    private void callProductRetrievalAct(){
         Intent intent = new Intent(getApplicationContext(), ProductsRetrievalActivity.class);
        startActivityForResult(intent, PRODUCT_RETRIEVAL_REQUEST_CODE);
    }

    private void initializeDB(boolean doRefresh,String productJsonString){
        if(!productJsonString.equals("")){
            Intent dbIntent = new Intent(getApplicationContext(), DBCreateActivity.class);
            dbIntent.putExtra("refreshDB",doRefresh);
            dbIntent.putExtra("products",productJsonString);
            startActivityForResult(dbIntent,DB_CREATE_REQUEST_CODE);
        }
    }

    private boolean haveNetworkConnection() {
        boolean isWifiConnected = false;
        boolean isMobileDataConnected = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    isWifiConnected = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    isMobileDataConnected = true;
        }
        return isWifiConnected || isMobileDataConnected;
    }

}
