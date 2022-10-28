package com.dfs.dfslineupbuilder.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;

@Entity
public class User {
    @PrimaryKey @NonNull
    public String UserId;

    public String Email;

    public String PasswordHash;

    public User(String Email, String PasswordHash){
        this.Email = Email;
        this.PasswordHash = PasswordHash;
        this.UserId = java.util.UUID.randomUUID().toString();
    }
}
