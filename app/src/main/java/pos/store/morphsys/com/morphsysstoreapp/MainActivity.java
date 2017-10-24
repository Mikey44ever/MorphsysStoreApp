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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import pos.store.morphsys.com.morphsysstoreapp.barcode.capture.BarcodeCaptureActivity;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBCreateActivity;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBHelper;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private static final int DB_CREATE_REQUEST_CODE = 2;

    private DBHelper mydb;
    private JSONArray productJSONArray = new JSONArray();
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
        }  else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = (TextView) findViewById(R.id.result_textview);
        txtRes = (TextView) findViewById(R.id.txtRes);

        //initialize resources here
        if(haveNetworkConnection())//check first if device is connected to the internet
            new SendPostRequest().execute();
        else
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
                    Log.i(null,targetValue);
                }
            }
        });
    }

    private void initializeDB(boolean doRefresh){
        if(productJSONArray.length()>0){
            Intent dbIntent = new Intent(getApplicationContext(), DBCreateActivity.class);
            dbIntent.putExtra("refreshDB",doRefresh);
            dbIntent.putExtra("products",productJSONArray.toString());
            startActivityForResult(dbIntent,DB_CREATE_REQUEST_CODE);
        }
    }

    public void parseProductJSON(String result){
        try{
            productJSONArray = (new JSONObject(result)).getJSONArray("products");
            Log.i(null,"Here is the json "+productJSONArray.toString());
            initializeDB(true);//true for testing only
        }catch(Exception e){
            Log.e("error here", e.getStackTrace().toString());
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

    /*******************************************
    *inner class for API call
    *******************/
    public class SendPostRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://morphsys.com.ph/store/getitems.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            } catch (MalformedURLException e) {
                return new String("Exception: " + e.getMessage());
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            parseProductJSON(result);
        }
    }

}
