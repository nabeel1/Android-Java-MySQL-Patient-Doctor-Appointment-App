package com.example.shahidhussain.assignemnt2.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.shahidhussain.assignemnt2.Registrations.doctorRegistration;
import com.example.shahidhussain.assignemnt2.Registrations.patientregistration;
import com.example.shahidhussain.assignemnt2.classess.patientjavaClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class main extends AppCompatActivity {


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
    private String doctorid="";
    private boolean found=false;
    private  String path = new dbPath("patientCRUD").getPath();
    private  String doctorpath = new dbPath("doctorCRUD").getPath();


    private ArrayList<patientjavaClass> patientlist=new ArrayList<>();
    public void startAnimation(View view){
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        view.startAnimation(animation1);
    }
    public void stopAnimation(View view){
        view.clearAnimation();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView next = findViewById(R.id.registration);
        final Button btn = findViewById(R.id.next);
        final Button doctorbtn = findViewById(R.id.doctorbtn);
        final Button patientbtn = findViewById(R.id.patientbtn);
        final ImageView doctorimg = findViewById(R.id.doctorstatus);
        final ImageView patientimg = findViewById(R.id.patientstatus);
        final TextView message = findViewById(R.id.message);
        final View contentmain = findViewById(R.id.maincontent);

        final TextView PALogin=findViewById(R.id.PALogin);
        PALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), com.example.shahidhussain.assignemnt2.Login.PALogin.class);
                startActivity(i);
            }
        });
        PALogin.setVisibility(View.GONE);
        TextView or=findViewById(R.id.or);
        or.setVisibility(View.GONE);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        oldemail=pref.getString("email", "");
        oldpassword=pref.getString("password", "");
        type=pref.getString("type", "");

        if(!oldemail.equals("")&&!oldpassword.equals("")&&!type.equals("")){
            if(type.equals("patient")){
                    Intent nxt = new Intent(getApplicationContext(), patientDashboard.class);
                    startActivity(nxt);
            }else if(type.equals("doctor")){
                Intent nxt = new Intent(getApplicationContext(), DoctorDashBoard.class);
                startActivity(nxt);
            }else if(type.equals("PA")){
                Intent nxt = new Intent(getApplicationContext(), padashboard.class);
                startActivity(nxt);
            }
        }else {
            doctorimg.setVisibility(View.GONE);
            patientimg.setVisibility(View.GONE);
            contentmain.setVisibility(View.GONE);
            doctorbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    patientimg.setVisibility(View.GONE);
                    doctorimg.setVisibility(View.VISIBLE);
                    contentmain.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    stopAnimation(patientimg);
                    startAnimation(doctorimg);
                    type = "doctor";
                    TextView PALogin=findViewById(R.id.PALogin);
                    PALogin.setVisibility(View.VISIBLE);
                    TextView or=findViewById(R.id.or);
                    or.setVisibility(View.VISIBLE);

                }
            });
            patientbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    patientimg.setVisibility(View.VISIBLE);
                    doctorimg.setVisibility(View.GONE);
                    contentmain.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    stopAnimation(doctorimg);
                    startAnimation(patientimg);
                    type = "patient";
                    TextView PALogin=findViewById(R.id.PALogin);
                    PALogin.setVisibility(View.GONE);
                    TextView or=findViewById(R.id.or);
                    or.setVisibility(View.GONE);
                }
            });
            TextView forget = findViewById(R.id.forget);
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
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type.equals("patient")) {
                        Intent nxt = new Intent(getApplicationContext(), patientregistration.class);
                        startActivity(nxt);
                    } else if (type.equals("doctor")) {
                        SharedPreferences settings = getSharedPreferences("registrationPref", 0);
                        settings.edit().clear().commit();
                        Intent nxt = new Intent(getApplicationContext(), doctorRegistration.class);
                        startActivity(nxt);
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
                    editor.putString("type", type);
                    editor.commit();
                    startActivity(nxt);
                }
            });
        }
    }
    class loginVerification extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(main.this);
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
            param.add(new BasicNameValuePair("loginType", type));
            param.add(new BasicNameValuePair("type", "loginVerfiction"));
            JSONObject json = jsonParser.makeHttpRequest(path, "GET", param);

            if(json!=null) {
                try {
                    int success = json.getInt("flag");
                    if (success == 1) {
                        found=true;
                        message=json.getString("message");
                        if(type.equals("doctor") ||type.equals("patient") ) {
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("email", email));
                            params.add(new BasicNameValuePair("type", "findstatus"));
                            JSONObject jsons = jsonParser.makeHttpRequest(doctorpath, "GET", params);
                            try {
                                status = jsons.getString("status");
                                doctorid= jsons.getString("doctorid");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
            if(type.equals("patient")){
                pDialog.dismiss();
                if(found) {
                    Intent nxt = new Intent(getApplicationContext(), patientDashboard.class);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    String newemail=email;
                    String newpassword=password;
                    editor.putString("email", newemail);
                    editor.putString("password", newpassword);
                    editor.putString("type", type);
                    editor.commit();
                    startActivity(nxt);
                }
            }else if(type.equals("doctor")){
                pDialog.dismiss();
                    if(found) {
                        if(status.equals("complete")){
                            Intent nxt = new Intent(getApplicationContext(), DoctorDashBoard.class);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            String newemail=email;
                            String newpassword=password;
                            editor.putString("email", newemail);
                            editor.putString("password", newpassword);
                            editor.putString("id", doctorid);
                            editor.putString("type", type);
                            editor.commit();
                            startActivity(nxt);
                        }
                        else{
                            Intent nxt = new Intent(getApplicationContext(), doctorStatus.class);
                            nxt.putExtra("status",status);
                            nxt.putExtra("email",email);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
                            editor.putString("id", doctorid);
                            editor.commit();
                            startActivity(nxt);
                        }
                    }
            }
        }
    }
}
