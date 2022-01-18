package com.example.ownserver;

import static com.example.ownserver.JoinActivity.checkNull;
import static com.example.ownserver.MainActivity.apiInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.example.ownserver.model.LoginInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private Button login, join;
    private EditText editId, editPassword;
    private ArrayList<String> loginInfo = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editId = (EditText)findViewById(R.id.login_id);
        editPassword = (EditText)findViewById(R.id.login_pw);

        login = (Button)findViewById(R.id.login);
        join = (Button)findViewById(R.id.join);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editId.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if(checkNull(id)){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    editId.requestFocus();
                    return;
                }

                if(checkNull(password)){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    editPassword.requestFocus();
                    return;
                }
                tryLogin(id, password);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    private void tryLogin(String id, String password){
        loginInfo.clear();

        Call<LoginInfo> getLoginInfo = apiInterface.getLoginInfo(id);
        getLoginInfo.enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                LoginInfo result = response.body();
                String debugResponse = "";

                for(String value: result.getData()){
                    loginInfo.add(value);
                    debugResponse += value + " ";
                }
                try{
                    if(loginInfo.get(0).isEmpty()){
                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        editId.requestFocus();
                        return;
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    editId.requestFocus();
                    return;
                }

                if(loginInfo.get(1).equals(password)){
                    Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id", id);
                    finish();
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    editPassword.requestFocus();
                    return;
                }
            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
