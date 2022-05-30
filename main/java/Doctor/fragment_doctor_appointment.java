package com.example.shahidhussain.assignemnt2.Doctor;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.appointmentDetail;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class fragment_doctor_appointment extends Fragment {

    View rootview;
    String email="";
    String type="";
    private String message="";
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=true;
    ArrayList<appointmentDetail> pendingAppointmentLists=new ArrayList<>();
    private  String path = new dbPath("getappointmentDetail").getPath();
    private  String appointmentpath = new dbPath("saveappointmentDetail").getPath();
    ListView appointmentlist;
    private LinearLayout timer;
    private Button savetime;
    private Button backtime;
    boolean settimeflag=false;
    int appointmentposition=0;
    String token="0";
    CustomAppointmentAdapter appointmentAdapter=new CustomAppointmentAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_fragment_doctor_appointment, container, false);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        email=pref.getString("email", "");
        type=pref.getString("type", "");
        appointmentlist=rootview.findViewById(R.id.appointmentlist);
        timer=rootview.findViewById(R.id.appointmenttimer);
        savetime=rootview.findViewById(R.id.savetime);
        backtime=rootview.findViewById(R.id.backtime);
        new getappointmentdetail().execute();
        return  rootview;
    }

    class getappointmentdetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Getting Data....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("type", type));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(path, "GET", params);

            if(json1!=null) {
                try {
                    int success = json1.getInt("flag");
                    if (success == 1) {
                        found=true;
                        JSONArray categoryArray = json1.getJSONArray("detail");
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            appointmentDetail list=new appointmentDetail();
                            list.setPatientId(String.valueOf(c.getInt("pid")));
                            list.setDoctorId(c.getString("did"));
                            list.setAppointmentId(c.getString("appointmentid"));
                            list.setPatientName(c.getString("pname"));
                            list.setNumber(c.getString("number"));
                            list.setStarthour(c.getString("starthour"));
                            list.setStartmin(c.getString("startmin"));
                            pendingAppointmentLists.add(list);
                        }
                        message="Appointment";
                    } else {
                        message="No Appointment Found";
                        found = false;
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
            pDialog.dismiss();
            if(!message.equals("")){
                TextView appointmentstatus1=rootview.findViewById(R.id.appointmentstatus1);
                appointmentstatus1.setText(message);
            }
            if(found) {
                appointmentlist.setVisibility(View.VISIBLE);
                appointmentlist.setAdapter(appointmentAdapter);
            }else{

                appointmentlist.setVisibility(View.GONE);
            }
        }
    }
    public class CustomAppointmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pendingAppointmentLists.size();
        }

        @Override
        public Object getItem(int position) {
            return null; //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // inflate the layout for each list row
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).
                        inflate(R.layout.custom_appointments, parent, false);
                TextView patientname=convertView.findViewById(R.id.patientname);
                TextView patientnumber=convertView.findViewById(R.id.patientnumber);
                final TextView patienttime=convertView.findViewById(R.id.patienttime);
                final EditText patienttoken=convertView.findViewById(R.id.patienttoken);
                Button savepatientappointment=convertView.findViewById(R.id.savepatientappointment);
                Button rejectpatientappointment=convertView.findViewById(R.id.rejectpatientappointment);
                patientname.setText(pendingAppointmentLists.get(position).getPatientName());
                patientnumber.setText(pendingAppointmentLists.get(position).getNumber());

                savepatientappointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(patienttoken.length()>0){
                            token=patienttoken.getText().toString();
                            appointmentposition=position;
                            new acceptAppointment().execute();
                        }else{
                            Toast.makeText(getContext(), "Please Enter Token number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                rejectpatientappointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appointmentposition=position;
                        new rejectAppointment().execute();
                    }
                });
                patienttime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timer.setVisibility(View.VISIBLE);
                    }
                });
                savetime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePicker timepicker=rootview.findViewById(R.id.timePicker1);
                        String newtime=String.valueOf(timepicker.getHour())+":"+String.valueOf(timepicker.getMinute());
                        String hour=String.valueOf(timepicker.getHour());
                        String min=String.valueOf(timepicker.getMinute());
                        pendingAppointmentLists.get(position).setStarthour(hour);
                        pendingAppointmentLists.get(position).setStartmin(min);
                        patienttime.setText(newtime);
                        timer.setVisibility(View.GONE);
                        settimeflag=true;
                    }
                });
                backtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timer.setVisibility(View.GONE);
                    }
                });
            }
            return convertView;
        }
    }

    class acceptAppointment extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Request is processing.Please wait....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appointmentid", pendingAppointmentLists.get(appointmentposition).getAppointmentId()));
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("starthour", pendingAppointmentLists.get(appointmentposition).getStarthour()));
            params.add(new BasicNameValuePair("startmin", pendingAppointmentLists.get(appointmentposition).getStartmin()));
            params.add(new BasicNameValuePair("type", "save"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(appointmentpath, "GET", params);

            if(json1!=null) {
                try {
                    int success = json1.getInt("flag");
                    if (success == 1) {
                        found=true;
                        message="Save Appointment Successfully...";
                    } else {
                        message="Unable to save Appointment....";
                        found = false;
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
            pDialog.dismiss();
            if(!message.equals("")){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            if(found) {
                appointmentDetail appointmentdetail = pendingAppointmentLists.get(appointmentposition);
                pendingAppointmentLists.remove(appointmentdetail);
                appointmentAdapter.notifyDataSetChanged();
                appointmentlist.setAdapter(appointmentAdapter);
            }
        }
    }

    class rejectAppointment extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Request is processing.Please wait....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appointmentid", pendingAppointmentLists.get(appointmentposition).getAppointmentId()));
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("starthour", pendingAppointmentLists.get(appointmentposition).getStarthour()));
            params.add(new BasicNameValuePair("startmin", pendingAppointmentLists.get(appointmentposition).getStartmin()));
            params.add(new BasicNameValuePair("type", "reject"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json1 = jsonParser.makeHttpRequest(appointmentpath, "GET", params);

            if(json1!=null) {
                try {
                    int success = json1.getInt("flag");
                    if (success == 1) {
                        found=true;
                        message="Save Appointment Info Successfully...";
                    } else {
                        message="Unable to save Appointment....";
                        found = false;
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
            pDialog.dismiss();
            if(!message.equals("")){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            if(found) {
                appointmentDetail appointmentdetail = pendingAppointmentLists.get(appointmentposition);
                pendingAppointmentLists.remove(appointmentdetail);
                appointmentAdapter.notifyDataSetChanged();
                appointmentlist.setAdapter(appointmentAdapter);
            }
        }
    }
}
