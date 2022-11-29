package com.dfs.dfslineupbuilder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.data.EntityRoomDatabase;
import com.dfs.dfslineupbuilder.data.dao.SlateDao;
import com.dfs.dfslineupbuilder.fragment.BottomBarFragment;
import com.dfs.dfslineupbuilder.fragment.ProfileFragment;
import com.dfs.dfslineupbuilder.fragment.RegulationFragment;
import com.dfs.dfslineupbuilder.fragment.SlateOptionFragment;
import com.dfs.dfslineupbuilder.viewmodel.BottomBarViewModel;

public class UserLandingPageActivity extends AppCompatActivity {

    BottomBarFragment bottomBarFragment = new BottomBarFragment();
    SlateOptionFragment slateOptionFragment = new SlateOptionFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_landing_page);
        if (savedInstanceState != null)
            return;
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.BottomBarContainer, bottomBarFragment).commit();
        fm.beginTransaction().replace(R.id.ContentFragment, slateOptionFragment).commit();
        BottomBarViewModel bottomBarViewModel = new ViewModelProvider(this).get(BottomBarViewModel.class);
        bottomBarViewModel.setLast(R.id.HomeButton);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        return true;
    }
}