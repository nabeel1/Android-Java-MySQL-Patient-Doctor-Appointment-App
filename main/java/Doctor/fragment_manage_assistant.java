package com.example.shahidhussain.assignemnt2.Doctor;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.shahidhussain.assignemnt2.Patient.fragment_home;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.services.MySingleton;
import com.example.shahidhussain.assignemnt2.services.urlBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class fragment_manage_assistant extends Fragment {
    View rootview;
    String email="";
    String type="";
    private String message="";
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=true;
    private  String path = new dbPath("getPaList").getPath();
    private  String deletepath = new dbPath("deleteAssistant").getPath();
    private ArrayList<String> nameList=new ArrayList<>();
    private ArrayList<String> mobileList=new ArrayList<>();
    private ArrayList<String> cityList=new ArrayList<>();
    private ArrayList<String> emailList=new ArrayList<>();
    private int assistantposition=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_fragment_manage_assistant, container, false);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        email=pref.getString("email", "");
         getPaList();
        Button addassistant=rootview.findViewById(R.id.addassistant);
        addassistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.image_swith_in_rc,R.anim.image_swith_out_cl)
                        .replace(R.id.doctorhomefragment, new fragment_addAssistant(), "findThisFragment0")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return rootview;
    }

    private void getPaList() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Getting Data....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("email", email);
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
                                if (success == 1) {
                                    JSONArray categoryArray = jsonObject.getJSONArray("padetail");
                                    for (int i = 0; i < categoryArray.length(); i++) {
                                        JSONObject c = categoryArray.getJSONObject(i);
                                        nameList.add(c.getString("name"));
                                        cityList.add(c.getString("city"));
                                        emailList.add(c.getString("email"));
                                        mobileList.add(c.getString("mobile"));
                                    }
                                    ListView list=rootview.findViewById(R.id.assisstantlist);
                                    setPaDetailAdapter adapter=new setPaDetailAdapter();
                                    list.setAdapter(adapter);
                                }else{
                                    Toast.makeText(getContext(), "No Assistant Found", Toast.LENGTH_SHORT).show();
                                }
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
                    Toast.makeText(getContext(), "Timeout Error",
                            Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError)
                {
                    Toast.makeText(getContext(), "No Connection",
                            Toast.LENGTH_LONG).show();

                }
                else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), error.toString(),
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
        MySingleton.getmInstance(getContext()).addToRequestQue(stringRequest);
    }
    private void deleteAssistant() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Deleting record....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("email", emailList.get(assistantposition));
        urlBuilder url=new urlBuilder();
        String updatedpath=url.buildURI(deletepath,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, updatedpath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject!=null){
                                int success = jsonObject.getInt("flag");
                                if (success == 1) {
                                    Toast.makeText(getContext(), "Assistant Record delete successfully", Toast.LENGTH_SHORT).show();
                                    ListView list=rootview.findViewById(R.id.assisstantlist);
                                    setPaDetailAdapter adapter=new setPaDetailAdapter();
                                    nameList.remove(assistantposition);
                                    emailList.remove(assistantposition);
                                    cityList.remove(assistantposition);
                                    mobileList.remove(assistantposition);
                                    list.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "Unable to Delete....", Toast.LENGTH_SHORT).show();
                                }
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
                    Toast.makeText(getContext(), "Timeout Error",
                            Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError)
                {
                    Toast.makeText(getContext(), "No Connection",
                            Toast.LENGTH_LONG).show();

                }
                else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), error.toString(),
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
        MySingleton.getmInstance(getContext()).addToRequestQue(stringRequest);
    }
    public class setPaDetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return emailList.size();
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
                        inflate(R.layout.custom_pa, parent, false);
                TextView paname=convertView.findViewById(R.id.paname);
                TextView paemail=convertView.findViewById(R.id.paemail);
                TextView pamobile=convertView.findViewById(R.id.pamobile);
                TextView pacity=convertView.findViewById(R.id.pacity);
                paname.setText(nameList.get(position));
                paemail.setText(emailList.get(position));
                pamobile.setText(mobileList.get(position));
                pacity.setText(cityList.get(position));
                Button editpa=convertView.findViewById(R.id.editpa);
                Button deletepa=convertView.findViewById(R.id.deletepa);
                editpa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences pref = getContext().getSharedPreferences("assistantPref",
                                MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", emailList.get(position));
                        editor.apply();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.image_swith_in_rc,R.anim.image_swith_out_cl)
                                .replace(R.id.doctorhomefragment, new fragment_editAssistant(), "findThisFragment0")
                                .addToBackStack(null)
                                .commit();
                    }
                });
                deletepa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        assistantposition=position;
                        deleteAssistant();
                    }
                });
            }
            return convertView;
        }
    }
}

