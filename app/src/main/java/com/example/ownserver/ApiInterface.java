package com.example.ownserver;

import com.example.ownserver.model.Data;
import com.example.ownserver.model.IdList;
import com.example.ownserver.model.LoginInfo;
import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    //RxJava
    @GET("getMyInfo.php")
    Single<Data> getMyInfo(@Query("id") String id);

    @GET("checkInfo.php")
    Single<UserList> getUserList();

    @GET("idCheck.php")
    Single<Data> getUserIDs();

    @GET("getLoginInfo.php")
    Single<Data> getLoginInfo(@Query("id") String id);

    @FormUrlEncoded
    @POST("join.php")
    Single<UserList> insertUser(@Field("id") String id, @Field("password") String password, @Field("name") String name);

    @FormUrlEncoded
    @POST("updateUser.php")
    Single<UserList> updateUser(@Field("id") String id, @Field("name") String name);

    @DELETE("deleteUser.php")
    Single<UserList> deleteUser(@Query("id") String id);

    @Multipart
    @POST("uploadImage.php")
    Single<Data> uploadImage(@Part MultipartBody.Part File, @Part("id") String id, @Part("serverPath") String serverPath);

//    Retrofit 버전
//    @GET("getMyInfo.php")
//    Call<Data> getMyInfo(@Query("id") String id);
//
//    @GET("checkInfo.php")
//    Call<UserList> getUserList();
//
//    @GET("idCheck.php")
//    Call<Data> getUserIDs();
//
//    @GET("getLoginInfo.php")
//    Call<Data> getLoginInfo(@Query("id") String id);
//
//    @FormUrlEncoded
//    @POST("join.php")
//    Call<UserList> insertUser(@Field("id") String id, @Field("password") String password, @Field("name") String name);
//
//    @FormUrlEncoded
//    @POST("updateUser.php")
//    Call<UserList> updateUser(@Field("id") String id, @Field("name") String name);
//
//    @DELETE("deleteUser.php")
//    Call<Void> deleteUser(@Query("id") String id);
//
//    @Multipart
//    @POST("uploadImage.php")
//    Call<Void> uploadImage(@Part MultipartBody.Part File, @Part("id") String id, @Part("serverPath") String serverPath);
}