package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfs.dfslineupbuilder.adapter.LineUpAdapter;
import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.viewmodel.LineUpViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SlateViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateLineUpFragment extends Fragment {

    TextView balanceText;
    TextView positionFilled;
    PlayerRepository playerRepository;
    LineUpViewModel lineUpViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_line_up, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.LineUpContainerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        LineUpAdapter adapter = new LineUpAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setSlates(new ArrayList<>());

        lineUpViewModel = new ViewModelProvider(this).get(LineUpViewModel.class);

        balanceText = v.findViewById(R.id.BalanceTxt);
        positionFilled = v.findViewById(R.id.PositionFilledTxt);

        lineUpViewModel.getBalanceLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                balanceText.setText("Balance: "+String.valueOf(integer));
            }
        });

        lineUpViewModel.getPositionFilledLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                positionFilled.setText("Position: "+integer+"/"+9);
            }
        });
        return v;
    }
}