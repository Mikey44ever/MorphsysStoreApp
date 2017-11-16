package com.store.pos.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import com.store.pos.pojo.cart.CartPOJO;

import static com.store.pos.util.Constants.CART_POJO_SERIAL_KEY;
import static com.store.pos.util.Constants.CHECKOUT_URL;

public class CheckoutActivity extends AppCompatActivity{

    ArrayList<CartPOJO> cartList;
    String userId;
    String cartId;
    boolean isFromUpdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getStringExtra("userId")!= null ? getIntent().getStringExtra("userId") : "";
        cartId = getIntent().getStringExtra("cartId") != null ? getIntent().getStringExtra("cartId") : "";
        isFromUpdate = getIntent().getBooleanExtra("isFromUpdate",false);
        cartList = (ArrayList<CartPOJO>)getIntent().getSerializableExtra(CART_POJO_SERIAL_KEY);
        Log.i("",cartList.toString());
        new CheckOutAPICall().execute();
    }

    public void callAfterAPIExecution(String message){
        Intent intent = new Intent();
        intent.putExtra("message",message);
        intent.putExtra("isFromUpdate",isFromUpdate);
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }

    public class CheckOutAPICall extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONArray itemArr = new JSONArray();
                JSONObject itemJsonObj = null;
                Iterator<CartPOJO> ite = cartList.iterator();
                while(ite.hasNext()){
                    itemJsonObj= new JSONObject();
                    CartPOJO cart = ite.next();
                    itemJsonObj.put("itemId",cart.getItemId());
                    itemJsonObj.put("quantity",cart.getQuantity());
                    itemArr.put(itemJsonObj);
                }

                JSONObject cartParams = new JSONObject();
                cartParams.put("userId",userId);
                if(!cartId.equals(""))
                    cartParams.put("cartId",cartId);
                cartParams.put("items",itemArr);

                JSONObject checkoutParam = new JSONObject();
                checkoutParam.put("cart",cartParams);

                URL url = new URL(CHECKOUT_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(checkoutParam.toString());

                writer.flush();
                writer.close();
                os.close();

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

                } else {
                    return new String("false : "+responseCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
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
        protected void onPostExecute(String s) {
            callAfterAPIExecution(s);
        }
    }
}
