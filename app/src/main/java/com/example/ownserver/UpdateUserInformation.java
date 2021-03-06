package com.example.ownserver;

import static com.example.ownserver.Home.disposable;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ownserver.databinding.UpdateUserInformationBinding;
import com.example.ownserver.model.UserList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateUserInformation extends Activity {
    private ArrayList<String> userIdList;
    private UpdateUserInformationBinding binding;
    private final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UpdateUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userIdList = new ArrayList<>();

        binding.updateUserID.setOnClickListener(view -> updateUserInfo());
    }
    private void updateUserInfo(){
        userIdList.clear();
        if(binding.updateUserID.getText().toString().length() == 0){
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if(binding.updateUserName.getText().toString().length() < 2){
            Toast.makeText(this, "변경할 이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        disposable.add(apiInterface.updateUser(binding.updateUserID.getText().toString().trim(), binding.updateUserName.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserList>() {
                    @Override
                    public void onSuccess(@NonNull UserList userList) {
                            List<UserList.Users> usersList = userList.data;

                            for(UserList.Users user : usersList)
                                userIdList.add(user.id);

                            if(userIdList.get(0) != null){
                                Toast.makeText(getApplicationContext(), "성공적으로 변경했습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
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
//    private void updateUserInfo(){
//        userIdList.clear();
//        if(id.getText().toString().length() == 0){
//            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(name.getText().toString().length() < 2){
//            Toast.makeText(this, "변경할 이름을 입력하세요", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Call<UserList> updateUser = apiInterface.updateUser(id.getText().toString(), name.getText().toString());
//        updateUser.enqueue(new Callback<UserList>() {
//            @Override
//            public void onResponse(Call<UserList> call, Response<UserList> response) {
//                if(response.isSuccessful()){
//                    UserList resultList = response.body();
//                    List<UserList.Users> usersList = resultList.data;
//
//                    for(UserList.Users user : usersList)
//                        userIdList.add(user.id);
//
//                    if(userIdList.get(0) != null){
//                        Toast.makeText(getApplicationContext(), "성공적으로 변경했습니다.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }else{
//                        Toast.makeText(getApplicationContext(), "추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<UserList> call, Throwable t) {
//                Log.d("ERROR", t.getMessage());
//            }
//        });
//    }