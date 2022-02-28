package com.example.ownserver.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    private int number = 0;

    private String id;
    private String password;
    private Boolean autologin;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAutologin() {
        return autologin;
    }

    public void setAutologin(Boolean autologin) {
        this.autologin = autologin;
    }
}
