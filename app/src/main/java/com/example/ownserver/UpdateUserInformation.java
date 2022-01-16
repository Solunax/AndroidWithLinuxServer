package com.example.ownserver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserInformation extends Activity {
    private ArrayList<String> userIdList;
    private EditText id, name;
    private Button update;
    private ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_information);

        id = (EditText)findViewById(R.id.updateUserID);
        name = (EditText)findViewById(R.id.updateUserName);

        update = (Button)findViewById(R.id.updateUserInfo);

        userIdList = new ArrayList<>();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });
    }

    private void updateUserInfo(){
        userIdList.clear();
        if(id.getText().toString().length() == 0){
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.getText().toString().length() < 2){
            Toast.makeText(this, "변경할 이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<UserList> updateUser = apiInterface.updateUser(id.getText().toString(), name.getText().toString());
        updateUser.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                UserList resultList = response.body();
                List<UserList.Users> usersList = resultList.data;

                for(UserList.Users user : usersList)
                    userIdList.add(user.id);

                if(userIdList.get(0) != null){
                    Toast.makeText(getApplicationContext(), "성공적으로 변경했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                call.cancel();
            }
        });
    }
}