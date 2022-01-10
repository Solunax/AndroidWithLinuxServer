package com.example.ownserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> myArraylist;
    private ListView mListView;
    private Button getinfo;

//    Gson gson = new GsonBuilder().setLenient().create();
//
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://192.168.56.117/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.infoList);
        getinfo = (Button)findViewById(R.id.getInfo);
        myArraylist = new ArrayList<>();

        getinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserinfo();
            }
        });
    }

    private void getUserinfo(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Object> get = apiInterface.getInformation();
        get.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if(response.isSuccessful()){
                    Log.d("SUCCESS!/USER", "SUCCESS!");
                    Log.d("RESULT/USER",  response.body().toString());
                    Log.d("RESULT/USER", new Gson().toJson(response.body()));

                }else{
                    Log.d("ERROR!", "ERROR!!!");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("F", "FAIL!");
            }
        });

        Call<UserList> getListInfo = apiInterface.getUserList();
        getListInfo.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if(response.isSuccessful()){
                    Log.d("SUCCESS!/USER LIST", "SUCCESS!");
                    UserList resultList = response.body();
                    List<UserList.Users> usersList = resultList.data;
                    String debugResponse = "";
                    for(UserList.Users user : usersList){
                        debugResponse += "ID : " + user.id + "  NAME : " + user.name + ", ";
                    }
                    Log.d("RESULT/USER LIST", debugResponse);
                }else{
                    Log.d("ERROR!", "ERROR!!!");
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {

            }
        });

    }
}