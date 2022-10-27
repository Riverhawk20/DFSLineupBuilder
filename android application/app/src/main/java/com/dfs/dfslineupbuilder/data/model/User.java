package com.dfs.dfslineupbuilder.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int UserId;

    public String Email;

    public String PasswordHash;

    public User(String Email, String PasswordHash){
        this.Email = Email;
        this.PasswordHash = PasswordHash;

    }
}
