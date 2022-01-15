package com.example.ownserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private Button getInfo, insertUser, toUpdate, toDelete;
    private EditText insertUserID, insertUserName;
    private Context context = this;
    private boolean deleteFlag = false;
    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.infoList);

        getInfo = (Button)findViewById(R.id.getInfo);
        insertUser = (Button)findViewById(R.id.insertUser);
        toUpdate = (Button)findViewById(R.id.toUpdateUserInfo);
        toDelete = (Button)findViewById(R.id.deleteInfo);

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

        toDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });
    }

    private void deleteDialog(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText deleteID = (EditText)dialog.findViewById(R.id.deleteUserID);
        Button yes = (Button)dialog.findViewById(R.id.delete_yes);
        Button no = (Button)dialog.findViewById(R.id.delete_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteIdValue = deleteID.getText().toString().trim();

                if(deleteIdValue.length() == 0){
                    Toast.makeText(context, "삭제할 아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Delete User Method
                Call<Void> deleteUser = apiInterface.deleteUser(deleteIdValue);
                deleteUser.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                            Log.d("SUCCESS!/ DELETE", "SUCCESS!");
                            dialog.dismiss();
                        }else{
                            Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "삭제에 실패", Toast.LENGTH_SHORT).show();
                        Log.d("FAIL", t.getMessage());
                        call.cancel();
                    }
                });
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void insertNewUser(){
        userIdList.clear();
        userNameList.clear();

        if(insertUserID.getText().toString().trim().length() == 0){
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(insertUserName.getText().toString().trim().length() < 2){
            Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(insertUserID.getText().toString().trim(), insertUserName.getText().toString().trim());
        Call<UserList> insertUser = apiInterface.insertUser(user.getId(), user.getName());
        insertUser.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                UserList resultList = response.body();
                List<UserList.Users> usersList = resultList.data;

                for(UserList.Users user : usersList)
                    userIdList.add(user.id);

                if(userIdList.get(0) != null){
                    Toast.makeText(context, "성공적으로 추가하였습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(context, "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("FAIL", t.getMessage());
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
                Toast.makeText(context, "불러오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("FAIL", t.getMessage());
                call.cancel();
            }
        });

    }
}