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


public class doctor_registrationf1 extends Fragment {

    View view;
    doctorJavaClass obj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_doctor_registrationf1, container, false);
        return view;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("registrationPref", 0);
        if (sharedPreferences.contains("registrationInfo")) {
            final Gson gson = new Gson();
            obj = gson.fromJson(sharedPreferences.getString("registrationInfo", ""), doctorJavaClass.class);
            final EditText name1=view.findViewById(R.id.name);
            final EditText email1=view.findViewById(R.id.email);
            final EditText password1=view.findViewById(R.id.password);
            final EditText number1=view.findViewById(R.id.number);
            final EditText hospital1=view.findViewById(R.id.hospital);
            final EditText city1=view.findViewById(R.id.city);
            final EditText pmdc1=view.findViewById(R.id.pmdc);
            final EditText fee1=view.findViewById(R.id.fee);
            name1.setText(obj.getName());
            email1.setText(obj.getEmail());
            password1.setText(obj.getPassword());
            number1.setText(obj.getNum());
            hospital1.setText(obj.getHospitalname());
            city1.setText(obj.getCity());
            pmdc1.setText(obj.getPmdc());
            fee1.setText(obj.getFee());
        }
    }
}
