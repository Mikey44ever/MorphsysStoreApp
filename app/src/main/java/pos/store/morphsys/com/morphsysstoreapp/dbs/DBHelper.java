package pos.store.morphsys.com.morphsysstoreapp.dbs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class DBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "MORPHSYS.db";
    public static final String PRODUCTS_TABLE_NAME = "PRODUCTS";
    public static final String PRODUCTS_PRICE_TABLE_NAME = "PRODUCTS_PRICE";
    private HashMap hp;
    String timeStamp = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss a").format(new Date());

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCTS('PRODUCT_ID' text PRIMARY KEY, 'PRODUCT_NAME' text, 'PRODUCT_DESCRIPTION' text,'BARCODE' text" +
                ",'CREATION_DATE' text, 'EFFECTIVE_DATE' text,'EXPIRATION_DATE' text)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCTS_PRICE('PRODUCT_PRICE_ID' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'PRODUCT_ID' text, 'PRODUCT_PRICE' NUMERIC"+
                ",'CREATION_DATE' text, 'EFFECTIVE_DATE' text,'EXPIRATION_DATE' text)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCTS_BARCODE('EAN_ID' NUMERIC PRIMARY KEY,'PRODUCT_ID' text, 'PRODUCT_BARCODE' text"+
                ",'CREATION_DATE' text, 'EFFECTIVE_DATE' text,'EXPIRATION_DATE' text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS_PRICE");
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS_BARCODE");
        onCreate(db);
    }

    public void doDBRefresh(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS");
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS_PRICE");
        db.execSQL("DROP TABLE IF EXISTS PRODUCTS_BARCODE");
        onCreate(db);
    }

    public boolean insertProduct(JsonElement details) throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();
        JsonObject obj = details.getAsJsonObject();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PRODUCT_ID", (obj.get("item_id").toString()).replaceAll("^\"|\"$", ""));
        contentValues.put("PRODUCT_NAME", (obj.get("description").toString()).replaceAll("^\"|\"$", ""));
        contentValues.put("PRODUCT_DESCRIPTION", (obj.get("description").toString()).replaceAll("^\"|\"$", ""));
        contentValues.put("BARCODE", (obj.get("barcode").toString()).replaceAll("^\"|\"$", ""));
        contentValues.put("CREATION_DATE", timeStamp);
        contentValues.put("EFFECTIVE_DATE", timeStamp);
        contentValues.put("EXPIRATION_DATE", "");
        db.insert(PRODUCTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertProductPrice(JsonElement details) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        JsonObject obj = details.getAsJsonObject();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PRODUCT_ID", (obj.get("item_id").toString()).replaceAll("^\"|\"$", ""));
        contentValues.put("PRODUCT_PRICE", (obj.get("cost").toString()).replaceAll("^\"|\"$", ""));
        contentValues.put("CREATION_DATE", timeStamp);
        contentValues.put("EFFECTIVE_DATE", timeStamp);
        contentValues.put("EXPIRATION_DATE", "");
        db.insert(PRODUCTS_PRICE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getAllData(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+table, null);
        return res;
    }

    public Cursor getSpecificProduct(String table,String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+table+" where BARCODE = ?", new String[]{barcode});
        return res;
    }
}
