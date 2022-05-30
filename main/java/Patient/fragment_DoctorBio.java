package com.example.shahidhussain.assignemnt2.Patient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

public class fragment_DoctorBio extends Fragment {

    doctorJavaClass obj;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_doctor_bio, container, false);
        return view;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mPreference", 0);
        if (sharedPreferences.contains("DoctorInfo")) {
            final Gson gson = new Gson();
            obj = gson.fromJson(sharedPreferences.getString("DoctorInfo", ""), doctorJavaClass.class);
            TextView about=view.findViewById(R.id.abouttxt);
            TextView qual=view.findViewById(R.id.qualificaiontxt);
            TextView spec=view.findViewById(R.id.specializationtxt);
            TextView year=view.findViewById(R.id.experieceyeartxt);
            about.setText(obj.getAbout());
            qual.setText(obj.getQualitfication());
            spec.setText(obj.getSpecialization());
            year.setText(obj.getExperienceYear());
        }
    }
}
