package com.dfs.dfslineupbuilder.ui.login;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dfs.dfslineupbuilder.R;
import com.dfs.dfslineupbuilder.UserLandingPageActivity;
import com.dfs.dfslineupbuilder.UserViewModel;
import com.dfs.dfslineupbuilder.data.model.User;
import com.dfs.dfslineupbuilder.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    private EditText emailET;
    private EditText passwordET;
    private UserViewModel userViewModel;
    private Button signInButton;
    private final List<User> allUsers = new ArrayList<>();

   @Override
   public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       Activity activity = this;
       userViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(UserViewModel.class);
       binding = ActivityLoginBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       emailET = binding.username;
       passwordET = binding.password;
       signInButton = binding.login;
       signInButton.setEnabled(true);
       signInButton.setOnClickListener(this);
       userViewModel.getAllUsers().observe((LifecycleOwner) activity, userAccounts -> {
           allUsers.clear();
           allUsers.addAll(userAccounts);
       });
   }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);
//
//        final EditText usernameEditText = binding.username;
//        final EditText passwordEditText = binding.password;
//        final Button loginButton = binding.login;
//        final ProgressBar loadingProgressBar = binding.loading;
//        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                loginButton.setEnabled(loginFormState.isDataValid());
//                if (loginFormState.getUsernameError() != null) {
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//                if (loginFormState.getPasswordError() != null) {
//                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
//                }
//            }
//        });
//
//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });
//
//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        };
//        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        });
//    }

//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void checkLogin(){
       final String email = emailET.getText().toString();
       final String password = passwordET.getText().toString();
       //User user = new User(email, password);

        boolean userMatch = false;
        for (User user: allUsers) {
            if(user.Email.equals(email) && user.PasswordHash.equals(password)){
                Log.i("login activity","user found");
                userMatch = true;
            }
        }
        if(userMatch){
            Log.i("login activity", "user found");
            //startActivity(new Intent(this, UserLandingPageActivity.class));
        }else{
            Log.i("login activity", "user not found");
        }
    }

    @Override
    public void onClick(View view) {
        checkLogin();
    }
}