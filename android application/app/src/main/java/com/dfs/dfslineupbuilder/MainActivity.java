package com.dfs.dfslineupbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dfs.dfslineupbuilder.activity.LoginActivity;
import com.dfs.dfslineupbuilder.activity.SignupActivity;
import com.dfs.dfslineupbuilder.activity.UserLandingPageActivity;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Regulation;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateTest;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;
import com.dfs.dfslineupbuilder.fragment.RegulationFragment;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PlayerRepository playerRepository;
    private SlateRepository slateRepository;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient fusedLocationProviderClient;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slateRepository = new SlateRepository(this.getApplication());
        playerRepository = new PlayerRepository(this.getApplication());
        ctx = this;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
        getLastLocation();
        try{
            networkRequest();
        }catch (Exception e){
            Log.d("Network","Error Fetching saved lineups");
        }
    }

    public void onLoginClick(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void onSignupClick(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private void networkRequest() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<SlateTest>> call = apiInterface
                .getAllSlates("https://qkjpmd9d09.execute-api.us-east-1.amazonaws.com/Prod/getslates");

        call.enqueue(new Callback<List<SlateTest>>() {
            @Override
            public void onResponse(Call<List<SlateTest>> call, Response<List<SlateTest>> response) {
                if (response.isSuccessful()) {
                    List<Slate> slate = new ArrayList<>();
                    for (SlateTest s : response.body()) {
                        for (Player p : s.Players) {
                            p.SlateId = s.SlateId;
                        }
                        playerRepository.insert(s.Players);
                        Slate sl = new Slate(s.SeasonYear, s.SlateName, s.StartDate, s.Week);
                        sl.SlateId = s.SlateId;
                        slate.add(sl);
                    }
                    slateRepository.insert(slate);
                    Log.d("slate fragment", "onResponse: " + response.body());
                } else {
                    Log.d("slate fragment", "slate network call fail");
                }
            }

            @Override
            public void onFailure(Call<List<SlateTest>> call, Throwable t) {
                Log.e("slate fragment", "onResponse error", t);
            }
        });

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
                            reverseGeocode(location);
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

    private void reverseGeocode(Location location) {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(location.getLatitude());
        coordinates.add(location.getLongitude());
        new MainActivity.GeocoderAsyncTask(ctx).execute(coordinates);
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
            Location lastLocation = locationResult.getLastLocation();
            //handle location
            reverseGeocode(lastLocation);
        }
    };

    private boolean checkPermissions() {
        return this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestPermissions(){
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ID);
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

    static class GeocoderAsyncTask extends AsyncTask<List<Double>, Void, Address> {

        Context ctx;

        GeocoderAsyncTask(Context context){
            ctx = context;
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
                //UserLocation.setUserLocation(ctx, "Ohio");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Address address)
        {
            // do whatever you want/need to do with the address found
            // remember to check first that it's not null

            if(address != null) {
                String state = address.getAdminArea();
                Log.i(TAG, "state: "+ state);
                UserLocation.setUserLocation(ctx, state);
            }
        }
    }

}