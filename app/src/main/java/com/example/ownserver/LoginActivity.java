package com.example.ownserver;

import static com.example.ownserver.Fragment.SettingFragment.disposable;
import static com.example.ownserver.JoinActivity.checkNull;
import static com.example.ownserver.Home.apiInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ownserver.SharedPreference.SavedUserInformation;
import com.example.ownserver.databinding.LoginActivityBinding;
import com.example.ownserver.model.Data;
import com.example.ownserver.model.LoginInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private String savedId, savedPw;
    private Boolean savedState;
    private ArrayList<String> loginInfo = new ArrayList<>();
    private Map<String, String> savedUserInfo = new HashMap<>();
    private LoginActivityBinding binding;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        savedUserInfo = SavedUserInformation.getUserInformation(this);
        Log.d("INFO", savedUserInfo.toString());
        savedId = savedUserInfo.get("id");
        savedPw = savedUserInfo.get("password");
        savedState = Boolean.valueOf(savedUserInfo.get("state"));

        if(savedState){
            binding.loginId.setText(savedId);
            binding.loginPw.setText(savedPw);
            binding.saveLoginInformation.setChecked(true);
        }

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = binding.loginId.getText().toString().trim();
                String password = binding.loginPw.getText().toString().trim();

                if(checkNull(id)){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    binding.loginId.requestFocus();
                    return;
                }

                if(checkNull(password)){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    binding.loginPw.requestFocus();
                    return;
                }
                tryLogin(id, password);
            }
        });

        binding.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    private void tryLogin(String id, String password){
        loginInfo.clear();

        disposable.add(apiInterface.getLoginInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Data>() {
                    @Override
                    public void onSuccess(@NonNull Data data) {
                            String debugResponse = "";

                            for(String value: data.getData()){
                                loginInfo.add(value);
                                debugResponse += value + " ";
                            }

                            try{
                                if(loginInfo.get(0).isEmpty()){
                                    Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    binding.loginId.requestFocus();
                                    return;
                                }
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                binding.loginId.requestFocus();
                                return;
                            }

                            if(loginInfo.get(1).equals(password)){
                                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);

                                if(binding.saveLoginInformation.isChecked())
                                    SavedUserInformation.setUserInformation(getApplicationContext(), binding.loginId.getText().toString().trim(), binding.loginPw.getText().toString().trim(), "true");
                                else{
                                    SavedUserInformation.deleteUserInformation(getApplicationContext());
                                }

                                intent.putExtra("id", id);

                                finish();
                                startActivity(intent);

                            }else{
                                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                binding.loginPw.requestFocus();
                                return;
                            }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "ERROR" + e.getMessage());
                    }
                })
        );
    }

//    Retrofit Ver
//    private void tryLogin(String id, String password){
//        loginInfo.clear();
//
//        Call<Data> getLoginInfo = apiInterface.getLoginInfo(id);
//        getLoginInfo.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call, Response<Data> response) {
//                if(response.isSuccessful()){
//                    Data result = response.body();
//                    String debugResponse = "";
//
//                    for(String value: result.getData()){
//                        loginInfo.add(value);
//                        debugResponse += value + " ";
//                    }
//
//                    try{
//                        if(loginInfo.get(0).isEmpty()){
//                            Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//                            editId.requestFocus();
//                            return;
//                        }
//                    }catch (Exception e){
//                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        editId.requestFocus();
//                        return;
//                    }
//
//                    if(loginInfo.get(1).equals(password)){
//                        Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), Home.class);
//                        intent.putExtra("id", id);
//                        finish();
//                        startActivity(intent);
//
//                    }else{
//                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        editPassword.requestFocus();
//                        return;
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Data> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "에러 발생", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
