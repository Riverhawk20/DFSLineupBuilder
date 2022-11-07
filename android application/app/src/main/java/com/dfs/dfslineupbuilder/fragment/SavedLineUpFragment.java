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

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.adapter.SavedLineUpAdapter;
import com.dfs.dfslineupbuilder.adapter.SavedSlateAdapter;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SavedSlateWithSavedPlayer;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.viewmodel.LineUpViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SavedLineUpViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SavedSlateViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SharedHelperViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedLineUpFragment extends Fragment {

    TextView balanceText;
    TextView positionFilled;
    PlayerRepository playerRepository;
    LineUpViewModel lineUpViewModel;
    int slateId;
    SharedHelperViewModel sharedHelperViewModel;
    SavedLineUpViewModel savedLineUpViewModel;
    RecyclerView recyclerView;
    SavedLineUpAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();
        slateId = b.getInt("slate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_line_up, container, false);
        savedLineUpViewModel = new ViewModelProvider(this).get(SavedLineUpViewModel.class);

        savedLineUpViewModel.setSlateId(slateId);
        recyclerView = v.findViewById(R.id.SavedLineUpContainerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new SavedLineUpAdapter();
        recyclerView.setAdapter(adapter);

        savedLineUpViewModel.getPlayerLiveData().observe(getViewLifecycleOwner(), new Observer<List<SavedSlateWithSavedPlayer>>() {
            @Override
            public void onChanged(List<SavedSlateWithSavedPlayer> slateWithPlayers) {
                adapter.setLineup(slateWithPlayers.get(0).players);
            }
        });


        return v;
    }
}