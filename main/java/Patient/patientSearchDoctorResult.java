package com.example.shahidhussain.assignemnt2.Patient;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class patientSearchDoctorResult extends AppCompatActivity {

    doctorJavaClass obj;

    ImageView imge;
    TextView name;
    TextView doctoremail;
    TextView experience;
    TextView fee;
    TextView ratetxt;
    TextView distance;
    RatingBar rate;
    String email="";
    String status="";
    String message="";
    Button biobtn;
    Button reviewbtn;
    Button detailbtn;
    Button callDoctor;
    Button reserveAppointment;
    private int permsRequestCode = 200;
    private ProgressDialog pDialog;
    JSONObject json1=null;

    JSONParser jsonParser = new JSONParser();
    private int found=0;
    private  String path = new dbPath("appointments").getPath();

    private void replacefragment(Fragment fragement){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.doctorDetaildefaultframelayout, fragement);
        ft.commit();
    }
    public void btnsetting(int p){
        biobtn.setTextColor(Color.parseColor("#ffffff"));
        reviewbtn.setTextColor(Color.parseColor("#ffffff"));
        detailbtn.setTextColor(Color.parseColor("#ffffff"));
        if(p==1)
            biobtn.setTextColor(Color.parseColor("#fc0000"));
        else if(p==2)
            reviewbtn.setTextColor(Color.parseColor("#fc0000"));
        else if(p==3)
            detailbtn.setTextColor(Color.parseColor("#fc0000"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_search_doctor_result);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        email=pref.getString("email", "");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mPreference", 0);
        if (sharedPreferences.contains("DoctorInfo")) {
            final Gson gson = new Gson();
            obj= gson.fromJson(sharedPreferences.getString("DoctorInfo", ""), doctorJavaClass.class);
            ImageView back=findViewById(R.id.backbutton);
            biobtn=findViewById(R.id.biobtn);
            reviewbtn=findViewById(R.id.reviewbtn);
            detailbtn=findViewById(R.id.practiceinfobtn);
            callDoctor=findViewById(R.id.calldoctorbtn);
            reserveAppointment=findViewById(R.id.reserveappointmentbtn);
            imge= findViewById(R.id.doctorPic);
            name= findViewById(R.id.doctorName);
            doctoremail= findViewById(R.id.doctoremail);
            experience= findViewById(R.id.experienceYear);
            fee= findViewById(R.id.doctorPrice);
            rate= findViewById(R.id.doctorRating);
            ratetxt=findViewById(R.id.ratetxt);
            distance=findViewById(R.id.distance);
            name.setText(obj.getName());
            if(obj.getImage()!=null)
                imge.setImageBitmap(obj.getImage());
            else
                imge.setImageResource(R.drawable.doctor1);
            doctoremail.setText(obj.getEmail());
            rate.setNumStars(Integer.parseInt(obj.getTotalrate()));
            rate.setRating(Float.parseFloat(obj.getGetrate()));
            ratetxt.setText(obj.getGetrate());
            fee.setText(obj.getFee());
            experience.setText(obj.getExperienceYear());
            distance.setText(obj.getDistance());
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    patientSearchDoctorResult.super.onBackPressed();
                }
            });
            biobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnsetting(1);
                    replacefragment(new fragment_DoctorBio());
                }
            });
            reviewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnsetting(2);
                    //replacefragment(new fragment_DoctorReviews());
                    Toast.makeText(patientSearchDoctorResult.this, "This is under Construction", Toast.LENGTH_SHORT).show();
                }
            });
            detailbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        String[] perms = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

                        requestPermissions(perms, permsRequestCode);
                    } else {
                        btnsetting(3);
                        replacefragment(new fragment_DoctorPracticeInfo());
                    }
                }
            });
            callDoctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+obj.getNum()));

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        String[] perms = {"android.permission.CALL_PHONE"};

                        requestPermissions(perms, permsRequestCode);
                        return;
                    }
                    else
                        startActivity(callIntent);
                }
            });
            reserveAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reserveAppointment();
                }
            });
            btnsetting(1);
            replacefragment(new fragment_DoctorBio());
        }
      }

    private void reserveAppointment() {
        pDialog = new ProgressDialog(patientSearchDoctorResult.this);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("email",email);
        params.put("doctorid",obj.getId());
        params.put("request","reserveAppointment");
        urlBuilder url=new urlBuilder();
        String updatedpath=url.buildURI(path,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, updatedpath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject!=null){
                                int success = jsonObject.getInt("flag");
                                if (success==1) {
                                    showNotification(getApplicationContext(), fragment_patient_appointments_status.class);
                                }else if (success==0) {
                                    Toast.makeText(patientSearchDoctorResult.this, "Cannot reserve appointment", Toast.LENGTH_SHORT).show();
                                }else if (success==-1) {
                                    Toast.makeText(patientSearchDoctorResult.this, "Appoinntment Reserve Already", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Timeout Error",
                            Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError)
                {
                    Toast.makeText(getApplicationContext(), "No Connection",
                            Toast.LENGTH_LONG).show();

                }
                else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
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
        MySingleton.getmInstance(this).addToRequestQue(stringRequest);
    }
    public  void showNotification(Context context, Class<?> cls)
    {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_02";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        Intent notificationIntent = new Intent(context, fragment_patient_appointments_status.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notificationBuilder.setContentIntent(intent);

        notificationBuilder.setAutoCancel(true)
                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notification)
                .setTicker("Hearty365")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Appointment Reservation")
                .setContentText("Your Appointment Request is forwarded.")
                .setContentInfo("Info");


        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }
}
