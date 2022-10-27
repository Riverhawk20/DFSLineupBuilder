package com.dfs.dfslineupbuilder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.dfs.dfslineupbuilder.LoggedInUser;
import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.StringUtils;
import com.dfs.dfslineupbuilder.data.model.User;
import com.dfs.dfslineupbuilder.viewmodel.UserViewModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private Button loginButton;
    private EditText emailET, passwordET;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate LoginActivity");

        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe((LifecycleOwner) this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

            }
        });
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        emailET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG,"login clicked");
        if(view.getId() == R.id.login){
            int userId = checkLogin();
            if(userId > 0){
                Log.i(TAG, "login success");
                handleLoginSuccess(userId);
            }else{
                Log.i(TAG, "login failure");
                handleLoginFail();
            }
        }
    }

    private int checkLogin(){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordHash = "";

        LiveData<List<User>> allUsers = userViewModel.getAllUsers();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256HashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            passwordHash = StringUtils.bytesToHex(sha256HashBytes);

        }catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, "Error: No SHA-256 algorithm found", Toast.LENGTH_SHORT).show();
        }

        for (User user: allUsers.getValue()) {
            if(user.Email.equals(email) && user.PasswordHash.equals(passwordHash)){
                return user.UserId;
            }
        }
        return -1;
    }

    private void handleLoginFail(){
        Toast.makeText(this.getApplicationContext(), "Login Failure", Toast.LENGTH_SHORT).show();
        emailET.setText("");
        passwordET.setText("");
        finish();
    }

    private void handleLoginSuccess(int userId){
        Toast.makeText(this.getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
        emailET.setText("");
        passwordET.setText("");

        LoggedInUser.setLoggedInUser(this.getApplicationContext(), userId);

        startActivity(new Intent(this.getApplicationContext(), UserLandingPageActivity.class));
    }

}

