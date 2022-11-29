package com.dfs.dfslineupbuilder.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dfs.dfslineupbuilder.adapter.LineUpAdapter;
import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.data.model.Lineup;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.SlateWithPlayers;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.viewmodel.LineUpViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SharedHelperViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SlateViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateLineUpFragment extends Fragment {

    TextView balanceText;
    TextView positionFilled;
    PlayerRepository playerRepository;
    LineUpViewModel lineUpViewModel;
    int slateId;
    SharedHelperViewModel sharedHelperViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        slateId = b.getInt("slate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Build Lineup");
        // Inflate the layout for this fragment
        lineUpViewModel = new ViewModelProvider(this).get(LineUpViewModel.class);
        lineUpViewModel.setSlateId(slateId);
        View v = inflater.inflate(R.layout.fragment_line_up, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.LineUpContainerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        LineUpAdapter adapter = new LineUpAdapter();
        recyclerView.setAdapter(adapter);

        lineUpViewModel.getPlayerLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Player>>() {
            @Override
            public void onChanged(ArrayList<Player> players) {
                adapter.setLineup(players);
            }
        });

        balanceText = v.findViewById(R.id.BalanceTxt);
        positionFilled = v.findViewById(R.id.PositionFilledTxt);

        lineUpViewModel.getBalanceLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                balanceText.setText("Balance: " + String.valueOf(integer));
            }
        });

        lineUpViewModel.getPositionFilledLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                positionFilled.setText("Position: " + integer + "/" + 9);
            }
        });

        getParentFragmentManager().setFragmentResultListener("playerData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Toast.makeText((Context) getActivity(), result.getString(requestKey), Toast.LENGTH_LONG).show();
            }
        });
        sharedHelperViewModel = new ViewModelProvider(getActivity()).get(SharedHelperViewModel.class);
        sharedHelperViewModel.getSelectedPlayer().observe(getViewLifecycleOwner(), new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                if (sharedHelperViewModel.getIndex().getValue() != -1
                        && player != null) {
                    if (lineUpViewModel.setPlayerOnLineUp(player, sharedHelperViewModel.getIndex().getValue())) {
                        Toast.makeText(getContext(), "Player Added", Toast.LENGTH_SHORT).show();
                        sharedHelperViewModel.setIndex(-1);
                        sharedHelperViewModel.setSelectedPlayer(null);

                    } else {
                        Toast.makeText(getContext(), "Cannot add player", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        v.findViewById(R.id.saveLineupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lineUpViewModel.saveLineup()) {
                    Toast.makeText(getContext(), "Lineup Saved!", Toast.LENGTH_SHORT).show();
                    sharedHelperViewModel.setIndex(-1);
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Lineup cannot be saved, fill in the rest of the players",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        adapter.setValues(slateId, sharedHelperViewModel);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedHelperViewModel.setIndex(-1);
    }
}