package com.example.shahidhussain.assignemnt2.Registrations;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;
import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.Doctor.doctorStatus;
import com.example.shahidhussain.assignemnt2.Login.main;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class doctorRegistration extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String message="";
    private boolean res = false;
    private boolean result = false;
    private Button back;
    private Button next;
    private  String path = new dbPath("doctorCRUD").getPath();
    private Fragment fragement;

    private InputStream inputStream;

    private ImageView image;
    private EditText workexperience;
    private EditText aboutme;
    private EditText Specialization;
    private EditText qualification;
    private EditText experienceYear;
    private EditText name1;
    private EditText email1;
    private EditText password1;
    private EditText number1;
    private EditText hospital1;
    private EditText city1;
    private EditText pmdc1;
    private EditText fee1;
    private doctorJavaClass obj;
    int fragmentposition=1;
    String Image_name;
    final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
    int position=0;

    private void replacefragment(Fragment fragement){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(position==1)
            ft.setCustomAnimations(R.anim.image_swith_in_rc,R.anim.image_swith_out_cl);
        else if(position==0)
            ft.setCustomAnimations(R.anim.image_swith_in_lc,R.anim.image_swith_out_cr);
        ft.replace(R.id.registrationframelayout, fragement);
        this.fragement=fragement;
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
    private void goback(){

        position=0;
        if(fragmentposition==3){
            next.setText("NEXT");
            fragmentposition=2;
            replacefragment(new doctor_registrationf2());
        }else if(fragmentposition==2){
            fragmentposition=1;
            workexperience=fragement.getView().findViewById(R.id.workexperience);
            aboutme=fragement.getView().findViewById(R.id.aboutme);
            Specialization=fragement.getView().findViewById(R.id.Specialization);
            qualification=fragement.getView().findViewById(R.id.qualification);
            experienceYear=fragement.getView().findViewById(R.id.experienceYear);
            obj.setExperience(workexperience.getText().toString());
            obj.setAbout(aboutme.getText().toString());
            obj.setSpecialization(Specialization.getText().toString());
            obj.setQualitfication(qualification.getText().toString());
            obj.setExperienceYear(experienceYear.getText().toString());
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("registrationPref", 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            final Gson gson = new Gson();
            String serializedObject = gson.toJson(obj);
            sharedPreferencesEditor.putString("registrationInfo", serializedObject);
            sharedPreferencesEditor.apply();
            next.setText("NEXT");
            replacefragment(new doctor_registrationf1());
        }else if(fragmentposition==1){
            SharedPreferences settings = getSharedPreferences("registrationPref", 0);
            settings.edit().clear().commit();
            Intent intent=new Intent(getApplicationContext(),main.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed(){
        goback();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        replacefragment(new doctor_registrationf1());
        next= findViewById(R.id.nextbtn);
        back= findViewById(R.id.backbtn);
        final TextView setname=findViewById(R.id.title);
        setname.setText("Doctor Registration(Portfolio)");
        ImageView backbtn=findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goback();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goback();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=1;
                if(fragmentposition==1){
                    fragmentposition=2;
                    name1=fragement.getView().findViewById(R.id.name);
                    email1=fragement.getView().findViewById(R.id.email);
                    password1=fragement.getView().findViewById(R.id.password);
                    number1=fragement.getView().findViewById(R.id.number);
                    hospital1=fragement.getView().findViewById(R.id.hospital);
                    city1=fragement.getView().findViewById(R.id.city);
                    pmdc1=fragement.getView().findViewById(R.id.pmdc);
                    fee1=fragement.getView().findViewById(R.id.fee);
                    if(!name1.getText().toString().equals("") && !name1.getText().toString().equals(null) &&
                            !email1.getText().equals("") && !email1.getText().toString().equals(null) &&
                            !password1.getText().toString().equals("") && !password1.getText().toString().equals(null) &&
                            !number1.getText().toString().equals("") && !number1.getText().toString().equals(null) &&
                            !hospital1.getText().toString().equals("") && !hospital1.getText().toString().equals(null) &&
                            !city1.getText().toString().equals("") && !city1.getText().toString().equals(null) &&
                            !pmdc1.getText().toString().equals("") && !pmdc1.getText().toString().equals(null) &&
                            !fee1.getText().toString().equals("") && !fee1.getText().toString().equals(null) ){
                        SharedPreferences sharedPreferences = getSharedPreferences("registrationPref", 0);
                        if (sharedPreferences.contains("registrationInfo")) {
                            final Gson gson = new Gson();
                            obj = gson.fromJson(sharedPreferences.getString("registrationInfo", ""), doctorJavaClass.class);
                        }else{
                            obj=new doctorJavaClass();
                        }
                        obj.setName(name1.getText().toString());
                        obj.setEmail(email1.getText().toString());
                        obj.setPassword(password1.getText().toString());
                        obj.setNum(number1.getText().toString());
                        obj.setCity(city1.getText().toString());
                        obj.setHospitalname(hospital1.getText().toString());
                        obj.setPmdc(pmdc1.getText().toString());
                        obj.setFee(fee1.getText().toString());
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        final Gson gson = new Gson();
                        String serializedObject = gson.toJson(obj);
                        sharedPreferencesEditor.putString("registrationInfo", serializedObject);
                        sharedPreferencesEditor.apply();
                        back.setEnabled(true);
                        setname.setText("Doctor Registration");
                        replacefragment(new doctor_registrationf2());
                    }else {
                        Toast.makeText(doctorRegistration.this, "Please fill all required field first ....", Toast.LENGTH_SHORT).show();
                    }

                }else if(fragmentposition==2){

                    fragmentposition=3;
                    workexperience=fragement.getView().findViewById(R.id.workexperience);
                    aboutme=fragement.getView().findViewById(R.id.aboutme);
                    Specialization=fragement.getView().findViewById(R.id.Specialization);
                    qualification=fragement.getView().findViewById(R.id.qualification);
                    experienceYear=fragement.getView().findViewById(R.id.experienceYear);
                    obj.setExperience(workexperience.getText().toString());
                    obj.setAbout(aboutme.getText().toString());
                    obj.setSpecialization(Specialization.getText().toString());
                    obj.setQualitfication(qualification.getText().toString());
                    obj.setExperienceYear(experienceYear.getText().toString());
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("registrationPref", 0);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    final Gson gson = new Gson();
                    String serializedObject = gson.toJson(obj);
                    sharedPreferencesEditor.putString("registrationInfo", serializedObject);
                    sharedPreferencesEditor.apply();
                    next.setText("signup");
                    replacefragment(new doctor_registrationf3());
                    
                }else if(fragmentposition==3){
                    SharedPreferences sharedPreferences = getSharedPreferences("registrationPref", 0);
                    if (sharedPreferences.contains("registrationInfo")) {
                        final Gson gson = new Gson();
                        obj = gson.fromJson(sharedPreferences.getString("registrationInfo", ""), doctorJavaClass.class);
                    }
                    if(obj.getImage()!=null)
                    {
                        Bitmap bitmap =obj.getImage();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream); //compress to which format you want.
                        byte [] byte_arr = stream.toByteArray();
                        String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
                        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                        Image_name="doctorsProfilePictures/"+String.valueOf(number)+".png";
                        nameValuePairs.add(new BasicNameValuePair("image",image_str));
                        nameValuePairs.add(new BasicNameValuePair("name",Image_name));
                        new CreateNew().execute();

                    }
                    else
                        Toast.makeText(doctorRegistration.this, "Please Upload Image First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    class CreateNew extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(doctorRegistration.this);
            pDialog.setMessage("It Will Take Time.Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String email = "";
            param.add(new BasicNameValuePair("email", obj.getEmail()));
            param.add(new BasicNameValuePair("pmdc", obj.getPmdc()));
            param.add(new BasicNameValuePair("type", "isfound"));
            JSONObject json = jsonParser.makeHttpRequest(path, "GET", param);
            if(json!=null) {
                try {
                    // Checking data exist befor or not
                    int success = json.getInt("flag");
                    if (success == 0) {
                        res = false;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("name", obj.getName()));
                        params.add(new BasicNameValuePair("email", obj.getEmail()));
                        params.add(new BasicNameValuePair("password", obj.getPassword()));
                        params.add(new BasicNameValuePair("num", obj.getNum()));
                        params.add(new BasicNameValuePair("city", obj.getCity()));
                        params.add(new BasicNameValuePair("pmdcnumber", obj.getPmdc()));
                        params.add(new BasicNameValuePair("hospitalname", obj.getHospitalname()));
                        params.add(new BasicNameValuePair("fee", obj.getFee()));
                        params.add(new BasicNameValuePair("experience", obj.getExperience()));
                        params.add(new BasicNameValuePair("about", obj.getAbout()));
                        params.add(new BasicNameValuePair("specialization", obj.getSpecialization()));
                        params.add(new BasicNameValuePair("qualification", obj.getQualitfication()));
                        params.add(new BasicNameValuePair("imageurl", Image_name));
                        params.add(new BasicNameValuePair("year", obj.getExperienceYear()));
                        params.add(new BasicNameValuePair("status", "pending"));
                        params.add(new BasicNameValuePair("type", "create"));
                        // getting JSON Object
                        // Note that create product url accepts POST method
                        JSONObject json1 = jsonParser.makeHttpRequest(path, "GET", params);

                        if (json1 != null) {
                            int success1 = json1.getInt("flag");
                            if (success1 == 1) {
                                result = true;
                                message = json1.getString("message");
                                try{
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost("https://phealthcarecom.000webhostapp.com/FYP/upload_image.php");
                                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                    HttpResponse response = httpclient.execute(httppost);
                                    final String the_string_response = convertResponseToString(response);

                                }catch(final Exception e){
                                    Toast.makeText(doctorRegistration.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                result = false;
                                message = json1.getString("message");
                            }
                        } else {
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
                    Intent nxt = new Intent(getApplicationContext(), doctorStatus.class);
                    nxt.putExtra("status","Pending");
                    startActivity(nxt);
                }

            }
        }
    }
    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{

        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

            }
        });

        if (contentLength < 0){
        }
        else{
            byte[] data = new byte[512];
            int len = 0;
            try
            {
                while (-1 != (len = inputStream.read(data)) )
                {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                inputStream.close(); // closing the stream…..
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to string…..

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                }
            });
            //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }
}
