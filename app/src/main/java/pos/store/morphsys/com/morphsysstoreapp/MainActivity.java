package pos.store.morphsys.com.morphsysstoreapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import pos.store.morphsys.com.morphsysstoreapp.barcode.capture.BarcodeCaptureActivity;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    SQLiteDatabase myDB;
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
        } else super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB= this.openOrCreateDatabase("SampleDatabase",MODE_PRIVATE,null);

        myDB.execSQL("DROP TABLE SampleTable");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS SampleTable('BARCODE' VARCHAR PRIMARY KEY, 'NAME' VARCHAR)");
        myDB.execSQL("INSERT INTO SampleTable('BARCODE', 'NAME') VALUES('8935001721925','Mentos Chewing Gum'");
        mResultTextView = (TextView) findViewById(R.id.result_textview);
        txtRes = (TextView) findViewById(R.id.txtRes);

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
                try {
                    String qu = "Select 'NAME' from SampleTable where BARCODE='" + mResultTextView + "'";
                    Cursor c = myDB.rawQuery(qu, null);
                    c.moveToFirst();
                    txtRes.setText(c.getString(0));
                }
                catch (Exception e){
                    txtRes.setText(e.toString());
                }
            }
        });


    }
}
