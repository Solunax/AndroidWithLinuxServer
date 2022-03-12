package com.example.ownserver;

import static com.example.ownserver.Home.disposable;
import static com.example.ownserver.JoinActivity.checkNull;
import static com.example.ownserver.Home.apiInterface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.ownserver.Room.AppDataBase;
import com.example.ownserver.Room.User;
import com.example.ownserver.Room.UserDAO;
import com.example.ownserver.databinding.LoginActivityBinding;
import com.example.ownserver.model.Data;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private ArrayList<String> loginInfo = new ArrayList<>();
    private LoginActivityBinding binding;
    private long lastTimeBackPressed;
    private AppDataBase dataBase;
    private UserDAO userDAO;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
        dataBase.close();
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataBase = Room.databaseBuilder(this, AppDataBase.class, "USER").build();
        userDAO = dataBase.userDAO();

        userDAO.getInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d("VALUE", user.toString());
                        if(user.getAutologin()){
                            binding.loginId.setText(user.getId());
                            binding.loginPw.setText(user.getPassword());
                            binding.saveLoginInformation.setChecked(user.getAutologin());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR", e.getMessage());
                    }
                });

        binding.login.setOnClickListener(v -> {
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
        });

        binding.join.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        });
    }

    private void tryLogin(String id, String password){
        loginInfo.clear();

        disposable.add(apiInterface.getLoginInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Data>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(@NonNull Data data) {
                        loginInfo.addAll(data.getData());

                        if(loginInfo.isEmpty()){
                            Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            binding.loginId.requestFocus();
                            return;
                        }

                        if(loginInfo.get(1).equals(password)){
                            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);

                            User user = new User();
                            user.setId(loginInfo.get(0));
                            user.setPassword(loginInfo.get(1));
                            user.setAutologin(binding.saveLoginInformation.isChecked());

                            userDAO.insertUserData(user).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe();

                            intent.putExtra("id", id);

                            finish();
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            binding.loginPw.requestFocus();
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

// SharedPreference
//    private String savedId, savedPw;
//    private Boolean savedState;
//        Map<String, String> savedUserInfo = SavedUserInformation.getUserInformation(this);
//        Log.d("INFO", savedUserInfo.toString());
//        savedId = savedUserInfo.get("id");
//        savedPw = savedUserInfo.get("password");
//        savedState = Boolean.valueOf(savedUserInfo.get("state"));
//
//        if(savedState){
//            binding.loginId.setText(savedId);
//            binding.loginPw.setText(savedPw);
//            binding.saveLoginInformation.setChecked(true);
//        }
//                            if(binding.saveLoginInformation.isChecked())
//                                SavedUserInformation.setUserInformation(getApplicationContext(), binding.loginId.getText().toString().trim(), binding.loginPw.getText().toString().trim(), "true");
//                            else{
//                                SavedUserInformation.deleteUserInformation(getApplicationContext());
//                            }