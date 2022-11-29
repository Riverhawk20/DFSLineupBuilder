package com.dfs.dfslineupbuilder.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.viewmodel.BottomBarViewModel;

public class BottomBarFragment extends Fragment implements View.OnClickListener {

    BottomBarViewModel bottomBarViewModel;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        v.findViewById(R.id.HomeButton).setOnClickListener(this);
        v.findViewById(R.id.ProfileButton).setOnClickListener(this);
        v.findViewById(R.id.RegulationButton).setOnClickListener(this);
        bottomBarViewModel = new ViewModelProvider(requireActivity()).get(BottomBarViewModel.class);
        Button b = v.findViewById(bottomBarViewModel.getLast());
        b.setBackgroundColor(getResources().getColor(R.color.selectedGreen));
        return v;
    }

    SlateOptionFragment slateOptionFragment = new SlateOptionFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    RegulationFragment regulationFragment = new RegulationFragment();

    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        if (v.getId() == R.id.HomeButton) {
            swapSelected(R.id.HomeButton);
            fm.beginTransaction().replace(R.id.ContentFragment, slateOptionFragment).commit();
        } else if (v.getId() == R.id.ProfileButton) {
            swapSelected(R.id.ProfileButton);
            fm.beginTransaction().replace(R.id.ContentFragment,new ProfileFragment()).addToBackStack(null).commit();
        } else {
            swapSelected(R.id.RegulationButton);
            fm.beginTransaction().replace(R.id.ContentFragment, regulationFragment).addToBackStack(null).commit();
        }
    }

    public void swapSelected(int current) {
        if (current != bottomBarViewModel.getLast()) {
            Button b = (Button) v.findViewById(current);
            b.setBackgroundColor(getResources().getColor(R.color.selectedGreen));
            b = (Button) v.findViewById(bottomBarViewModel.getLast());
            b.setBackgroundColor(getResources().getColor(R.color.mainGreen));
            bottomBarViewModel.setLast(current);
        }
    }
}