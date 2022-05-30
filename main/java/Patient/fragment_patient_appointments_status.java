package com.example.shahidhussain.assignemnt2.Patient;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.example.shahidhussain.assignemnt2.services.MySingleton;
import com.example.shahidhussain.assignemnt2.services.urlBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class fragment_patient_appointments_status extends Fragment implements OnMapReadyCallback{

    private String email="";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=false;
    private  String path = new dbPath("appointments").getPath();
    View view;
    private String message="";

    private String doctorName ;
    private String appointmentnumber ;
    private String latitude;
    private String longitude;
    private String hospitalAddress ;
    private String status;
    private GoogleMap mMap;
    private int permsRequestCode = 200;
    JSONObject json1=null;
    private boolean complete=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_fragment_patient_appointments_status, container, false);

        return view;
    }
    private void setMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        email=pref.getString("email", "");
         getAppointmentInfo();
    }
    private void getAppointmentInfo() {

        TextView status1 = view.findViewById(R.id.appointmentstatus);
        status1.setText("Checking Appointment Status.....");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("request","getAppointmentDetail");
        params.put("pemail",email);
        urlBuilder url=new urlBuilder();
        String updatedpath=url.buildURI(path,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, updatedpath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        TextView status1 = view.findViewById(R.id.appointmentstatus);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int success = jsonObject.getInt("flag");
                            status=jsonObject.getString("message");
                            if (success == 1) {
                                found = true;
                                JSONObject detail = jsonObject.getJSONObject("detail");
                                doctorName = detail.getString("doctorName");
                                appointmentnumber = detail.getString("appointmentnumber");
                                latitude = detail.getString("latitude");
                                longitude = detail.getString("longitude");
                                hospitalAddress = detail.getString("hospitalAddress");
                                status1.setText(message);
                                if (status.equals("Appointment is Accepted")) {
                                    LinearLayout linearLayout = view.findViewById(R.id.appointmentDetailLayout);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    TextView doctorName1 = view.findViewById(R.id.doctornametxt);
                                    TextView appointmentNumber1 = view.findViewById(R.id.appointmentnumber);
                                    TextView Address1 = view.findViewById(R.id.addresstxt);
                                    status1.setText(status);
                                    doctorName1.setText(doctorName);
                                    appointmentNumber1.setText(appointmentnumber);
                                    Address1.setText(hospitalAddress);
                                    setMap();


                                } else if (status.equals("Appointment is In_progress")) {
                                    LinearLayout linearLayout = view.findViewById(R.id.appointmentDetailLayout);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    TextView doctorName1 = view.findViewById(R.id.doctornametxt);
                                    TextView appointmentNumber1 = view.findViewById(R.id.appointmentnumber);
                                    TextView Address1 = view.findViewById(R.id.addresstxt);
                                    status1.setText(status);
                                    doctorName1.setText(doctorName);
                                    appointmentNumber1.setText("Not issued yet");
                                    Address1.setText("------");

                                } else if (status.equals("Appointment is Rejected") ) {
                                    LinearLayout linearLayout = view.findViewById(R.id.appointmentDetailLayout);
                                    linearLayout.setVisibility(View.GONE);
                                    status1.setText(status);

                                }else if (status.equals("Appointment is Complete") ) {
                                    LinearLayout linearLayout = view.findViewById(R.id.appointmentDetailLayout);
                                    linearLayout.setVisibility(View.GONE);
                                    status1.setText("No Appointment Found");

                                }else{
                                    LinearLayout linearLayout = view.findViewById(R.id.appointmentDetailLayout);
                                    linearLayout.setVisibility(View.GONE);
                                }
                            } else {
                                message=status;
                                status1.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    TextView status1 = view.findViewById(R.id.appointmentstatus);
                    status1.setText("Timeout Error");
                }
                else if(error instanceof NoConnectionError)
                {
                    TextView status1 = view.findViewById(R.id.appointmentstatus);
                    status1.setText("No Connection");
                }
                else if (error instanceof AuthFailureError) {
                    TextView status1 = view.findViewById(R.id.appointmentstatus);
                    status1.setText(error.toString());
                    //TODO
                } else if (error instanceof ServerError) {
                    TextView status1 = view.findViewById(R.id.appointmentstatus);
                    status1.setText(error.toString());
                    //TODO
                } else if (error instanceof NetworkError) {
                    TextView status1 = view.findViewById(R.id.appointmentstatus);
                    status1.setText(error.toString());
                    //TODO
                } else if (error instanceof ParseError) {
                    TextView status1 = view.findViewById(R.id.appointmentstatus);
                    status1.setText(error.toString());
                    //TODO
                }


            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingleton.getmInstance(getContext()).addToRequestQue(stringRequest);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    String[] perms = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

                    requestPermissions(perms, permsRequestCode);
                } else {
                    mMap.setTrafficEnabled(true);
                    mMap.setIndoorEnabled(true);
                    mMap.setBuildingsEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Location").snippet(doctorName));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(18).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
}
