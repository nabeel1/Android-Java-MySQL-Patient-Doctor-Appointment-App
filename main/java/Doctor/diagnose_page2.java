package com.example.shahidhussain.assignemnt2.Doctor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.medicationJavaClass;
import com.example.shahidhussain.assignemnt2.classess.scheduleTimingJavaClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class diagnose_page2 extends Fragment{
    //**********************************DB Link******************************
    JSONParser jsonParser = new JSONParser();
    private String message="";
    private boolean result = true;
    private  String storing_medicationdata_dbPath = new dbPath("storing_medicationdata_dbPath").getPath();
    //***********************************************************************
    View rootview;
    CustomListAdapter medicationAdapter;
    ListView medicationlistview;
    ArrayList<medicationJavaClass> medicationlist=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_diagnose_page2, container, false);

        FloatingActionButton btn=rootview.findViewById(R.id.addmedication);

        medicationAdapter = new CustomListAdapter();

        medicationlistview  = rootview.findViewById(R.id.medicationList);

        medicationlist.add(new medicationJavaClass("","","",""));
        medicationlistview.setAdapter(medicationAdapter);
        medicationAdapter.notifyDataSetChanged();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<scheduleTimingJavaClass> timing= new ArrayList<>();
                medicationlist.add(new medicationJavaClass("","","",""));
                medicationlistview.setAdapter(medicationAdapter);
                medicationAdapter.notifyDataSetChanged();
            }
        });
        Button saverecord=rootview.findViewById(R.id.saverecord);
        saverecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new save_medication_data().execute();
            }
        });
        return rootview;
    }
    public class CustomListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return medicationlist.size(); //returns total of items in the list
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
                        inflate(R.layout.custom_medicationlist, parent, false);
                final EditText disease = convertView.findViewById(R.id.disease);
                disease.setText(medicationlist.get(position).getDisease());
                final EditText symptoms = convertView.findViewById(R.id.symptoms);
                symptoms.setText(medicationlist.get(position).getSymptoms());
                final EditText medicine = convertView.findViewById(R.id.medicine);
                medicine.setText(medicationlist.get(position).getMedicines());
                final EditText doze = convertView.findViewById(R.id.doze);
                doze.setText(medicationlist.get(position).getDoze());
                disease.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //here is your code
                        medicationlist.get(position).setDisease(disease.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }
                });
                symptoms.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //here is your code
                        medicationlist.get(position).setSymptoms(symptoms.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }
                });
                medicine.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //here is your code
                        medicationlist.get(position).setMedicines(medicine.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }
                });
                doze.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //here is your code
                        medicationlist.get(position).setDoze(doze.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }
                });
                ImageView delete = convertView.findViewById(R.id.deletemedication);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (medicationlist.size() > 0) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                            adb.setTitle("Delete?");
                            adb.setMessage("Are you sure you want to delete?");
                            final int positionToRemove = position;
                            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    medicationJavaClass medication  = medicationlist.get(position);
                                    medicationlist.remove(medication);
                                    medicationAdapter.notifyDataSetChanged();
                                }
                            });
                            adb.setNegativeButton("Cancel", null);
                            adb.show();
                        }
                    }
                });
                SharedPreferences pref = getContext().getSharedPreferences("detail",
                        MODE_PRIVATE);
                String email = pref.getString("pemail", "");
                String name = pref.getString("pname", "");
                TextView pemail = rootview.findViewById(R.id.pemail);
                TextView pname = rootview.findViewById(R.id.pname);
                pemail.setText(email);
                pname.setText(name);
            }
            return convertView;
        }
    }
    class save_medication_data extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("It Will Take Time.Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            // Building Parameters
            SharedPreferences pref = getContext().getSharedPreferences("detail",
                    MODE_PRIVATE);
            List<NameValuePair> param1 = new ArrayList<>();

            param1.add(new BasicNameValuePair("did", pref.getString("did", "")));
            param1.add(new BasicNameValuePair("pid", pref.getString("pid", "")));
            param1.add(new BasicNameValuePair("weight", pref.getString("weight", "")));
            param1.add(new BasicNameValuePair("height", pref.getString("height", "")));
            param1.add(new BasicNameValuePair("temperature", pref.getString("temperature", "")));
            param1.add(new BasicNameValuePair("age", pref.getString("age", "")));
            param1.add(new BasicNameValuePair("bloodpressure", pref.getString("bloodpressure", "")));
            param1.add(new BasicNameValuePair("appointmentid", pref.getString("appointmentid", "")));
            CheckBox report=rootview.findViewById(R.id.allow);
            if(report.isChecked())
                param1.add(new BasicNameValuePair("allowReport", "y"));
            else
                param1.add(new BasicNameValuePair("allowReport", "n"));
            param1.add(new BasicNameValuePair("size", String.valueOf(medicationlist.size())));
            for (int i = 0; i < medicationlist.size(); i++) {
                param1.add(new BasicNameValuePair("disease" + String.valueOf(i), medicationlist.get(i).getDisease()));
                param1.add(new BasicNameValuePair("medicine" + String.valueOf(i), medicationlist.get(i).getMedicines()));
                param1.add(new BasicNameValuePair("symptoms" + String.valueOf(i), medicationlist.get(i).getSymptoms()));
                param1.add(new BasicNameValuePair("doze" + String.valueOf(i), medicationlist.get(i).getDoze()));
            }
                JSONObject json = jsonParser.makeHttpRequest(storing_medicationdata_dbPath, "GET", param1);
                if (json != null) {
                    try {
                        // Checking data exist befor or not
                        int success = json.getInt("flag");
                        if (success == 1) {
                            result = true;
                            message = "Save Record SuccessFully";
                        } else {
                            result = false;
                            message = json.getString("message");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    message = "No Internet Connection Found! Check Internet Connection...";
                }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            if(result){
                pDialog.dismiss();
                Intent i=new Intent(getActivity(),DoctorDashBoard.class);
                startActivity(i);

            }
        }
    }

}
