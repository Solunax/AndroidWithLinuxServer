package com.example.ownserver.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    @SerializedName("data")
    public List<Users> data = null;

    public class Users{
        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;
    }
}
