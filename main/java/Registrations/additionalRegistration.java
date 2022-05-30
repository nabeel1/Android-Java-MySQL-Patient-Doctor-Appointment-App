package com.example.shahidhussain.assignemnt2.Registrations;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.Doctor.DoctorDashBoard;
import com.example.shahidhussain.assignemnt2.Login.main;
import com.example.shahidhussain.assignemnt2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class additionalRegistration extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    JSONParser jsonParser = new JSONParser();
    private  String path = new dbPath("getDoctorInfo").getPath();
    private  String path1 = new dbPath("doctorCRUD").getPath();
    public ArrayList<String> dcategory=new ArrayList<>();
    private String email="";
    double latitude=0;
    double longitude=0;
    private boolean flag=false;
    private String message="";
    final String[] chooseCategory = new String[1];
    private ProgressDialog pDialog;
    private boolean iscategoryload=false;
    private int viewid=0;
    private String starthour="";
    private String startmin="";
    private String endhour="";
    private String endmin="";
    private String hospitaladdress="";
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private int permsRequestCode = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_registration);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        TextView starttxt=findViewById(R.id.start);
        TextView endtxt=findViewById(R.id.end);
        starttxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    final ConstraintLayout timer=findViewById(R.id.timer);
                    timer.setVisibility(View.VISIBLE);
                    viewid=v.getId();
                    return true;
                }
                return false;
            }
        });
        endtxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    final ConstraintLayout timer=findViewById(R.id.timer);
                    timer.setVisibility(View.VISIBLE);
                    viewid=v.getId();
                    return true;
                }
                return false;
            }
        });
        Button savetime=findViewById(R.id.savetime);
        Button backtime=findViewById(R.id.backtime);
        savetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timepicker=findViewById(R.id.timePicker1);
                if (viewid == R.id.start) {
                    TextView start=findViewById(R.id.start);
                    String time=String.valueOf(timepicker.getHour())+":"+String.valueOf(timepicker.getMinute());
                    start.setText(time);
                    starthour=String.valueOf(timepicker.getHour());
                    startmin=String.valueOf(timepicker.getMinute());
                }else if (viewid == R.id.end) {
                    TextView end=findViewById(R.id.end);
                    String time=String.valueOf(timepicker.getHour())+":"+String.valueOf(timepicker.getMinute());
                    end.setText(time);
                    endhour=String.valueOf(timepicker.getHour());
                    endmin=String.valueOf(timepicker.getMinute());
                }
                final ConstraintLayout timer=findViewById(R.id.timer);
                timer.setVisibility(View.GONE);
            }
        });
        backtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ConstraintLayout timer=findViewById(R.id.timer);
                timer.setVisibility(View.GONE);
            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        email=pref.getString("email",null);
        new getCategory().execute();

        FloatingActionButton mapbtn=findViewById(R.id.map);
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***********************Place Picker************************
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    String[] perms = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

                    requestPermissions(perms, permsRequestCode);
                } else {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(additionalRegistration.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        TextView setname=findViewById(R.id.title);
        EditText doctoremail=findViewById(R.id.doctoremail);

        doctoremail.setText(email);

        Spinner sp=findViewById(R.id.category);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                chooseCategory[0] = dcategory.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        setname.setText("Additional Information");
        Button btn= findViewById(R.id.next);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latitude==0 && longitude==0){
                    Toast.makeText(getApplicationContext(),"Kindlly select your Location",Toast.LENGTH_LONG).show();
                }else{
                    TextView starttxt=findViewById(R.id.start);
                    TextView endtxt=findViewById(R.id.end);
                    TextView timedifference=findViewById(R.id.timedifference);
                    if(!starttxt.getText().toString().equals("") &&!starttxt.getText().toString().equals(null) &&
                            !endtxt.getText().toString().equals("") &&!endtxt.getText().toString().equals(null) &&
                            !chooseCategory[0].equals("") &&!chooseCategory[0].equals(null) &&
                            !timedifference.getText().toString().equals("") &&!timedifference.getText().toString().equals(null))
                    new store().execute();
                    else
                        Toast.makeText(additionalRegistration.this, "Please Enter Required Information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                for (int i=0;i<grantResults.length;i++){
                    boolean p = grantResults[i]== PackageManager.PERMISSION_GRANTED;
                }
                break;
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(address);
                TextView address1=findViewById(R.id.address);
                address1.setText(stBuilder);
                hospitaladdress=stBuilder.toString();
                this.latitude=Double.parseDouble(latitude);
                this.longitude=Double.parseDouble(longitude);
            }
        }
    }

    @Override
    public void onBackPressed() {
        final ConstraintLayout timer = findViewById(R.id.timer);
        if (timer.getVisibility() == View.VISIBLE ) {
            timer.setVisibility(View.GONE);
        }
        else{
            Intent back=new Intent(getApplicationContext(),main.class);
            startActivity(back);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class getCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(additionalRegistration.this);
            pDialog.setMessage("Fetching Category..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("request", "getcategory"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(path, "GET", params);

            if(json1!=null) {
                try {
                    int success = json1.getInt("success");
                    if (success == 1) {
                        JSONArray categoryArray = json1.getJSONArray("category");
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            String category = c.getString("category");
                            dcategory.add(category);
                        }
                        chooseCategory[0] = dcategory.get(0);
                    }
                    iscategoryload=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                message="No Internet Connection Found! Check Internet Connection...";
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
             pDialog.cancel();
            if(!message.equals(""))
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Spinner sp=findViewById(R.id.category);
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.text, dcategory);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(adp1);

        }
    }
    class store extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(additionalRegistration.this);
            pDialog.setMessage("Updating Information..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {

            EditText timedifference=findViewById(R.id.timedifference);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("start_hour", starthour));
            params.add(new BasicNameValuePair("start_min", startmin));
            params.add(new BasicNameValuePair("end_hour", endhour));
            params.add(new BasicNameValuePair("end_min", endmin));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("timedifference", String.valueOf(timedifference.getText())));
            params.add(new BasicNameValuePair("category", chooseCategory[0]));
            params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
            params.add(new BasicNameValuePair("address", hospitaladdress));
            params.add(new BasicNameValuePair("type", "additionalupdate"));


            JSONObject json1 = jsonParser.makeHttpRequest(path1, "GET", params);
            if(json1!=null) {
                try {
                    int success = json1.getInt("flag");
                    if (success == 1) {
                        flag = true;
                        message="Update SuccessFully";
                    }else{
                        message="Fail to Update Record";
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
             pDialog.cancel();
            if(!message.equals(""))
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if(flag){

                Intent nxt = new Intent(getApplicationContext(), DoctorDashBoard.class);
                startActivity(nxt);
            }

        }
    }
}
