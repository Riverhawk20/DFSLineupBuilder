package com.dfs.dfslineupbuilder.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link RegulationFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

// source: https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
public class RegulationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Context ctx;
    private static final String TAG = "SignupActivity";
    public RegulationFragment(Context context) {
        // Required empty public constructor
        ctx = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegulationFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static RegulationFragment newInstance(String param1, String param2) {
//        RegulationFragment fragment = new RegulationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    int PERMISSION_ID = 44;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView regulationTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
        getLastLocation();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regulation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        regulationTV = (TextView) getView().findViewById(R.id.StateRegulationTV);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            //handle location
                            List<Double> coordinates = new ArrayList<>();
                            coordinates.add(location.getLatitude());
                            coordinates.add(location.getLongitude());
                            new GeocoderAsyncTask(ctx, regulationTV).execute(coordinates);
                        }
                    }
                });
            } else {
                Toast.makeText(ctx, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            //handle location

            List<Double> coordinates = new ArrayList<>();
            coordinates.add(mLastLocation.getLatitude());
            coordinates.add(mLastLocation.getLongitude());
            new GeocoderAsyncTask(ctx, regulationTV).execute(coordinates);

        }
    };

    private boolean checkPermissions() {
        return this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestPermissions(){
        this.getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_ID){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }

    static class GeocoderAsyncTask extends AsyncTask<List<Double>, Void, Address>{

        Context ctx;
        TextView regulationTV;

        GeocoderAsyncTask(Context context, TextView tv){
            ctx = context;
            regulationTV = tv;
        }

        @Override
            protected Address doInBackground(List<Double>... geoPoints)
            {
                try
                {
                    Geocoder geoCoder = new Geocoder(ctx);
                    double latitude = Math.round(geoPoints[0].get(0));
                    double longitude = Math.round(geoPoints[0].get(1));
                    Log.i(TAG,"lat: "+ latitude + " long: "+ longitude);
                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0)
                        return addresses.get(0);
                }
                catch (IOException ex)
                {
                    Log.e(TAG, ex.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address)
            {
                // do whatever you want/need to do with the address found
                // remember to check first that it's not null

                if(address != null) {
                    Log.i(TAG, "address: " + address.toString());
                    regulationTV.setText("You are in: " + address.getAdminArea());
                }else{
                    regulationTV.setText("Error getting location");
                }
            }
        }
    }


