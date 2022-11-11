package com.dfs.dfslineupbuilder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";

    private UserViewModel userViewModel;
    private Button createAccount;
    private EditText emailET, passwordET;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d(TAG, "onCreate SignupActivity");
//        userViewModel = new UserViewModel(getApplication());
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe((LifecycleOwner) this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

            }
        });
        createAccount = findViewById(R.id.ConfirmCreateAccountButton);
        createAccount.setOnClickListener(this);
        emailET = findViewById(R.id.CreateAccountEmailAddress);
        passwordET = findViewById(R.id.CreateAccountPassword);
        ctx = this.getApplicationContext();
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
        Log.i(TAG,"create account clicked");
        if(view.getId() == R.id.ConfirmCreateAccountButton){
            if(validateFields()){
                createAccount();
            }
        }
    }

    private void createAccount() {
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256HashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String passwordHash = StringUtils.bytesToHex(sha256HashBytes);

            Log.i(TAG, "hash: "+passwordHash);

            User newUser = new User(email, passwordHash);

            checkRoomUsers(newUser);


        }catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, "Error: No SHA-256 algorithm found", Toast.LENGTH_SHORT).show();
        }
    }

    private void postUserToNetwork(User newUser) {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.postUser("https://j3fvqoaanh.execute-api.us-east-1.amazonaws.com/Prod/writeuser", newUser);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "user added to dynamoDB");
                }else{
                    Log.i(TAG, "response code "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "failure to add user to dynamoDB", t);
            }
        });
    }


    private boolean validateFields(){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        Log.i(TAG, "email length "+ email.length());

        if(email.length() != 0 && password.length() != 0){
            //regex to check for valid email string
            //source: https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
            Pattern rfc2822 = Pattern.compile(
                    "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
            );
            if(!rfc2822.matcher(email).matches()){
                Toast.makeText(ctx, "Invalid Email", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(password.length() < 6){
                Toast.makeText(ctx, "Password Must Be 6+ Chars", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        Toast.makeText(ctx, "Empty field", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void createUser(User newUser){
        userViewModel.insert(newUser);
        postUserToNetwork(newUser);

        LoggedInUser.clearLoggedInUser(ctx);

        LoggedInUser.setLoggedInUser(ctx, newUser.UserId, newUser.Email);
        Log.i(TAG, "Logged in user Id: "+LoggedInUser.getLoggedInUser(ctx));

        Toast.makeText(ctx, "Account Created!", Toast.LENGTH_SHORT).show();
        emailET.setText("");
        passwordET.setText("");
        startActivity(new Intent(ctx, UserLandingPageActivity.class));
    }

    private void checkRoomUsers(User newUser){
        String email = emailET.getText().toString();

        LiveData<List<User>> allUsers = userViewModel.getAllUsers();

        boolean foundUser = false;
        for (User user: allUsers.getValue()) {
            if(user.Email.equals(email)){
                Log.i(TAG, "email already registered in room");
                foundUser = true;
                handleSignupFail();
            }
        }
        if(!foundUser){
            checkNetworkUsers(newUser);
        }
    }

    private void checkNetworkUsers(User newUser) {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<User> call = apiInterface.getUserByEmail("https://33c41umu3j.execute-api.us-east-1.amazonaws.com/Prod/getuserbyemail", newUser.Email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        if(response.body().Email.equals(newUser.Email)){
                            Log.i(TAG, "email already registered in network");
                            handleSignupFail();
                        }
                    }
                }else{
                    createUser(newUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "failure to get user by email", t);
                Toast.makeText(ctx, "Network Failure", Toast.LENGTH_SHORT).show();
                emailET.setText("");
                passwordET.setText("");
            }
        });
    }

    public void handleSignupFail(){
        Toast.makeText(ctx, "Email already registered", Toast.LENGTH_SHORT).show();
        emailET.setText("");
        passwordET.setText("");
    }
}