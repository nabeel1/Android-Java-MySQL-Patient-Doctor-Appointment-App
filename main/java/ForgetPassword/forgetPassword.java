package com.example.shahidhussain.assignemnt2.ForgetPassword;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.Doctor.DoctorDashBoard;
import com.example.shahidhussain.assignemnt2.Doctor.doctorStatus;
import com.example.shahidhussain.assignemnt2.Login.main;
import com.example.shahidhussain.assignemnt2.Patient.patientDashboard;
import com.example.shahidhussain.assignemnt2.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class forgetPassword extends AppCompatActivity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String message="";
    private String email="";
    private String password="";
    private String type="";
    private boolean found=false;
    private  String path = new dbPath("forgetPassword").getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        type=pref.getString("type", "");
        ImageView back = findViewById(R.id.back);
        final Intent move = new Intent(this, main.class);
        TextView setname = findViewById(R.id.title);
        setname.setText("Do you forget your password?");
        Button next = findViewById(R.id.next);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(move);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email1=findViewById(R.id.email);
                email=email1.getText().toString();
            new sendingEmail().execute();
            }
        });
    }

    class sendingEmail extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(forgetPassword.this);
            pDialog.setMessage("Sending Password..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            if (type.equals("doctor") || type.equals("patient") || type.equals("PA") ) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));

                if (type.equals("doctor"))
                    params.add(new BasicNameValuePair("table", "doctor"));
                else if(type.equals("patient"))
                    params.add(new BasicNameValuePair("table", "patient"));
                else if(type.equals("PA"))
                    params.add(new BasicNameValuePair("table", "doctorPA"));

                params.add(new BasicNameValuePair("type", "getPassword"));

                JSONObject json = jsonParser.makeHttpRequest(path, "GET", params);
                if (json != null) {
                    try {
                        int success = json.getInt("flag");
                        if (success == 1) {
                            found=true;
                            password=json.getString("password");
                        } else {
                            message = json.getString("message");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    message = "No Internet Connection Found! Check Internet Connection...";
                }
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(!message.equals("") && !message.equals(null))
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(getApplicationContext(), "Email Send SuccessFully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),main.class);
                startActivity(intent);
            }
        }
    }
}
