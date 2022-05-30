package com.example.shahidhussain.assignemnt2.Doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shahidhussain.assignemnt2.DataBase.JSONParser;
import com.example.shahidhussain.assignemnt2.DataBase.dbPath;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.Registrations.editdoctorRegistration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class fragment_editAssistant extends Fragment {

    private Uri url;
    private Bitmap mybitmap=null;
    private View rootview;
    boolean isEmailValid=false;

    JSONParser jsonParser = new JSONParser();
    private String message="";
    private boolean res = false;
    private boolean result = false;
    private String imageURL="";
    private  String imagepath = new dbPath().getPath();
    private  String path = new dbPath("editDoctorPA").getPath();
    private  String getpath = new dbPath("getDoctorPADetail").getPath();
    String name1,email1,mobile1,cnic1,password1,city1,address1;
    final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
    private String email="";
    private ProgressDialog pDialog;
    private Bitmap getImage(String path) {
        if (!path.equals(null) && !path.equals("") && !path.equals("null") ) {
            String add = path;
            try {
                try {
                    URL url = new URL(add);
                    return Glide.
                            with(this).
                            load(path).
                            asBitmap().
                            into(100, 100). // Width and height
                            get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_fragment_edit_assistant, container, false);
        return rootview;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences pref = getContext().getSharedPreferences("assistantPref",
                MODE_PRIVATE);
        email=pref.getString("email", "");
        new getDetail().execute();
        final ImageView upload=rootview.findViewById(R.id.uploadimage);
        final EditText name=rootview.findViewById(R.id.name);
        final EditText email=rootview.findViewById(R.id.email);
        final EditText mobile=rootview.findViewById(R.id.mobile);
        final EditText cnic=rootview.findViewById(R.id.cnic);
        final EditText address=rootview.findViewById(R.id.address);
        final EditText password=rootview.findViewById(R.id.password);
        final EditText city=rootview.findViewById(R.id.city);
        final ImageView deleteimage = rootview.findViewById(R.id.deleteimage);


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                final String emailstring = email.getText().toString().trim();
                if (emailstring.matches(emailPattern) && s.length() > 0){
                    email.setError(null);
                    isEmailValid=true;
                }
                else
                {
                    isEmailValid=false;
                    int errorColor;
                    final int version = Build.VERSION.SDK_INT;

                    //Get the defined errorColor from color resource.
                    if (version >= 23) {
                        errorColor = ContextCompat.getColor(getContext(), R.color.errorColor);
                    } else {
                        errorColor = getResources().getColor(R.color.errorColor);
                    }

                    String errorString = "Invalid Email";  // Your custom error message.
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
                    email.setError(spannableStringBuilder);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 7);
            }
        });
        deleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mybitmap=null;
                ImageView uploadimage = rootview.findViewById(R.id.paimage);
                uploadimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.noimage));
                deleteimage.setVisibility(View.GONE);
                upload.setVisibility(View.VISIBLE);
            }
        });
        Button savebtn=rootview.findViewById(R.id.update);
        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(!name.getText().equals("") && name.getText().toString().length()>0){
                    name.setError(null);
                    name1=name.getText().toString();
                    email1=email.getText().toString();
                    mobile1=mobile.getText().toString();
                    cnic1=cnic.getText().toString();
                    password1=password.getText().toString();
                    city1=city.getText().toString();
                    address1=address.getText().toString();
                    if(mybitmap==null){
                        mybitmap= BitmapFactory.decodeResource(getResources(),
                                R.drawable.noimage);
                    }
                    Bitmap bitmap =mybitmap;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream); //compress to which format you want.
                    byte [] byte_arr = stream.toByteArray();
                    String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
                    nameValuePairs.add(new BasicNameValuePair("image",image_str));
                    nameValuePairs.add(new BasicNameValuePair("name",imageURL));
                    new Update().execute();
                }else{
                    int errorColor;
                    final int version = Build.VERSION.SDK_INT;

                    //Get the defined errorColor from color resource.
                    if (version >= 23) {
                        errorColor = ContextCompat.getColor(getContext(), R.color.errorColor);
                    } else {
                        errorColor = getResources().getColor(R.color.errorColor);
                    }

                    String errorString = "name is required";  // Your custom error message.
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
                    name.setError(spannableStringBuilder);
                }
                if(!email.getText().equals("") && email.getText().toString().length()>0  && isEmailValid){
                    email.setError(null);
                }else{
                    int errorColor;
                    final int version = Build.VERSION.SDK_INT;

                    //Get the defined errorColor from color resource.
                    if (version >= 23) {
                        errorColor = ContextCompat.getColor(getContext(), R.color.errorColor);
                    } else {
                        errorColor = getResources().getColor(R.color.errorColor);
                    }

                    String errorString = "Password is required";  // Your custom error message.
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
                    email.setError(spannableStringBuilder);
                }
                if(!password.getText().equals("") && password.getText().toString().length()>0 ){
                    password.setError(null);
                }else{
                    int errorColor;
                    final int version = Build.VERSION.SDK_INT;

                    //Get the defined errorColor from color resource.
                    if (version >= 23) {
                        errorColor = ContextCompat.getColor(getContext(), R.color.errorColor);
                    } else {
                        errorColor = getResources().getColor(R.color.errorColor);
                    }

                    String errorString = "Password is required";  // Your custom error message.
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
                    spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
                    password.setError(spannableStringBuilder);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {
                    ImageView upload = rootview.findViewById(R.id.paimage);
                    url = data.getData();
                    InputStream is = null;
                    try {
                        is = getActivity().getContentResolver().openInputStream(url);
                        mybitmap = BitmapFactory.decodeStream(is);
                        if(mybitmap!=null) {
                            upload.setImageBitmap(mybitmap);
                            ImageView uploadimage = rootview.findViewById(R.id.uploadimage);
                            ImageView deleteimage = rootview.findViewById(R.id.deleteimage);
                            uploadimage.setVisibility(View.GONE);
                            deleteimage.setVisibility(View.VISIBLE);
                        }
                        is.close();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Fail to Upload Image", Toast.LENGTH_LONG).show();
                    }
                    break;

                }
        }
    }
    class Update extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Updating Record");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected Void doInBackground(Void... args) {
            // Building Parameters
            List<NameValuePair> param1 = new ArrayList<>();

            param1.add(new BasicNameValuePair("name", name1));
            param1.add(new BasicNameValuePair("email", email1));
            param1.add(new BasicNameValuePair("oldemail", email));
            param1.add(new BasicNameValuePair("mobile", mobile1));
            param1.add(new BasicNameValuePair("cnic", cnic1));
            param1.add(new BasicNameValuePair("password", password1));
            param1.add(new BasicNameValuePair("city", city1));
            param1.add(new BasicNameValuePair("address", address1));
            param1.add(new BasicNameValuePair("imageURL", imageURL));
            JSONObject json = jsonParser.makeHttpRequest(path, "GET", param1);
            if(json!=null) {
                try {
                    // Checking data exist befor or not
                    int success = json.getInt("flag");
                    if (success == 1) {
                        res = false;
                        try{
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("https://phealthcarecom.000webhostapp.com/FYP/upload_image.php");
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            HttpResponse response = httpclient.execute(httppost);
                            final String the_string_response = convertResponseToString(response);

                        }catch(final Exception e){

                        }
                        message="Save Record SuccessFully";
                    } else {
                        res = true;
                        message = json.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                message = "No Internet Connection Found! Check Internet Connection...";
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            if(!res){
                pDialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.image_swith_in_rc,R.anim.image_swith_out_cl)
                        .replace(R.id.doctorhomefragment, new fragment_manage_assistant(), "findThisFragment0")
                        .addToBackStack(null)
                        .commit();

            }
        }
    }
    class getDetail extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(getContext());
            pDialog1.setMessage("Fetching Data..");
            pDialog1.setIndeterminate(false);
            pDialog1.setCancelable(true);
            pDialog1.show();
        }
        protected Void doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            JSONObject json1 = jsonParser.makeHttpRequest(getpath, "GET", params);
            if(json1!=null){
                try {
                    int flag = json1.getInt("flag");
                    if (flag == 1) {
                        result=true;
                        name1 = json1.getString("name");
                        email1 = json1.getString("email");
                        mobile1 = json1.getString("mobile");
                        cnic1 = json1.getString("cnic");
                        password1 = json1.getString("password");
                        city1 = json1.getString("city");
                        address1 = json1.getString("address");
                        imageURL = imagepath+json1.getString("image");
                    }else{
                        result=false;
                        message="No Record Found";
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                message = "No Internet Connection Found! Check Internet Connection...";
            }
            return null;
        }
        protected void onPostExecute(Void file_url) {
            pDialog1.dismiss();
            if(result){
                final EditText name=rootview.findViewById(R.id.name);
                final EditText email=rootview.findViewById(R.id.email);
                final EditText mobile=rootview.findViewById(R.id.mobile);
                final EditText cnic=rootview.findViewById(R.id.cnic);
                final EditText address=rootview.findViewById(R.id.address);
                final EditText password=rootview.findViewById(R.id.password);
                final EditText city=rootview.findViewById(R.id.city);
                name.setText(name1);
                email.setText(email1);
                mobile.setText(mobile1);
                cnic.setText(cnic1);
                address.setText(address1);
                password.setText(password1);
                city.setText(city1);
                final ImageView uploadimage = rootview.findViewById(R.id.paimage);
                Picasso.with(getContext())
                        .load(imageURL)
                        .into(uploadimage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                uploadimage.setImageResource(R.drawable.doctor1);
                            }
                        });

            }else{
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private InputStream inputStream;

    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException {

        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..


        if (contentLength < 0){
        }
        else{
            byte[] data = new byte[512];
            int len = 0;
            try
            {
                while (-1 != (len = inputStream.read(data)) )
                {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                inputStream.close(); // closing the stream…..
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to string…..

            //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }
}
