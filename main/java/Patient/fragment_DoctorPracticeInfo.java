package com.example.shahidhussain.assignemnt2.Patient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shahidhussain.assignemnt2.R;
import com.example.shahidhussain.assignemnt2.classess.doctorJavaClass;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class fragment_DoctorPracticeInfo extends Fragment implements OnMapReadyCallback {
    private View view;
    private int permsRequestCode = 200;
    private GoogleMap mMap;
    doctorJavaClass obj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment__doctor_practice_info, container, false);

        return view;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mPreference", 0);
        if (sharedPreferences.contains("DoctorInfo")) {
            final Gson gson = new Gson();
            obj = gson.fromJson(sharedPreferences.getString("DoctorInfo", ""), doctorJavaClass.class);
            TextView timing=view.findViewById(R.id.timingtxt);
            TextView address=view.findViewById(R.id.addresstxt);
            String time=obj.getStarthour()+":"+obj.getStartmin()+"-"+obj.getEndhour()+":"+obj.getEndmin();
            timing.setText(time);
            address.setText(obj.getHospitaladdress());
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    String[] perms = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

                    requestPermissions(perms, permsRequestCode);
                } else {
                    mMap.setTrafficEnabled(true);
                    mMap.setIndoorEnabled(true);
                    mMap.setBuildingsEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    if (obj!=null) {
                        LatLng sydney = new LatLng(Double.parseDouble(obj.getLatitude()), Double.parseDouble(obj.getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(obj.getHospitalname()).snippet(obj.getName()));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(18).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }

            }
        });
    }
    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                for (int i=0;i<grantResults.length;i++){
                    boolean p = grantResults[i]==PackageManager.PERMISSION_GRANTED;
                }
                break;
        }

    }
}
