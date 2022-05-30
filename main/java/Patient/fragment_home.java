package com.example.shahidhussain.assignemnt2.Patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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


public class fragment_home extends Fragment {

    private View view;
    public int dimg[]={R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4,R.drawable.c5,R.drawable.c6,R.drawable.c7,R.drawable.c8,R.drawable.c9,R.drawable.c10,R.drawable.c11,R.drawable.c12,R.drawable.c13,R.drawable.c14};
    public ArrayList<String> dcategory=new ArrayList<>();
    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private boolean found=true;

    private  String path = new dbPath("getDoctorInfo").getPath();
    private String message="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        getCategory();
        return view;
    }

    private void getCategory() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Fetching Category");
        pDialog.setCancelable(true);
        pDialog.show();
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
                                    JSONArray categoryArray = jsonObject.getJSONArray("category");
                                    for (int i = 0; i < categoryArray.length(); i++) {
                                        JSONObject c = categoryArray.getJSONObject(i);
                                        String category = c.getString("category");
                                        dcategory.add(category);
                                    }
                                    RecyclerView mRecyclerView;
                                    RecyclerView.Adapter mAdapter;
                                    RecyclerView.LayoutManager mLayoutManager;

                                    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


                                    mRecyclerView.setHasFixedSize(true);
                                    mLayoutManager = new LinearLayoutManager(view.getContext());
                                    mRecyclerView.setLayoutManager(mLayoutManager);

                                    // specify an adapter (see also next example)
                                    mAdapter= new ProgrammingAdapter();
                                    mRecyclerView.setAdapter(mAdapter);
                                }else{
                                    Toast.makeText(getContext(), "Category List is Empty", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "No Internet Connection Found! Check Internet Connection...", Toast.LENGTH_SHORT).show();
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
    public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ViewHolder> {

        public  class ViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView categoryImage;
            public ViewHolder(View itemView) {
                super(itemView);
                categoryName= view.findViewById(R.id.categoryName);
                categoryImage=view.findViewById(R.id.categoryImage);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ProgrammingAdapter() {

        }

        // Create new views (invoked by the layout manager)
        @Override
        public ProgrammingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            view=inflater.inflate(R.layout.custom_category_list,parent,false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.categoryName.setText(dcategory.get(position));
            holder.categoryImage.setImageResource(dimg[position]);
            holder.categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent next=new Intent(getContext(),searchResult.class);
                    next.putExtra("name",dcategory.get(position));
                    startActivity(next);
                }
            });

        }

        @Override
        public int getItemCount() {
            return dcategory.size();
        }
    }
}
