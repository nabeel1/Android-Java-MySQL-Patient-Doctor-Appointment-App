package com.example.shahidhussain.assignemnt2.Doctor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.R;

import static android.content.Context.MODE_PRIVATE;

public class diagnose_page1 extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_diagnose_page1, container, false);
        Button nextbtn=view.findViewById(R.id.nextfragment);
        TextView pemail=view.findViewById(R.id.pemail);
        TextView pname=view.findViewById(R.id.pname);
        final TextView bloodpressure=view.findViewById(R.id.bloodpressure);
        final TextView temperature=view.findViewById(R.id.temperature);
        final TextView age=view.findViewById(R.id.age);
        final TextView height=view.findViewById(R.id.height);
        final TextView weight=view.findViewById(R.id.weight);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bloodpressure.getText().length()==0 || temperature.getText().length()==0 ||
                        age.getText().length()==0 ){
                    Toast.makeText(getContext(), "Fill Required Fields", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences pref = getContext().getSharedPreferences("detail",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("bloodpressure", bloodpressure.getText().toString());
                    editor.putString("temperature", temperature.getText().toString());
                    editor.putString("age", age.getText().toString());
                    editor.putString("height", height.getText().toString());
                    editor.putString("weight", weight.getText().toString());
                    editor.commit();
                    Bundle bundle = new Bundle();
                    diagnose_page2 nextFrag = new diagnose_page2();
                    nextFrag.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.image_swith_in_rc,R.anim.image_swith_out_cl)
                            .replace(R.id.diagnosefragment, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        SharedPreferences pref = getContext().getSharedPreferences("detail",
                MODE_PRIVATE);
        String email=pref.getString("pemail", "");
        String name=pref.getString("pname", "");
        String appointmentid=pref.getString("appointmentid", "");
        pemail.setText(email);
        pname.setText(name);
        return view;

    }

}
