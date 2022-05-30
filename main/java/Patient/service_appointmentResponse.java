package com.example.shahidhussain.assignemnt2.Patient;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
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
import com.example.shahidhussain.assignemnt2.services.MySingleton;
import com.example.shahidhussain.assignemnt2.services.urlBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class service_appointmentResponse extends Service {
    // constant
    public static long NOTIFY_INTERVAL = (1000*5); // for 5 sec

    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    JSONParser jsonParser = new JSONParser();
    private boolean found=false;
    private  String path = new dbPath("appointmentsstatus").getPath();
    View view;
    private String message="";
    JSONObject json1=null;
    int responseStatus=0;
    boolean checked1=false;
    int oldstatus;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 1, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    //Service Task Here
                     getAppointmentInfo1();

                }

            });
        }
    }

    private void getAppointmentInfo1() {
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String email=pref.getString("email", "");
        Map<String,String> params=new HashMap<>();
        params.put("pemail",email);
        urlBuilder url=new urlBuilder();
        String updatedpath=url.buildURI(path,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, updatedpath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject!=null){
                                responseStatus = jsonObject.getInt("flag");
                                if (responseStatus == 3 ||responseStatus == 2  ) {
                                    found = true;
                                }
                                SharedPreferences pref = getSharedPreferences("StatusPref", MODE_PRIVATE);
                                oldstatus=Integer.parseInt(pref.getString("status", "0"));
                                if (found && oldstatus!=responseStatus) {
                                    if(responseStatus==3) {

                                        SharedPreferences StatusPref = getApplicationContext().getSharedPreferences("StatusPref",
                                                MODE_PRIVATE);
                                        SharedPreferences.Editor editor = StatusPref.edit();
                                        editor.putString("status", String.valueOf(responseStatus));
                                        editor.apply();
                                        showNotification(getApplicationContext(), fragment_patient_appointments_status.class, "Congratulation your " +
                                                "appointment is Accepted",R.drawable.happy);
                                    }else if(responseStatus==2) {

                                        SharedPreferences StatusPref = getApplicationContext().getSharedPreferences("StatusPref",
                                                MODE_PRIVATE);
                                        SharedPreferences.Editor editor = StatusPref.edit();
                                        editor.putString("status", String.valueOf(responseStatus));
                                        editor.apply();
                                        showNotification(getApplicationContext(), fragment_patient_appointments_status.class, "Oho your " +
                                                "appointment is being Rejected",R.drawable.sad);
                                    }
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

    public  void showNotification(Context context, Class<?> cls, String subject, int icon)
    {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_04";

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


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);


        Intent notificationIntent = new Intent(context, fragment_patient_appointments_status.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notificationBuilder.setContentIntent(intent);

        notificationBuilder.setAutoCancel(true)
                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icon)
                .setTicker("Hearty365")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Appointment Reservation")
                .setContentText(subject)
                .setContentInfo("Info");

        notificationManager.notify(/*notification id*/3, notificationBuilder.build());
        checked1=true;
    }
}
