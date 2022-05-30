package com.example.shahidhussain.assignemnt2.Patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.shahidhussain.assignemnt2.Doctor.fragment_DoctorHome;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.Registrations.doctorRegistration;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.example.shahidhussain.assignemnt2.classess.pendingAppointmentList;
import com.example.shahidhussain.assignemnt2.services.MySingleton;
import com.example.shahidhussain.assignemnt2.services.urlBuilder;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class fragment_report extends Fragment {

    private View view;
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=true;
    ArrayList<pendingAppointmentList> pendingAppointmentLists=new ArrayList<>();
    private  String path = new dbPath("patientReports").getPath();
    private  String submitReportpath = new dbPath("submitPatientReports").getPath();
    private String message="";
    private Uri url;
    private int diagnoseId=-1;
    private Bitmap mybitmap=null;
    private String Image_name="";
    private ArrayList<String> Images=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_report, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getReport();
        Button selectReport=view.findViewById(R.id.selectReport);
        Button uploadReport=view.findViewById(R.id.uploadReport);
        final ImageView removeicon = view.findViewById(R.id.removeicon);
        removeicon.setVisibility(View.GONE);
        uploadReport.setEnabled(false);
        selectReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 7);
            }
        });
        uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mybitmap!=null){
                    long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                     Image_name="Reports/"+String.valueOf(number)+".png";
                     submitReport();
                    new uploadReport().execute();
                }else{
                    Toast.makeText(getContext(), "Select Valid Image First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                ImageView upload = view.findViewById(R.id.reportImage);
                upload.setImageDrawable(getResources().getDrawable(R.drawable.noimage));
                removeicon.setVisibility(View.GONE);
                mybitmap=null;
            }
        });
    }


    private void getReport() {

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Status...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        found=false;
        Map<String,String> params=new HashMap<>();
        SharedPreferences pref = getContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        String email=pref.getString("email", "");
        params.put("email",email);
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
                                    found=true;
                                    diagnoseId = jsonObject.getInt("diagnoseId");
                                    JSONArray ImagesURLArray = jsonObject.getJSONArray("ImagesURL");
                                    for (int i = 0; i < ImagesURLArray.length(); i++) {
                                        JSONObject ImagesURL = ImagesURLArray.getJSONObject(i);
                                        Images.add(ImagesURL.getString("image"));
                                    }
                                    LinearLayout noappointmentmessage=view.findViewById(R.id.noappointmentmessage);
                                    LinearLayout reportLayout=view.findViewById(R.id.reportLayout);
                                    noappointmentmessage.setVisibility(View.GONE);
                                    reportLayout.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(getContext(), "No Report Found", Toast.LENGTH_SHORT).show();
                                    LinearLayout noappointmentmessage=view.findViewById(R.id.noappointmentmessage);
                                    LinearLayout reportLayout=view.findViewById(R.id.reportLayout);
                                    noappointmentmessage.setVisibility(View.VISIBLE);
                                    reportLayout.setVisibility(View.GONE);
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

    private void submitReport() {
        Map<String,String> params=new HashMap<>();
        params.put("diagnoseId",String.valueOf(diagnoseId));
        params.put("imageurl",Image_name);
        urlBuilder url=new urlBuilder();
        String updatedpath=url.buildURI(submitReportpath,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, updatedpath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject!=null){
                                int success = jsonObject.getInt("flag");
                                if (success != 1) {
                                    Toast.makeText(getContext(), "Fail to Submit Report",
                                            Toast.LENGTH_LONG).show();
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
    class uploadReport extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Submitting your Reports.......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            try{
                final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mybitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.
                byte [] byte_arr = stream.toByteArray();
                String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
                nameValuePairs.add(new BasicNameValuePair("image",image_str));
                nameValuePairs.add(new BasicNameValuePair("name",Image_name));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://phealthcarecom.000webhostapp.com/FYP/upload_image.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
            }catch(final Exception e){
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            pDialog.dismiss();
            Intent i=new Intent(getContext(),patientDashboard.class);
            startActivity(i);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {
                    ImageView upload = view.findViewById(R.id.reportImage);
                    url = data.getData();//Image URL
                    InputStream is = null;
                    try {
                        /*Code to View Image*/
                        is = getActivity().getContentResolver().openInputStream(url);
                        mybitmap = BitmapFactory.decodeStream(is);
                        if(mybitmap!=null) {
                            Button uploadReport=view.findViewById(R.id.uploadReport);
                            ImageView removeicon=view.findViewById(R.id.removeicon);
                            removeicon.setVisibility(View.VISIBLE);
                            uploadReport.setEnabled(true);
                            upload.setImageBitmap(mybitmap);
                        }else{

                            Button uploadReport=view.findViewById(R.id.uploadReport);
                            uploadReport.setEnabled(false);
                            ImageView removeicon=view.findViewById(R.id.removeicon);
                            removeicon.setVisibility(View.GONE);
                        }
                        is.close();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Fail to Upload Image", Toast.LENGTH_LONG).show();
                    }
                    break;

                }
        }
    }

}
