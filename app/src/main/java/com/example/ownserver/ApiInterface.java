package com.example.ownserver;

import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @GET("checkInfo.php")
    Call<UserList> getUserList();

    @FormUrlEncoded
    @POST("insertUser.php")
    Call<UserList> insertUser(@Field("id") String id, @Field("name") String name);

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<UserList> updateUser(@Field("id") String id, @Field("name") String name);
}