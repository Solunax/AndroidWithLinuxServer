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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
