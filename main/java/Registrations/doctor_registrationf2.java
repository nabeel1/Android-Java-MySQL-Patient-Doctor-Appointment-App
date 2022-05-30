package com.example.shahidhussain.assignemnt2.Registrations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.google.gson.Gson;

public class doctor_registrationf2 extends Fragment {

    View view;
    doctorJavaClass obj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_doctor_registrationf2, container, false);
        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("registrationPref", 0);
        if (sharedPreferences.contains("registrationInfo")) {
            final Gson gson = new Gson();
            obj = gson.fromJson(sharedPreferences.getString("registrationInfo", ""), doctorJavaClass.class);
            final EditText workexperience=view.findViewById(R.id.workexperience);
            final EditText aboutme=view.findViewById(R.id.aboutme);
            final EditText Specialization=view.findViewById(R.id.Specialization);
            final EditText qualification=view.findViewById(R.id.qualification);
            final EditText experienceYear=view.findViewById(R.id.experienceYear);
            workexperience.setText(obj.getExperience());
            aboutme.setText(obj.getAbout());
            Specialization.setText(obj.getSpecialization());
            qualification.setText(obj.getQualitfication());
            experienceYear.setText(obj.getExperienceYear());
        }
    }
}
