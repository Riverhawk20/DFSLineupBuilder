package com.dfs.dfslineupbuilder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dfs.dfslineupbuilder.data.repository.SignupRepository;
import com.dfs.dfslineupbuilder.ui.signup.SignupFormState;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<SignupFormState> signupFormState = new MutableLiveData<>();
    private SignupRepository signupRepository;

    SignupViewModel(SignupRepository signupRepository){
        this.signupRepository = signupRepository;
    }
}
