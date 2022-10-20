package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dfs.dfslineupbuilder.LineUpAdapter;
import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.SlateAdapter;

import java.util.ArrayList;

public class LineUpFragment extends Fragment {

    TextView balanceText;
    TextView positionFilled;

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
        adapter.setContext(getContext());
        recyclerView.setAdapter(adapter);
        adapter.setSlates(new ArrayList<>());
        return v;
    }
}