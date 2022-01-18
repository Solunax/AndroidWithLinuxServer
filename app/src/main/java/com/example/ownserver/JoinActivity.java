package com.example.ownserver;

import static com.example.ownserver.MainActivity.apiInterface;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import com.example.ownserver.model.IdList;
import com.example.ownserver.model.UserList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends Activity {
    private Button join, check;
    private EditText editId, editPassword, editName;
    private ArrayList<String>idList= new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        editId = (EditText)findViewById(R.id.join_userID);
        editPassword = (EditText)findViewById(R.id.join_userPW);
        editName = (EditText)findViewById(R.id.join_userName);

        join = (Button)findViewById(R.id.join_complete);
        check = (Button)findViewById(R.id.id_check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idChecking(editId.getText().toString().trim());
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editId.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String name = editName.getText().toString().trim();

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

                if(checkNull(name)){
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    editName.requestFocus();
                    return;
                }

                joinUser(id, password, name);
            }
        });
    }

    private void idChecking(String id){
        if(checkNull(id)){
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            editId.requestFocus();
            return;
        }

        idList.clear();

        Call<IdList> getIDList = apiInterface.getUserIDs();
        getIDList.enqueue(new Callback<IdList>() {
            @Override
            public void onResponse(Call<IdList> call, Response<IdList> response) {
                IdList result = response.body();
                String debugResponse = "";

                for(String value: result.getData()){
                    idList.add(value);
                    debugResponse += value + " ";
                }
                Log.d("ID LIST", idList.toString());
                Log.d("ID", debugResponse);

                if(idList.contains(id))
                    Toast.makeText(getApplicationContext(), "중복된 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    check.setEnabled(false);
                    join.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<IdList> call, Throwable t) {

            }
        });
    }

    private void joinUser(String id, String pw, String name){
        Call<UserList> joinNew = apiInterface.insertUser(id, pw, name);
        joinNew.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d("FAIL", "FAIL");
            }
        });
    }

    public static Boolean checkNull(String value){
        if(value.isEmpty())
            return true;
        else
            return false;
    }
}