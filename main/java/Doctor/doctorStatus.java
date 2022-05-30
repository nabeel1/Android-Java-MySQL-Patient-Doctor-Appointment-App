package com.example.shahidhussain.assignemnt2.Doctor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shahidhussain.assignemnt2.Login.main;
import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.Registrations.additionalRegistration;
import com.example.shahidhussain.assignemnt2.Registrations.editdoctorRegistration;

public class doctorStatus extends AppCompatActivity {

    private String status1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_status);
        final Intent i=getIntent();
        status1=i.getStringExtra("status");
        final Button next=(Button) findViewById(R.id.next);
        next.setVisibility(View.GONE);
        TextView warning=findViewById(R.id.warning);
        TextView setname=findViewById(R.id.title);
        setname.setText("Doctor Registration Status");
        EditText status=findViewById(R.id.status);
        warning.setVisibility(View.GONE);
        status.setText(status1);
        final String stats=status.getText().toString();
        if(stats.equals("Rejected")){
            warning.setTextColor(Color.parseColor("#f00919"));
            warning.setText("Message: Your Request For Registration is being Rejected because of Wrong Information. Kindly Provide Correct Information.");
            warning.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            next.setText("Edit Information");
        }
        else if(stats.equals("Pending")){
            warning.setTextColor(Color.parseColor("#0cf0c2"));
            warning.setText("Note: Your Request is Forwarded. Please Wait...");
            next.setVisibility(View.GONE);
            warning.setVisibility(View.VISIBLE);
        }else if(stats.equals("Approved")){
            warning.setTextColor(Color.parseColor("#0cf0c2"));
            warning.setText("Congratulation! Your Request is accepted. Please Click on Button to proceed further");
            warning.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            next.setText("Proceed To Next");
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stats.equals("Approved")) {
                    Intent next = new Intent(getApplicationContext(), additionalRegistration.class);
                    startActivity(next);
                }else if(stats.equals("Rejected")){
                    String email=i.getStringExtra("email");
                    Intent edit=new Intent(getApplicationContext(),editdoctorRegistration.class);
                    edit.putExtra("email",email);
                    startActivity(edit);
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(getApplicationContext(),main.class);
        startActivity(intent);
    }
}
