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
import com.dfs.dfslineupbuilder.UserLocation;
import com.dfs.dfslineupbuilder.data.model.Regulation;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    static TextView regulationTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ctx = requireContext();

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
        String state = UserLocation.getUserLocation(ctx);
        Log.i(TAG, "got state: "+state);
        if(state.length()!=0) {
            networkRequest(UserLocation.getUserLocation(ctx));
        }else{
            regulationTV.setText("Error getting user location");
        }
    }

    private static void networkRequest(String state){

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Regulation>> call = apiInterface.getStateRegulation("https://p5w0cm320i.execute-api.us-east-1.amazonaws.com/Prod/getregulations");

        call.enqueue(new Callback<List<Regulation>>() {
            @Override
            public void onResponse(Call<List<Regulation>> call, Response<List<Regulation>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    for (Regulation r: response.body()){
                        if(r.StateName.equals(state)){
                            if(r.IsLegal){
                                regulationTV.setText("Sports betting is legal in "+r.StateName);
                            }else{
                                regulationTV.setText("Sports betting is not legal in "+r.StateName);
                            }
                        }
                    }
                }else{
                    regulationTV.setText("Error fetching sports betting regulation for " + state);
                }
            }

            @Override
            public void onFailure(Call<List<Regulation>> call, Throwable t) {
                Log.e("slate fragment", "onResponse error", t);
            }
        });

    }
    }


