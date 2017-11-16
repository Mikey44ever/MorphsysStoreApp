package com.store.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.store.R;
import pos.store.morphsys.com.morphsysstoreapp.pojo.registration.RegistrationPOJO;
import pos.store.morphsys.com.morphsysstoreapp.pojo.registration.RegistrationPOJOBuilder;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.store.constants.Constants.*;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RegistrationActivity extends AppCompatActivity {

    Button registerBtn;
    TextView txtFirstname;
    TextView txtLastname;
    TextView txtUsername;
    TextView txtMiddlename;
    TextView txtPassword;
    TextView txtEmail;
    TextView txtMobileNumber;
    TextView txtBirthdate;
    Spinner branchSpinner;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    RegistrationPOJOBuilder registrationPOJOBuilder;
    RegistrationPOJO rPOJO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        setListeners();
        instantiateObjects();
        setSpinner();
    }

    private void setListeners(){
        registerBtn = (Button) findViewById(R.id.btnSubmit);
        txtFirstname = (TextView) findViewById(R.id.txtCustomerheader);
        txtLastname = (TextView) findViewById(R.id.txtLname);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtMiddlename = (TextView) findViewById(R.id.txtMname);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtMobileNumber = (TextView) findViewById(R.id.txtMobileNumber);
        txtBirthdate = (TextView) findViewById(R.id.txtBirthdate);
        branchSpinner = (Spinner) findViewById(R.id.branchSpinner);

        txtBirthdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate(txtBirthdate);
                //disable keyboard
                view.onTouchEvent(motionEvent);
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  // hide the soft keyboard
                }
                return true;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map=(HashMap)getBranches();
                //builder for registration details
                registrationPOJOBuilder = new RegistrationPOJOBuilder();
                rPOJO = registrationPOJOBuilder
                        .setFirstName(txtFirstname.getText().toString())
                        .setMiddleName(txtMiddlename.getText().toString())
                        .setLastName(txtLastname.getText().toString())
                        .setUsername(txtUsername.getText().toString())
                        .setPassword(txtPassword.getText().toString())
                        .setEmail(txtEmail.getText().toString())
                        .setMobileNumber(txtMobileNumber.getText().toString())
                        .setBirthDate(txtBirthdate.getText().toString())
                        .setBranch(map.get(branchSpinner.getSelectedItem().toString()).toString())
                        .build();

                new CallRegistrationAPI().execute();
            }
        });
    }

    private void setSpinner(){
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, SPINNERS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(adapter);
    }

    private void instantiateObjects(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void showDate(int year, int month, int day) {
        txtBirthdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,int arg1, int arg2, int arg3) {
                    showDate(arg1, (arg2+1), arg3);
                }
            };

    private void callAfterAPIExecution(String result){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(result);
        JsonObject obj = tradeElement.getAsJsonObject();

        String status = (obj.get("status").toString()).replaceAll("\"","");
        String statusMsg = (obj.get("status_msg").toString()).replaceAll("\"","");
        String userId = (obj.get("user_id").toString()).replaceAll("\"","");

        Intent intent = new Intent();
        intent.putExtra("userId",userId);
        intent.putExtra("userName",txtUsername.getText().toString());
        intent.putExtra("password",txtPassword.getText().toString());

        showConstantDialog(RegistrationActivity.this,"REGISTRATION",statusMsg,intent,status,true);
    }

    public class CallRegistrationAPI extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject userObj = new JSONObject();

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("firstname", rPOJO.getFirstName());
                postDataParams.put("middlename", rPOJO.getMiddleName());
                postDataParams.put("lastname", rPOJO.getLastName());
                postDataParams.put("username", rPOJO.getUserName());
                postDataParams.put("password", rPOJO.getPassword());
                postDataParams.put("email", rPOJO.getEmail());
                postDataParams.put("birthdate", rPOJO.getBirthDate());
                postDataParams.put("branch_id", rPOJO.getBranch());
                postDataParams.put("mobile", rPOJO.getMobileNumber());

                userObj.put("user",postDataParams);

                URL url = new URL(REGISTRATION_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                String input = userObj.toString();//getPostDataString(postDataParams);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(input);

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
