package com.example.ownserver;

import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("checkInfo.php")
    Call<Object> getInformation();

    @GET("checkInfo.php")
    Call<UserList> getUserList();
}
