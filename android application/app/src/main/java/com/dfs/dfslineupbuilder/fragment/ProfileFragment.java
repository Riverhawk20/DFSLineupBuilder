package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfs.dfslineupbuilder.LoggedInUser;
import com.dfs.dfslineupbuilder.R;

public class ProfileFragment extends Fragment {

    private TextView userInfo;
    SavedSlateFragment savedSlateFragment =  new SavedSlateFragment();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.UserLineupContainer, savedSlateFragment).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userInfo = (TextView) getView().findViewById(R.id.UserName);
        userInfo.setText("Welcome " + LoggedInUser.getLoggedInUserName(this.getContext()) + "!");
    }
}