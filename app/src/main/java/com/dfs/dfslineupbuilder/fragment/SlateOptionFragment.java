package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.SlateAdapter;

import java.util.ArrayList;

public class SlateOptionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slate_option, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.SlateContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        SlateAdapter adapter = new SlateAdapter();
        adapter.setContext(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setSlates(new ArrayList<>());

        return v;
    }
}