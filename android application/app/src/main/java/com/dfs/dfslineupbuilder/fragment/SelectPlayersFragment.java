package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.adapter.SelectPlayerAdapter;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.viewmodel.SharedHelperViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectPlayersFragment extends Fragment {
    private static Supplier<Stream<String>> flexPositionsStreamSupplier = () -> Arrays.stream(new String[]{"RB", "WR", "TE"});
    PlayerRepository playerRepository;
    private int slateId;
    private int layoutPos;
    private String pos;
    SharedHelperViewModel sharedHelperViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        slateId = b.getInt("slate");
        pos = b.getString("position");
        layoutPos = b.getInt("layoutPos");
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
                if(pos.equals("FL") || layoutPos == 7){
                    List<Player> players = slateWithPlayers.get(0).players.stream().filter(p->flexPositionsStreamSupplier.get().anyMatch(p.Position::equals)).sorted((a,b)->b.Salary - a.Salary).collect(Collectors.toList());
                    adapter.setSlates(players);
                }else{
                    adapter.setSlates(slateWithPlayers.get(0).players.stream().filter(p->p.Position.equals(pos)).collect(Collectors.toList()));
                }

            }});
        sharedHelperViewModel = new ViewModelProvider(getActivity()).get(SharedHelperViewModel.class);
        adapter.setValues(sharedHelperViewModel);
        return v;
    }
}