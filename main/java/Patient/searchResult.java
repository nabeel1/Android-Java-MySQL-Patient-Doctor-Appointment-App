package com.example.shahidhussain.assignemnt2.Patient;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.security.AccessController.getContext;

public class searchResult extends AppCompatActivity implements LocationListener{
    public ArrayList<doctorJavaClass> doctorlist=new ArrayList<>();
    public String name="";
    private String email="";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    final String[] chooseCategory = new String[1];
    public ArrayList<String> dcategory=new ArrayList<>();
    private boolean found=true;
    private  String path = new dbPath("getDoctorInfo").getPath();
    private  String imagepath = new dbPath().getPath();
    private Bitmap bitmap_image=null;
    doctorJavaClass doctor;
    ImageView fullview;
    private Uri filePath;
    //*******************Variable for getting current latitude and longitude*********************************

    static double PI_RAD = Math.PI / 180.0;
    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    TextView tvLatitude, tvLongitude, tvTime;
    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    double currentlatitude=0.0;
    double currentlongitude=0.0;
    private String message="";

    String searchCategory="",searchPrice="",searchSearchtxt="";
    //*******************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        fullview=findViewById(R.id.fullview);

        //***************First Get Current Latitude and Longitude**********************************

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }

            // get location
            getLocation();

            //**************************************************************************************


            Intent i = getIntent();
            name = i.getStringExtra("name");
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                    MODE_PRIVATE);
            email = pref.getString("email", "");
            LinearLayout filterscreen = findViewById(R.id.filterscreen);
            filterscreen.setVisibility(View.GONE);
            ImageView back = findViewById(R.id.backbtn);
            ImageView filter = findViewById(R.id.filter);
            final TextView title = findViewById(R.id.Doctorcategory);
            title.setText(name);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout filterscreen=findViewById(R.id.filterscreen);
                    filterscreen.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slidedown));
                    if (filterscreen.getVisibility()==View.VISIBLE) {
                        filterscreen.setVisibility(View.GONE);
                        final TextView title=findViewById(R.id.Doctorcategory);
                        title.setText(name);
                    } else {
                        ImageView imageView=findViewById(R.id.fullview);
                        if(imageView.getVisibility()==View.VISIBLE){
                            imageView.setVisibility(View.GONE);
                        }else{
                            Intent back=new Intent(getApplicationContext(),patientDashboard.class);
                            startActivity(back);
                        }
                    }
                }
            });
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout filterscreen=findViewById(R.id.filterscreen);
                    filterscreen.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slideup));
                    filterscreen.setVisibility(View.VISIBLE);
                    title.setText("Filter");
                    fullview.setVisibility(View.GONE);
                }
            });

            getCategory();
            getDoctorlist();
            RecyclerView mRecyclerView = findViewById(R.id.dclist);
            initializeAdapter();
            Spinner sp = findViewById(R.id.search_category);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                    chooseCategory[0] = dcategory.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
        Button search_searchbtn=findViewById(R.id.search_searchbtn);
        search_searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Spinner search_category=findViewById(R.id.search_category);
                 EditText search_price=findViewById(R.id.search_price);
                 EditText search_searchtxt=findViewById(R.id.search_searchtxt);
                 if(search_category.getSelectedItem().toString().length()>0)
                    searchCategory=search_category.getSelectedItem().toString();
                if(search_price.getText().toString().length()>0)
                    searchPrice=search_price.getText().toString();
                if(search_searchtxt.getText().toString().length()>0)
                    searchSearchtxt=search_searchtxt.getText().toString();
                 advanceSearch();
            }
        });
    }
    private void initializeAdapter(){
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        RecyclerView mRecyclerView = findViewById(R.id.dclist);


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RVAdapter adapter = new RVAdapter(new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mPreference", 0);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                final Gson gson = new Gson();
                String serializedObject = gson.toJson(doctorlist.get(position));
                sharedPreferencesEditor.putString("DoctorInfo", serializedObject);
                sharedPreferencesEditor.apply();
                Intent intent=new Intent(getApplicationContext(),patientSearchDoctorResult.class);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        LinearLayout filterscreen=findViewById(R.id.filterscreen);

        if (filterscreen.getVisibility()==View.VISIBLE) {
            filterscreen.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slidedown));
            filterscreen.setVisibility(View.GONE);
            final TextView title=findViewById(R.id.Doctorcategory);
            title.setText(name);
        } else {
            ImageView imageView=findViewById(R.id.fullview);
            if(imageView.getVisibility()==View.VISIBLE){
                imageView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.zoomout));
                imageView.setVisibility(View.GONE);
            }else{
                Intent back=new Intent(getApplicationContext(),patientDashboard.class);
                startActivity(back);
            }
        }
    }
    private void getDoctorlist() {
        pDialog = new ProgressDialog(searchResult.this);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("email",email);
        params.put("type","category");
        params.put("input",name);
        params.put("request","search");
        urlBuilder url=new urlBuilder();
        path=url.buildURI(path,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject!=null){
                                    int success = jsonObject.getInt("flag");
                                    if (success == 1) {
                                        JSONArray categoryArray = jsonObject.getJSONArray("doctorlist");
                                        int length = categoryArray.length();
                                        for (int i = 0; i < length; i++) {
                                            JSONObject c = categoryArray.getJSONObject(i);
                                            String id = String.valueOf(c.getInt("id"));
                                            String name = c.getString("name");
                                            String email = c.getString("email");
                                            String city = c.getString("city");
                                            String num = c.getString("num");
                                            String hospitalname = c.getString("hospitalname");
                                            String starthour = c.getString("starthour");
                                            String startmin = c.getString("startmin");
                                            String endhour = c.getString("endhour");
                                            String endmin = c.getString("endmin");
                                            String timedifference = String.valueOf(c.getString("timedifference"));
                                            String totalrate = c.getString("totalrate");
                                            String getrate = String.valueOf(c.getString("getrate"));
                                            String latitude = c.getString("latitude");
                                            String longitude = c.getString("longitude");
                                            String fee = String.valueOf(c.getInt("fee"));
                                            String experienceYear = c.getString("experienceYear");
                                            String about = c.getString("about");
                                            String specialization = c.getString("specialization");
                                            String qualification = c.getString("qualification");
                                            String experience = c.getString("experience");
                                            String hospitaladdress = c.getString("hospitaladdress");
                                            String loadImagePath = c.getString("imageurl");
                                            double distance = greatCircleInKilometers(currentlatitude, currentlongitude, Double.parseDouble(latitude), Double.parseDouble(longitude));
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            doctorJavaClass doctor = new doctorJavaClass(id, name, email, city, num, hospitalname, starthour, startmin, endhour, endmin, timedifference, totalrate, getrate, latitude, longitude, fee, experienceYear, distance, about, specialization, qualification, experience, hospitaladdress,loadImagePath);
                                            doctorlist.add(doctor);
                                        }
                                        pDialog.dismiss();
                                        initializeAdapter();
                                    } else {
                                        found = false;
                                        Toast.makeText(getApplicationContext(),"No Result Found",Toast.LENGTH_LONG).show();
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

    private void advanceSearch() {
        doctorlist.clear();
        pDialog = new ProgressDialog(searchResult.this);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Map<String,String> params=new HashMap<>();
        params.put("email",email);
        params.put("type","filtersearch");
        params.put("request","search");
        if(!searchCategory.equals(""))
            params.put("searchCategory",searchCategory);
        if(!searchPrice.equals(""))
            params.put("searchPrice",searchPrice);
        if(!searchSearchtxt.equals(""))
            params.put("searchSearchtxt",searchSearchtxt);
        urlBuilder url=new urlBuilder();
        path=url.buildURI(path,params);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject!=null){
                                int success = jsonObject.getInt("flag");
                                if (success == 1) {
                                    JSONArray categoryArray = jsonObject.getJSONArray("doctorlist");
                                    int length = categoryArray.length();
                                    for (int i = 0; i < length; i++) {
                                        JSONObject c = categoryArray.getJSONObject(i);
                                        String id = String.valueOf(c.getInt("id"));
                                        String name = c.getString("name");
                                        String email = c.getString("email");
                                        String city = c.getString("city");
                                        String num = c.getString("num");
                                        String hospitalname = c.getString("hospitalname");
                                        String starthour = c.getString("starthour");
                                        String startmin = c.getString("startmin");
                                        String endhour = c.getString("endhour");
                                        String endmin = c.getString("endmin");
                                        String timedifference = String.valueOf(c.getString("timedifference"));
                                        String totalrate = c.getString("totalrate");
                                        String getrate = String.valueOf(c.getString("getrate"));
                                        String latitude = c.getString("latitude");
                                        String longitude = c.getString("longitude");
                                        String fee = String.valueOf(c.getInt("fee"));
                                        String experienceYear = c.getString("experienceYear");
                                        String about = c.getString("about");
                                        String specialization = c.getString("specialization");
                                        String qualification = c.getString("qualification");
                                        String experience = c.getString("experience");
                                        String hospitaladdress = c.getString("hospitaladdress");
                                        String loadImagePath = c.getString("imageurl");
                                        double distance = greatCircleInKilometers(currentlatitude, currentlongitude, Double.parseDouble(latitude), Double.parseDouble(longitude));
                                        distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                        doctorJavaClass doctor = new doctorJavaClass(id, name, email, city, num, hospitalname, starthour, startmin, endhour, endmin, timedifference, totalrate, getrate, latitude, longitude, fee, experienceYear, distance, about, specialization, qualification, experience, hospitaladdress,loadImagePath);
                                        doctorlist.add(doctor);
                                    }
                                    pDialog.dismiss();
                                    LinearLayout filterscreen = findViewById(R.id.filterscreen);
                                    filterscreen.setVisibility(View.GONE);
                                    initializeAdapter();

                                } else {
                                    found = false;
                                    Toast.makeText(getApplicationContext(),"No Result Found",Toast.LENGTH_LONG).show();
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
    private void getCategory() {
        pDialog = new ProgressDialog(searchResult.this);
        Map<String,String> params=new HashMap<>();
        params.put("request","getcategory");
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
                                int success=jsonObject.getInt("success");
                                if(success==1){
                                    JSONArray categoryArray= jsonObject.getJSONArray("category");
                                    for (int i = 0; i < categoryArray.length(); i++) {
                                        JSONObject c = categoryArray.getJSONObject(i);
                                        String category = c.getString("category");
                                        dcategory.add(category);
                                    }
                                    chooseCategory[0] = dcategory.get(0);
                                    Spinner sp=findViewById(R.id.search_category);
                                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.text, dcategory);
                                    adp1.setDropDownViewResource(R.layout.italictxt);
                                    sp.setAdapter(adp1);
                                }else{
                                    Toast.makeText(searchResult.this, "Category List is Empty", Toast.LENGTH_SHORT).show();
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

    public void zoom(View view){
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide);
        view.startAnimation(animation1);
    }

    Bitmap bitmapImage;
    private Bitmap getImage(String loadImagePath){
        Picasso.with(searchResult.this)
                .load(loadImagePath)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmapImage=bitmapImage;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        return bitmapImage;
    }
    View view;
    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

        CustomItemClickListener listener;

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imge;
            TextView name;
            TextView doctoremail;
            TextView experience;
            TextView fee;
            TextView ratetxt;
            TextView distance;
            RatingBar rate;

            ViewHolder(View itemView) {
                super(itemView);
                imge= view.findViewById(R.id.doctorPic);
                name= view.findViewById(R.id.doctorName);
                doctoremail= view.findViewById(R.id.doctoremail);
                experience= view.findViewById(R.id.experienceYear);
                fee= view.findViewById(R.id.doctorPrice);
                rate= view.findViewById(R.id.doctorRating);
                ratetxt=view.findViewById(R.id.ratetxt);
                distance=view.findViewById(R.id.distance);
            }
        }
        RVAdapter(CustomItemClickListener listener){
            this.listener = listener;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
            view=inflater.inflate(R.layout.custom_doctor_list,viewGroup,false);
            final ViewHolder pvh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pvh.getPosition());
                }
            });
            return pvh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.name.setText(doctorlist.get(position).getName());
            holder.imge.setImageResource(R.drawable.doctor1);
            holder.doctoremail.setText(doctorlist.get(position).getEmail());
            holder.rate.setNumStars(Integer.parseInt(doctorlist.get(position).getTotalrate()));
            holder.rate.setRating(Float.parseFloat(doctorlist.get(position).getGetrate()));
            holder.ratetxt.setText(doctorlist.get(position).getGetrate());
            holder.fee.setText(doctorlist.get(position).getFee());
            holder.experience.setText(doctorlist.get(position).getExperienceYear());
            holder.distance.setText(doctorlist.get(position).getDistance());
            if(doctorlist.get(position).getImageName().length()>0 && !doctorlist.get(position).getImageName().equals("")){
                String newPath=imagepath+doctorlist.get(position).getImageName();
                Picasso.with(getApplicationContext())
                        .load(newPath)
                        .into(holder.imge, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                holder.imge.setImageResource(R.drawable.doctor1);
                            }
                        });
            }else{
                Picasso.with(getApplicationContext()).load(R.drawable.doctor1).into(holder.imge);
            }
            holder.imge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullview.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.zoomin));
                    fullview.setVisibility(View.VISIBLE);
                    if(doctorlist.get(position).getImageName().length()>0 && !doctorlist.get(position).getImageName().equals("")){
                        String newPath=imagepath+doctorlist.get(position).getImageName();
                        Picasso.with(getApplicationContext())
                                .load(newPath)
                                .into(fullview, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        fullview.setImageResource(R.drawable.doctor1);
                                    }
                                });
                    }else{
                        fullview.setImageResource(R.drawable.doctor1);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return doctorlist.size();
        }
    }

    /**
     * Use Great Circle distance formula to calculate distance between 2 coordinates in meters.
     */
    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }


    //******************Functions For getting current latitude and longitude *******************************************

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(searchResult.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {
        currentlatitude=loc.getLatitude();
        currentlongitude=loc.getLongitude();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates((LocationListener) this);
        }
    }

}
