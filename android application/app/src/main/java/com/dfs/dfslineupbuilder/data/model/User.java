package com.dfs.dfslineupbuilder.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int UserId;
    public String Email;
    public String PasswordHash;

    public User(String Email, String PasswordHash){
        this.Email = Email;
        this.PasswordHash = PasswordHash;
    }
}
