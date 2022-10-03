package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int UserId;
    public String Email;
    public String PasswordHash;

    public User(String email, String passwordHash){
        this.Email = email;
        this.PasswordHash = passwordHash;
    }
}
