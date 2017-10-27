package pos.store.morphsys.com.morphsysstoreapp.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import pos.store.morphsys.com.morphsysstoreapp.R;

/**
 * Created by MorphsysLaptop on 25/10/2017.
 */

public class RegistrationMessageActivity extends AppCompatActivity {

    TextView txtMessage;
    Button btnOK;
    Intent intent = new Intent();
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_message);

        txtMessage = (TextView) findViewById(R.id.txtMessage);
        btnOK = (Button) findViewById(R.id.btnOK);

        String message = getIntent().getStringExtra("results");
        messageDisplay(message);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("userId",userId);
                finish();
            }
        });
    }

    private void messageDisplay(String message){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(message);
        JsonObject obj = tradeElement.getAsJsonObject();

        txtMessage.setText(obj.get("status_msg").toString());
        String status = (obj.get("status").toString()).replaceAll("\"","");
        if(status.equalsIgnoreCase("SUCCESS")){
            userId = (obj.get("user_id").toString()).replaceAll("\"","");
            setResult(CommonStatusCodes.SUCCESS, intent);
        }else
            setResult(CommonStatusCodes.ERROR, intent);
    }
}
