
package com.example.shahidhussain.assignemnt2.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.pendingAppointmentList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class fragment_DoctorHome extends Fragment{

    private View view;
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=true;
    ArrayList<pendingAppointmentList> pendingAppointmentLists=new ArrayList<>();
    private  String path = new dbPath("getAppointmentPendingList").getPath();
    private String message="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new fragment_DoctorHome.getCategory().execute();
    }
    class getCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("...Sync Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            SharedPreferences pref = getContext().getSharedPreferences("MyPref",
                    MODE_PRIVATE);
            String id=pref.getString("id", "");
            params.add(new BasicNameValuePair("id", id));
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
                            pendingAppointmentList list=new pendingAppointmentList();
                            list.setId(c.getInt("appointmentid"));
                            list.setDoctorId(String.valueOf(c.getInt("doctorId")));
                            list.setPatientId(c.getString("patientId"));
                            list.setPatientName(c.getString("patientName"));
                            list.setStartmin(c.getString("startmin"));
                            list.setStarthour(c.getString("starthour"));
                            list.setEndmin(c.getString("endmin"));
                            list.setEndhour(c.getString("endhour"));
                            list.setAppointmentNumber(c.getString("appointmentnumber"));
                            pendingAppointmentLists.add(list);
                        }
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
            if(!message.equals(""))
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            if(found) {
                  RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
//                mRecyclerView.setVisibility(View.VISIBLE);
//                LinearLayout statusofappointment=view.findViewById(R.id.statusofappointment);
                //statusofappointment.setVisibility(View.GONE);
                RecyclerView.Adapter mAdapter;
                RecyclerView.LayoutManager mLayoutManager;
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(view.getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                mAdapter = new fragment_DoctorHome.ProgrammingAdapter();
                mRecyclerView.setAdapter(mAdapter);
            }else{
//                RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
//                mRecyclerView.setVisibility(View.GONE);
//                LinearLayout statusofappointment=view.findViewById(R.id.statusofappointment);
                //statusofappointment.setVisibility(View.GONE);
            }
        }
    }
    public class ProgrammingAdapter extends RecyclerView.Adapter<fragment_DoctorHome.ProgrammingAdapter.ViewHolder> {

        public  class ViewHolder extends RecyclerView.ViewHolder {
            TextView pname;
            TextView ptiming;
            TextView ptoken;
            Button diagnosebtn;
            public ViewHolder(View itemView) {
                super(itemView);
                pname= view.findViewById(R.id.pname);
                ptiming=view.findViewById(R.id.ptiming);
                ptoken=view.findViewById(R.id.ptoken);
                diagnosebtn=view.findViewById(R.id.diagnosebtn);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ProgrammingAdapter() {

        }

        // Create new views (invoked by the layout manager)
        @Override
        public fragment_DoctorHome.ProgrammingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            view=inflater.inflate(R.layout.custom_appointment_list,parent,false);
            return new fragment_DoctorHome.ProgrammingAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(fragment_DoctorHome.ProgrammingAdapter.ViewHolder holder, final int position) {
            holder.pname.setText("Name: "+pendingAppointmentLists.get(position).getPatientName());
            String time="Time: "+pendingAppointmentLists.get(position).getStarthour()
                    +":"+pendingAppointmentLists.get(position).getStarthour()
                    +"-"+pendingAppointmentLists.get(position).getEndhour()
                    +":"+pendingAppointmentLists.get(position).getEndmin();
            holder.ptiming.setText(time);
            holder.ptoken.setText("Token# "+pendingAppointmentLists.get(position).getAppointmentNumber());
            holder.diagnosebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getContext().getSharedPreferences("MyPref",
                            MODE_PRIVATE);
                    String email=pref.getString("email", "");
                    SharedPreferences pref1 = getContext().getSharedPreferences("detail",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref1.edit();
                    editor.putString("pid", pendingAppointmentLists.get(position).getPatientId());
                    editor.putString("did", pendingAppointmentLists.get(position).getDoctorId());
                    editor.putString("appointmentid", String.valueOf(pendingAppointmentLists.get(position).getId()));
                    editor.putString("pname", pendingAppointmentLists.get(position).getPatientName());
                    editor.putString("pemail", email);
                    editor.commit();
                    Intent next=new Intent(getContext(),doctor_Diagnose.class);
                    startActivity(next);
                }
            });

        }

        @Override
        public int getItemCount() {
            return pendingAppointmentLists.size();
        }
    }
}
