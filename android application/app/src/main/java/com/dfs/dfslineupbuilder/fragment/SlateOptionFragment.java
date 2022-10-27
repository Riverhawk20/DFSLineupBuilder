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
    private SlateRepository slateRepository;
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

        slateRepository = new SlateRepository(getActivity().getApplication());
        slateList = new ArrayList<>();
        slateAdapter = new SlateAdapter(getContext().getApplicationContext(), slateList);

        recyclerView.setAdapter(slateAdapter);
        slateViewModel = new ViewModelProvider(this).get(SlateViewModel.class);
        networkRequest();
        slateAdapter.setViewModel(slateViewModel);
        slateViewModel.getSlates().observe(getViewLifecycleOwner(), new Observer<List<Slate>>() {
            @Override
            public void onChanged(List<Slate> slateList) {
                recyclerView.setAdapter(slateAdapter);
                slateAdapter.getAllSlates(slateList);
            }
        });

        return v;
    }

    private void networkRequest(){

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Slate>> call = apiInterface.getAllSlates();

        call.enqueue(new Callback<List<Slate>>() {
            @Override
            public void onResponse(Call<List<Slate>> call, Response<List<Slate>> response) {
                if(response.isSuccessful()) {
                    slateRepository.insert(response.body());
                    Log.d("slate fragment", "onResponse: "+response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Slate>> call, Throwable t) {
                Log.e("slate fragment", "onResponse error", t);
            }
        });

    }
}