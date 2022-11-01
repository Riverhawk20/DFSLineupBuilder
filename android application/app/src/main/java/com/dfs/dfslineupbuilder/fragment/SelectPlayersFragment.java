package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.adapter.SelectPlayerAdapter;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;

import java.util.List;

public class SelectPlayersFragment extends Fragment {
    PlayerRepository playerRepository;

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
        playerRepository = new PlayerRepository(getActivity().getApplication());
        playerRepository.getPlayers(76225).observe(getViewLifecycleOwner(), new Observer<List<SlateWithPlayers>>() {
            @Override
            public void onChanged(List<SlateWithPlayers> slateWithPlayers) {
                adapter.setSlates(slateWithPlayers.get(0).players);
            }});
        return v;
    }
}