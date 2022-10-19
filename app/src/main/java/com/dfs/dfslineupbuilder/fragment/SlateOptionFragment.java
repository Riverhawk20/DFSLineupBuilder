package com.dfs.dfslineupbuilder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dfs.dfslineupbuilder.R;

public class SlateOptionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getParentFragmentManager();
//        fm.beginTransaction().add(R.id.GameFragmentContainer, new GameFragment()).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_slate_option, container, false);
        for(int i = 0; i < 15; i++){
            addSlate(v,"Slate  "+(i+1));
        }
        return v;
    }

    public void addSlate(View v, String text){
        LinearLayout layout = v.findViewById(R.id.SlateContainerLayout);
        Button btn = new Button(getContext());
        btn.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("slate",text+" button");
                LineUpFragment fragment = new LineUpFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.ContentFragment,fragment).commit();
            }
        });
        layout.addView(btn);
    }
}