package com.example.shahidhussain.assignemnt2.Patient;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.R;
import com.google.android.gms.maps.GoogleMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class service_appointmentReminder extends Service {
    // constant
    public static long NOTIFY_INTERVAL = (1000*5); // for 1 min
    //public static long NOTIFY_INTERVAL = (1000*60*20); // for 20 min
    //public static long NOTIFY_INTERVAL = (1000*60*60*20); // for 20 hour
    //public static final long NOTIFY_INTERVAL = 10*1000; // for 10 seconds

    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=false;
    private  String path = new dbPath("appointmentsInfoForNotification").getPath();
    View view;
    private String message="";

    private String doctorName ;
    private String appointmentnumber ;
    private int starthour;
    private int endhour;
    private int startmin ;
    private int endmin;
    private String status;
    private int nowTimeHour;
    private int nowTimeMin;
    JSONObject json1=null;
    boolean checked=false;

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
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    //Service Task Here
                    new getAppointmentInfo().execute();

                }

            });
        }
    }
    class getAppointmentInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {super.onPreExecute();}
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
            String email=pref.getString("email", "");
            params.add(new BasicNameValuePair("pemail", email));
            json1 = jsonParser.makeHttpRequest(path, "GET", params);

            if(json1!=null) {
                try {
                    int success = json1.getInt("flag");
                    if (success == 1) {
                        found = true;
                        JSONObject detail = json1.getJSONObject("detail");
                        doctorName = detail.getString("doctorName");
                        appointmentnumber = detail.getString("appointmentnumber");
                        starthour = Integer.parseInt(detail.getString("starthour"));
                        endhour = Integer.parseInt(detail.getString("endhour"));
                        startmin = Integer.parseInt(detail.getString("startmin"));
                        endmin = Integer.parseInt(detail.getString("endmin"));
                        nowTimeHour = Integer.parseInt(detail.getString("nowTimeHour"));
                        nowTimeMin = Integer.parseInt(detail.getString("nowTimeMin"));

                    } else {
                        message=status;
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
            if(json1!=null) {
                if (found  &&  starthour>nowTimeHour ) {
                    setReminder(AlarmReceiver.class,
                            9, 11);
                    checked=true;
                }else{
                    //cancelReminder(AlarmReceiver.class);
                }
            }
        }
    }
    public  void cancelReminder(Class<?> cls)
    {
        // Disable a receiver
        ComponentName receiver = new ComponentName(this, cls);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(this, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public void setReminder(Class<?> cls, int hour, int min)
    {
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders

        //cancelReminder(context,cls);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver
        ComponentName receiver = new ComponentName(this, cls);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(this, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
