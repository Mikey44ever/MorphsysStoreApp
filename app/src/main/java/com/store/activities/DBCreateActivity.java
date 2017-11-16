package com.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.store.dbs.DBHelper;


/**
 * Created by MorphsysLaptop on 23/10/2017.
 */

public class DBCreateActivity extends AppCompatActivity {

    private DBHelper mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            boolean doRefresh = getIntent().getBooleanExtra("refreshDB",false);
            //if(doRefresh)
            mydb = new DBHelper(this);
            mydb.doDBRefresh();//this line is for testing only, comment if done testing
            //mydb.onCreate(new DBHelper(this).getWritableDatabase());//to make sure DBs are created
            insertRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //finish activity here
            Intent intent = new Intent();
            setResult(CommonStatusCodes.SUCCESS, intent);
            finish();
        }
    }

    private void insertRecords(){
        String jsonString=getIntent().getStringExtra("products");
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(jsonString);
        JsonArray trade = tradeElement.getAsJsonArray();
        for(JsonElement obj:trade){
            try {
                mydb.insertProduct(obj);
                mydb.insertProductPrice(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(null,trade.toString());
    }

}
