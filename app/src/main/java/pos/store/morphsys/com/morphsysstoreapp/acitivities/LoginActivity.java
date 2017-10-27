package pos.store.morphsys.com.morphsysstoreapp.acitivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.JsonElement;

import pos.store.morphsys.com.morphsysstoreapp.R;
import pos.store.morphsys.com.morphsysstoreapp.dbs.DBHelper;

import static pos.store.morphsys.com.morphsysstoreapp.constants.Constants.*;

/**
 * Created by MorphsysLaptop on 25/10/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private DBHelper mydb;
    Button btnSignUp, btnSignIn, btnExit;
    TextView txtUsername, txtPassword, txtMessage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTRATION_REQUEST_CODE){
            if(resultCode == CommonStatusCodes.SUCCESS){
                String userId = data.getStringExtra("userId").toString();
                String userName = data.getStringExtra("userName").toString();
                String password = data.getStringExtra("password").toString();
                saveUserToLocal(userName,userId,password);
            }
        } else if(requestCode == PRODUCT_RETRIEVAL_REQUEST_CODE){
            String jsonString=data.getStringExtra("products") != null ? data.getStringExtra("products") : "";
            initializeDB(true,jsonString);//true for testing only
        } else if(requestCode == MAIN_ACTIVITY_REQUEST_CODE){
            Log.i("","success");
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnExit = (Button) findViewById(R.id.btnExit);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        //initialize resources here
        if(haveNetworkConnection()) {//check first if device is connected to the internet
            callProductRetrievalAct();
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

    private void callProductRetrievalAct(){
        Intent intent = new Intent(getApplicationContext(), ProductsRetrievalActivity.class);
        startActivityForResult(intent, PRODUCT_RETRIEVAL_REQUEST_CODE);
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
                String message=performLoginCheck(txtUsername.getText().toString(),txtPassword.getText().toString());
                if(message.equalsIgnoreCase(""))
                    txtMessage.setText("Username or password is incorrect!");
                else{
                    txtMessage.setText("");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent,MAIN_ACTIVITY_REQUEST_CODE);
                }

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

    private String performLoginCheck(String username,String password){
        Cursor echos =mydb.getAllData(USERS_TABLE);
        int count = echos.getCount();

        String message="";
        Cursor c=mydb.getGenericData(SELECT_SUFFIX+USERS_TABLE+WHERE+" USER_NAME="+"'"+username+"'"+" AND PASSWORD="+"'"+password+"'");
        while (c.moveToNext()) {
            message=c.getString(c.getColumnIndex("USER_ID"));
        }
        return message;
    }

    private void saveUserToLocal(String username,String userId,String password){
        mydb.insertUser(username,userId,password);
    }
}
