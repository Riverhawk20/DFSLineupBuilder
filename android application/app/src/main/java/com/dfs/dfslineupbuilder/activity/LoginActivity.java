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
import com.dfs.dfslineupbuilder.retrofit.APIClient;
import com.dfs.dfslineupbuilder.retrofit.APIInterface;
import com.dfs.dfslineupbuilder.viewmodel.UserViewModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            checkRoomUsers();
        }
    }

    private void checkRoomUsers(){
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

        boolean foundUser = false;
        for (User user: allUsers.getValue()) {
            if(user.Email.equals(email)){
                if(user.PasswordHash.equals(passwordHash)) {
                    Log.i(TAG, "login success from room");
                    foundUser = true;
                    handleLoginSuccess(user);
                }else{
                    handleLoginFail();
                }
            }
        }
        if(!foundUser){
            checkNetworkUsers(email, passwordHash);
        }
    }

    private void checkNetworkUsers(String email, String passwordHash) {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<User> call = apiInterface.getUserByEmail("https://33c41umu3j.execute-api.us-east-1.amazonaws.com/Prod/getuserbyemail", email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        if(response.body().Email.equals(email) && response.body().PasswordHash.equals(passwordHash)){
                            Log.i(TAG, "login success from network");
                            userViewModel.insert(response.body());
                            handleLoginSuccess(response.body());
                        }else{
                            handleLoginFail();
                        }
                    }
                }else{
                    Log.i(TAG, "response code "+response.code());
                    handleLoginFail();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "failure to get user by email", t);
            }
        });
    }

    private void handleLoginFail(){
        Toast.makeText(this.getApplicationContext(), "Login Failure", Toast.LENGTH_SHORT).show();
        passwordET.setText("");
    }

    private void handleLoginSuccess(User user){
        Toast.makeText(this.getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
        emailET.setText("");
        passwordET.setText("");

        //clear old logged in user
        LoggedInUser.clearLoggedInUser(this.getApplicationContext());

        LoggedInUser.setLoggedInUser(this.getApplicationContext(), user.UserId, user.Email);
        Log.i(TAG, "Logged in user Id: "+LoggedInUser.getLoggedInUser(this.getApplicationContext()));

        startActivity(new Intent(this.getApplicationContext(), UserLandingPageActivity.class));
    }

}

