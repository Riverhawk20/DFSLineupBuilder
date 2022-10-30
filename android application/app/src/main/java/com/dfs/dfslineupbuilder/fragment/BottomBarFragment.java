package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dfs.dfslineupbuilder.R;

public class BottomBarFragment extends Fragment  implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        v.findViewById(R.id.HomeButton).setOnClickListener(this);
        v.findViewById(R.id.ProfileButton).setOnClickListener(this);
        v.findViewById(R.id.RegulationButton).setOnClickListener(this);
        return v;
    }

    public void onClick(View v){
        FragmentManager fm = getParentFragmentManager();
        if(v.getId() == R.id.HomeButton){
            fm.beginTransaction().replace(R.id.ContentFragment,new SlateOptionFragment()).commit();
        }else if (v.getId() == R.id.ProfileButton){
            fm.beginTransaction().replace(R.id.ContentFragment,new ProfileFragment()).addToBackStack(null).commit();
        }else{
            fm.beginTransaction().replace(R.id.ContentFragment,new RegulationFragment()).addToBackStack(null).commit();
        }
    }
}