package com.example.shahidhussain.assignemnt2.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.shahidhussain.assignemnt2.ForgetPassword.forgetPassword;
import com.example.shahidhussain.assignemnt2.PA.padashboard;
import com.example.shahidhussain.assignemnt2.Patient.patientDashboard;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.Registrations.PARegistration;
import com.example.shahidhussain.assignemnt2.Registrations.doctorRegistration;
import com.example.shahidhussain.assignemnt2.Registrations.patientregistration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PALogin extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String email="";
    private String password="";
    private String oldemail="";
    private String oldpassword="";
    private String message="";
    private String status="";
    private String type="";
    private boolean found=false;
    private  String path = new dbPath("PALogin").getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palogin);
        final Button btn = findViewById(R.id.next);
        TextView forget = findViewById(R.id.forget);
        final TextView next = findViewById(R.id.registration);
        final TextView title = findViewById(R.id.title);
        final ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PALogin.super.onBackPressed();
            }
        });
        title.setText("PA Login");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = ((EditText) findViewById(R.id.email)).getText().toString();
                password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (email.length() > 0 && password.length() > 0) {
                    new loginVerification().execute();
                }
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nxt = new Intent(getApplicationContext(), forgetPassword.class);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("type", "PA");
                editor.commit();
                startActivity(nxt);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nxt = new Intent(getApplicationContext(), PARegistration.class);
                startActivity(nxt);
            }
        });
    }

    class loginVerification extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PALogin.this);
            pDialog.setMessage("Verifying..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("email", email));
            param.add(new BasicNameValuePair("password", password));
            param.add(new BasicNameValuePair("type", "loginVerfiction"));
            JSONObject json = jsonParser.makeHttpRequest(path, "GET", param);

            if(json!=null) {
                try {
                    int success = json.getInt("flag");
                    if (success == 1) {
                        found=true;
                        message=json.getString("message");
                    }else {
                        message=json.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                message="No Internet Connection Found! Check Internet Connection...";
            }

            return null;
        }
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Intent nxt = new Intent(getApplicationContext(), padashboard.class);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putString("type", "PA");
            editor.commit();
            startActivity(nxt);
        }
    }
}
