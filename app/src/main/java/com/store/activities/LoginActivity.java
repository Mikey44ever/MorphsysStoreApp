package com.store.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

import javax.net.ssl.HttpsURLConnection;

import com.store.R;
import com.store.dbs.DBHelper;

import static com.store.constants.Constants.*;
import static com.store.constants.Strings.*;

public class LoginActivity extends AppCompatActivity {

    private DBHelper mydb;
    Button btnSignUp, btnSignIn, btnExit;
    TextView txtUsername, txtPassword;
    Intent mainIntent;

    private String userName,password,userId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTRATION_REQUEST_CODE){
            if(resultCode == CommonStatusCodes.SUCCESS){
                //TODO put something here
            }
        } else if(requestCode == PRODUCT_RETRIEVAL_REQUEST_CODE){
            String jsonString=data.getStringExtra("products") != null ? data.getStringExtra("products") : "";
            initializeDB(true,jsonString);//true for testing only
        } else if(requestCode == MAIN_DRAWER_ACTIVITY_REQUEST_CODE){
            Intent intent= new Intent(LoginActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(requestCode == DB_CREATE_REQUEST_CODE){

        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
        mainIntent= intent;
        startActivityForResult(mainIntent,MAIN_DRAWER_ACTIVITY_REQUEST_CODE);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnExit = (Button) findViewById(R.id.btnExit);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);

        txtUsername.setText("Uu");
        txtPassword.setText("uu");

        if(haveNetworkConnection()) {
            //callProductRetrievalAct();
        }else{
            showConstantDialog(LoginActivity.this,NOTICE.name(),NOTICE_MESSAGE,mainIntent,"",false);
        }

        mydb = new DBHelper(this);

        setListeners();
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
            if (ni.getTypeName().equalsIgnoreCase(WIFI.name()))
                if (ni.isConnected())
                    isWifiConnected = true;
            if (ni.getTypeName().equalsIgnoreCase(MOBILE.name()))
                if (ni.isConnected())
                    isMobileDataConnected = true;
        }
        return isWifiConnected || isMobileDataConnected;
    }

    private void setListeners(){
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLoginCheck(txtUsername.getText().toString(),txtPassword.getText().toString());
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REGISTRATION_REQUEST_CODE);
            }
        });
    }

    private void performLoginCheck(String username,String password){
        if(haveNetworkConnection()) {
            this.userName=txtUsername.getText().toString();
            this.password=txtPassword.getText().toString();
            new LoginAPI().execute();
        }else{
            showConstantDialog(LoginActivity.this,NOTICE.name(),NOTICE_MESSAGE,mainIntent,"",false);
        }
    }

    public void callAfterLoginAPI(String message){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(message);
        JsonObject userObj = tradeElement.getAsJsonObject().getAsJsonObject("user");

        String status = userObj.get("status").toString().replaceAll("\"","");
        String statusMsg = userObj.get("status_msg").toString().replaceAll("\"","");
        String fName = userObj.get("firstname").toString().replaceAll("\"","");
        String lName = userObj.get("lastname").toString().replaceAll("\"","");

        showConstantDialog(LoginActivity.this,"USER AUTHENTICATION",statusMsg,mainIntent,status,false);
        if(!status.equalsIgnoreCase("FAILED")){
            userId = userObj.get("user_id").toString().replaceAll("\"","");
            mainIntent.putExtra("fName",fName);
            mainIntent.putExtra("lName",lName);
            mainIntent.putExtra("userId",userId);
            startActivityForResult(mainIntent,MAIN_DRAWER_ACTIVITY_REQUEST_CODE);
        }

    }

    public class LoginAPI extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", userName);
                postDataParams.put("password", password);

                JSONObject userObj = new JSONObject();
                userObj.put("user",postDataParams);

                URL url = new URL(LOGIN_URL);

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
                writer.write(userObj.toString());

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
            callAfterLoginAPI(s);
        }
    }
}
