package com.dfs.dfslineupbuilder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dfs.dfslineupbuilder.data.repository.UserRepository;
import com.dfs.dfslineupbuilder.data.model.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository=new UserRepository(application);
        allUsers=userRepository.getAllUsers();
    }

    public void insert(User newUser)
    {
        userRepository.insert(newUser);
    }


    public LiveData<List<User>> getAllUsers()
    {
        return allUsers;
    }
}
