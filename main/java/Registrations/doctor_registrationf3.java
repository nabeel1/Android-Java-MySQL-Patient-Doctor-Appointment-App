package com.example.shahidhussain.assignemnt2.Registrations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import static android.app.Activity.RESULT_OK;


public class doctor_registrationf3 extends Fragment {

    public String path="";
    private Uri url;
    private Bitmap mybitmap;
    doctorJavaClass obj;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_doctor_registrationf3, container, false);
        return view;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView upload=view.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 7);
            }
        });
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {
                    ImageView upload = view.findViewById(R.id.image1);
                    url = data.getData();//Image URL
                    InputStream is = null;
                    try {
                        /*Code to View Image*/
                        is = getActivity().getContentResolver().openInputStream(url);
                        mybitmap = BitmapFactory.decodeStream(is);
                        if(mybitmap!=null) {
                            upload.setImageBitmap(mybitmap);
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("registrationPref", 0);
                            if (sharedPreferences.contains("registrationInfo")) {
                                final Gson gson = new Gson();
                                obj = gson.fromJson(sharedPreferences.getString("registrationInfo", ""), doctorJavaClass.class);
                                obj.setImage(mybitmap);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                String serializedObject = gson.toJson(obj);
                                sharedPreferencesEditor.putString("registrationInfo", serializedObject);
                                sharedPreferencesEditor.apply();
                            }
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
