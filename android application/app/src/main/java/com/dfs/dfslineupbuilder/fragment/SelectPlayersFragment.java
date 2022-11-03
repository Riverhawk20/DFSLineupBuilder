package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
    int slateId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        slateId = b.getInt("slate");
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
        Log.d("slate", "getting player from slate id "+slateId);
        playerRepository.getPlayers(slateId).observe(getViewLifecycleOwner(), new Observer<List<SlateWithPlayers>>() {
            @Override
            public void onChanged(List<SlateWithPlayers> slateWithPlayers) {
                adapter.setSlates(slateWithPlayers.get(0).players);
            }});
        return v;
    }
}