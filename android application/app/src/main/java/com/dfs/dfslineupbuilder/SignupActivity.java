package com.dfs.dfslineupbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d(TAG, "onCreate SignupActivity");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart SignupActivity");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume SignupActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause SignupActivity");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop SignupActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart SignupActivity");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy SignupActivity");
        super.onDestroy();
    }
}