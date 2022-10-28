package com.dfs.dfslineupbuilder.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.dfs.dfslineupbuilder.viewmodel.UserViewModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

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
        Log.i(TAG,"create account clicked");
        if(view.getId() == R.id.ConfirmCreateAccountButton){
            if(validateFields()){
                createAccount();
                Toast.makeText(this.getApplicationContext(), "Account Created!", Toast.LENGTH_SHORT).show();
                emailET.setText("");
                passwordET.setText("");
                startActivity(new Intent(this.getApplicationContext(), UserLandingPageActivity.class));
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
            userViewModel.insert(newUser);

            LoggedInUser.clearLoggedInUser(this.getApplicationContext());

            LoggedInUser.setLoggedInUser(this.getApplicationContext(), newUser.UserId);
            Log.i(TAG, "Logged in user Id: "+LoggedInUser.getLoggedInUser(this.getApplicationContext()));

        }catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, "Error: No SHA-256 algorithm found", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(this.getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(password.length() < 6){
                Toast.makeText(this.getApplicationContext(), "Password Must Be 6+ Chars", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        Toast.makeText(this.getApplicationContext(), "Empty field", Toast.LENGTH_SHORT).show();
        return false;
    }
}