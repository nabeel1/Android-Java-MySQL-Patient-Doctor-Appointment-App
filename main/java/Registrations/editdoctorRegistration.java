package com.example.shahidhussain.assignemnt2.Registrations;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.Doctor.doctorStatus;
import com.example.shahidhussain.assignemnt2.Login.main;
import com.example.shahidhussain.assignemnt2.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class editdoctorRegistration extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String name="";
    private String email="";
    private String password="";
    private String number="";
    private String city="";
    private String pmdc="";
    private String hospitalname="";
    private String message="";
    private String type="";
    private boolean res = false;
    private boolean result = false;
    private  String path = new dbPath("doctorCRUD").getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdoctor_registration);
        new getdoctorDetail().execute();
        final EditText name1=findViewById(R.id.name);
        final android.widget.EditText email1=findViewById(R.id.email);
        final EditText password1=findViewById(R.id.password);
        final EditText number1=findViewById(R.id.number);
        final EditText hospital1=findViewById(R.id.hospital);
        final EditText city1=findViewById(R.id.city);
        final EditText pmdc1=findViewById(R.id.pmdc);
        Button btn= findViewById(R.id.next);
        TextView setname=findViewById(R.id.title);
        setname.setText("Edit Information");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = name1.getText().toString();
                email = email1.getText().toString();
                password = password1.getText().toString();
                number = number1.getText().toString();
                city = city1.getText().toString();
                hospitalname = hospital1.getText().toString();
                pmdc = pmdc1.getText().toString();
                if(!password.equals("") && !password.equals("") && password.length()>0 ){
                    new Update().execute();
                }else{
                    Toast.makeText(editdoctorRegistration.this, "Password Cannot be Empty...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    class getdoctorDetail extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(editdoctorRegistration.this);
            pDialog.setMessage("Fetching Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Intent i=getIntent();
            params.add(new BasicNameValuePair("email", i.getStringExtra("email")));
            params.add(new BasicNameValuePair("type", "getdoctorData"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(path, "GET", params);
            try {
                name = json1.getString("name");
                email = json1.getString("email");
                city = json1.getString("city");
                number = json1.getString("num");
                pmdc = json1.getString("pmdcnumber");
                hospitalname = json1.getString("hospitalname");
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            final EditText name1=findViewById(R.id.name);
            final android.widget.EditText email1=findViewById(R.id.email);
            final EditText number1=findViewById(R.id.number);
            final EditText hospital1=findViewById(R.id.hospital);
            final EditText city1=findViewById(R.id.city);
            final EditText pmdc1=findViewById(R.id.pmdc);
            name1.setText(name);
            email1.setText(email);
            city1.setText(city);
            number1.setText(number);
            pmdc1.setText(pmdc);
            hospital1.setText(hospitalname);
        }
    }
    class Update extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(editdoctorRegistration.this);
            pDialog.setMessage("Updating..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("num", number));
            params.add(new BasicNameValuePair("city", city));
            params.add(new BasicNameValuePair("pmdcnumber", pmdc));
            params.add(new BasicNameValuePair("hospitalname", hospitalname));
            params.add(new BasicNameValuePair("status", "pending"));
            params.add(new BasicNameValuePair("type", "update"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(path, "GET", params);

            if(json1!=null) {
                try {
                    int success1 = json1.getInt("flag");
                    if (success1 == 1) {
                        result = true;
                        message = json1.getString("message");
                    } else {
                        result = false;
                        message = json1.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                message = "No Internet Connection Found! Check Internet Connection...";
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if(!res){
                if(result){
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent nxt = new Intent(getApplicationContext(), doctorStatus.class);
                    nxt.putExtra("status","Pending");
                    startActivity(nxt);
                }

            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent back=new Intent(getApplicationContext(),main.class);
        startActivity(back);
    }
}
