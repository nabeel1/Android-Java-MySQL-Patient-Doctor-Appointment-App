package com.example.shahidhussain.assignemnt2.Doctor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.shahidhussain.assignemnt2.Login.main;
import com.example.shahidhussain.assignemnt2.R;

public class doctor_Diagnose extends AppCompatActivity {
String email="";
String name="";
String pid="";
String did="";
Fragment fragment;
    private int position=0;
    private FragmentTransaction ft;

    private void replacefragment(Fragment fragement){
        FragmentManager fm = getSupportFragmentManager();
        ft=fm.beginTransaction();
            ft.setCustomAnimations(R.anim.image_swith_in_rc,R.anim.image_swith_out_cl);
        ft.replace(R.id.diagnosefragment, fragement);
        ft.addToBackStack(null);
        this.fragment=fragement;
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__diagnose);
        replacefragment(new diagnose_page1());

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.homedefaultfragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}
