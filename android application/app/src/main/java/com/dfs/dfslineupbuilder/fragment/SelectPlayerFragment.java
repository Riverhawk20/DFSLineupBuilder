package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfs.dfslineupbuilder.LineUpAdapter;
import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.SelectPlayerAdapter;

import java.util.ArrayList;

public class SelectPlayerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_player, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.PlayerContainerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        SelectPlayerAdapter adapter = new SelectPlayerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setSlates(new ArrayList<>());
        return v;
    }
}