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
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.Login.main;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class patientregistration extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String name="";
    private String email="";
    private String password="";
    private String number="";
    private String city="";
    private String address="";
    private String zipcode="";
    private String message="";
    private String type="";
    private boolean res = false;
    private boolean result = false;
    private  String path = new dbPath("patientCRUD").getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        final EditText name1=findViewById(R.id.name);
        final EditText email1=findViewById(R.id.email);
        final EditText password1=findViewById(R.id.password);
        final EditText number1=findViewById(R.id.number);
        final EditText address1=findViewById(R.id.address);
        final EditText city1=findViewById(R.id.city);
        final EditText zipcode1=findViewById(R.id.zipcode);
        Button btn= findViewById(R.id.next);
        TextView setname=findViewById(R.id.title);
        setname.setText("Patient Registration(Portfolio)");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = name1.getText().toString();
                email = email1.getText().toString();
                password = password1.getText().toString();
                number = number1.getText().toString();
                city = city1.getText().toString();
                address = address1.getText().toString();
                zipcode = zipcode1.getText().toString();
                if (name.length() > 0 && email.length() > 0 && password.length() > 0) {
                    new CreateNew().execute();
                }
            }
        });
    }
    class CreateNew extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(patientregistration.this);
            pDialog.setMessage("Creating..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("email", email));
            param.add(new BasicNameValuePair("type", "isfound"));
            JSONObject json = jsonParser.makeHttpRequest(path, "GET", param);

            if(json!=null) {
                try {
                    // Checking data exist befor or not
                    int success = json.getInt("flag");
                    if (success == 0) {
                        res = false;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("name", name));
                        params.add(new BasicNameValuePair("email", email));
                        params.add(new BasicNameValuePair("password", password));
                        params.add(new BasicNameValuePair("num", number));
                        params.add(new BasicNameValuePair("city", city));
                        params.add(new BasicNameValuePair("address", address));
                        params.add(new BasicNameValuePair("zipcode", zipcode));
                        params.add(new BasicNameValuePair("type", "create"));
                        // getting JSON Object
                        // Note that create product url accepts POST method
                        JSONObject json1 = jsonParser.makeHttpRequest(path, "GET", params);

                        if(json!=null) {
                            int success1 = json1.getInt("flag");
                            if (success1 == 1) {
                                result = true;
                                message = json1.getString("message");
                            } else {
                                result = false;
                                message = json1.getString("message");
                            }
                        }else{
                            message = "No Internet Connection Found! Check Internet Connection...";
                        }
                    } else {
                        res = true;
                        message = json.getString("message");
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
                    pDialog.dismiss();Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent nxt = new Intent(getApplicationContext(), main.class);
                    startActivity(nxt);
                }

            }
        }
    }
}
