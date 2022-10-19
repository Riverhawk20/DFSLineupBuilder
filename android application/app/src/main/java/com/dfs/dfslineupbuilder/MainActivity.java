package com.dfs.dfslineupbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dfs.dfslineupbuilder.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginClick(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void onSignupClick(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

}