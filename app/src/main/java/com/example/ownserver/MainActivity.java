package com.example.ownserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> userIdList;
    private ArrayList<String> userNameList;
    private ListView mListView;
    private Button getInfo, insertUser, toUpdate;
    private EditText insertUserID, insertUserName;
    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.infoList);

        getInfo = (Button)findViewById(R.id.getInfo);
        insertUser = (Button)findViewById(R.id.insertUser);
        toUpdate = (Button)findViewById(R.id.toUpdateUserInfo);

        insertUserID = (EditText)findViewById(R.id.userID);
        insertUserName = (EditText)findViewById(R.id.userName);

        userIdList = new ArrayList<>();
        userNameList = new ArrayList<>();

        insertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNewUser();
            }
        });

        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo();
            }
        });

        toUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateUserInformation.class);
                startActivity(intent);
            }
        });
    }

    private void insertNewUser(){
        userIdList.clear();
        userNameList.clear();
        if(insertUserID.getText().toString().length() == 0){
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(insertUserName.getText().toString().length() < 2){
            Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(insertUserID.getText().toString(), insertUserName.getText().toString());
        Call<UserList> insertUser = apiInterface.insertUser(user.getId(), user.getName());
        insertUser.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                UserList resultList = response.body();
                List<UserList.Users> usersList = resultList.data;

                for(UserList.Users user : usersList)
                    userIdList.add(user.id);

                if(userIdList.get(0) != null){
                    Toast.makeText(getApplicationContext(), "성공적으로 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("ABC", t.getMessage());
                call.cancel();
            }
        });
    }

    private void getUserInfo(){
        userIdList.clear();
        userNameList.clear();
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
                        userIdList.add(user.id);
                        userNameList.add(user.name);
                        debugResponse += "ID : " + user.id + "  NAME : " + user.name + ", ";
                    }
                    UserListAdapter userListAdapter = new UserListAdapter(MainActivity.this, userIdList, userNameList);
                    userListAdapter.notifyDataSetChanged();
                    mListView.setAdapter(userListAdapter);
                    Log.d("RESULT/USER LIST", debugResponse);
                }else{
                    Log.d("ERROR!", "ERROR!!!");
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "불러오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("ABC", t.getMessage());
                call.cancel();
            }
        });

    }
}