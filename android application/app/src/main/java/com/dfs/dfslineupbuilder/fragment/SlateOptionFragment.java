package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.adapter.SlateAdapter;
import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.data.model.Player;
import com.dfs.dfslineupbuilder.data.model.Regulation;
import com.dfs.dfslineupbuilder.data.model.SlateTest;
import com.dfs.dfslineupbuilder.data.repository.PlayerRepository;
import com.dfs.dfslineupbuilder.viewmodel.SharedHelperViewModel;
import com.dfs.dfslineupbuilder.viewmodel.SlateViewModel;
import com.dfs.dfslineupbuilder.data.repository.SlateRepository;
import com.dfs.dfslineupbuilder.data.model.Slate;
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlateOptionFragment extends Fragment {

    private SlateViewModel slateViewModel;
    private RecyclerView recyclerView;
    private SlateAdapter slateAdapter;
    private List<Slate> slateList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slate_option, container, false);
        recyclerView = v.findViewById(R.id.SlateContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        slateList = new ArrayList<>();
        slateAdapter = new SlateAdapter(getContext().getApplicationContext(), slateList);

        recyclerView.setAdapter(slateAdapter);
        slateViewModel = new ViewModelProvider(requireActivity()).get(SlateViewModel.class);
        slateAdapter.setViewModel(slateViewModel);
        new ViewModelProvider(getActivity()).get(SharedHelperViewModel.class).setIndex(-1);
        slateViewModel.getSlates().observe(getViewLifecycleOwner(), new Observer<List<Slate>>() {
            @Override
            public void onChanged(List<Slate> slateList) {
                recyclerView.setAdapter(slateAdapter);
                slateAdapter.getAllSlates(slateList);
            }
        });
        
        return v;
    }
}