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

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.adapter.SavedSlateAdapter;
import com.dfs.dfslineupbuilder.data.model.SavedSlate;
import com.dfs.dfslineupbuilder.viewmodel.SavedSlateViewModel;

import java.util.List;

public class SavedSlateFragment extends Fragment {
    private SavedSlateViewModel savedSlateViewModel;
    private RecyclerView recyclerView;
    private SavedSlateAdapter adapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_slate, container, false);
        savedSlateViewModel = new ViewModelProvider(this).get(SavedSlateViewModel.class);
        recyclerView = v.findViewById(R.id.SavedSlateContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new SavedSlateAdapter();
        recyclerView.setAdapter(adapter);

        savedSlateViewModel.getSlates().observe(getViewLifecycleOwner(), new Observer<List<SavedSlate>>() {
            @Override
            public void onChanged(List<SavedSlate> savedSlates) {
                    adapter.setAllSlates(savedSlates);
            }
        });




        return v;
    }
}