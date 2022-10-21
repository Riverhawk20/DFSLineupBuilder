package com.dfs.dfslineupbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dfs.dfslineupbuilder.data.model.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";

    private UserViewModel userViewModel;
    private Button createAccount;
    private EditText emailET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d(TAG, "onCreate SignupActivity");
        userViewModel = new UserViewModel(getApplication());
        createAccount = findViewById(R.id.ConfirmCreateAccountButton);
        createAccount.setOnClickListener(this);
        emailET = findViewById(R.id.CreateAccountEmailAddress);
        passwordET = findViewById(R.id.CreateAccountPassword);
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

    @Override
    public void onClick(View view) {
        Log.i("signup activity","create account clicked");
        if(view.getId() == R.id.ConfirmCreateAccountButton){
            createAccount();
            //startActivity(new Intent(SignupActivity.this, UserLandingPageActivity.class));

        }
    }

    private void createAccount(){
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        //TODO: CHECK THAT EMAIL AND PWD ARE VALID
        //TODO: HASH PWD
        User newUser = new User(email,password);
        userViewModel.insert(newUser);
    }
}