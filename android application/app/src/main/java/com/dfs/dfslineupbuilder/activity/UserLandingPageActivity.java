package com.dfs.dfslineupbuilder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.fragment.BottomBarFragment;
import com.dfs.dfslineupbuilder.fragment.ProfileFragment;
import com.dfs.dfslineupbuilder.fragment.SlateOptionFragment;

public class UserLandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing_page);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.BottomBarContainer, new BottomBarFragment()).commit();
        fm.beginTransaction().add(R.id.ContentFragment, new SlateOptionFragment()).commit();
    }
}