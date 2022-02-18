package com.example.ownserver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.ownserver.Home.disposable;
import static com.example.ownserver.Home.apiInterface;

import com.example.ownserver.databinding.JoinActivityBinding;
import com.example.ownserver.model.Data;
import com.example.ownserver.model.UserList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class JoinActivity extends Activity {
    private ArrayList<String>idList= new ArrayList<>();
    private JoinActivityBinding binding;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = JoinActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.idCheck.setOnClickListener(view -> idChecking(binding.joinUserID.getText().toString().trim()));

        binding.joinComplete.setOnClickListener(view -> {
            String id = binding.joinUserID.getText().toString().trim();
            String password = binding.joinUserPW.getText().toString().trim();
            String name = binding.joinUserName.getText().toString().trim();

            if(checkNull(id)){
                Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                binding.joinUserID.requestFocus();
                return;
            }

            if(checkNull(password)){
                Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                binding.joinUserPW.requestFocus();
                return;
            }

            if(checkNull(name)){
                Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                binding.joinUserName.requestFocus();
                return;
            }

            joinUser(id, password, name);
        });
    }

    private void idChecking(String id){
        if(checkNull(id)){
            Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
            binding.joinUserID.requestFocus();
            return;
        }

        idList.clear();

        disposable.add(apiInterface.getUserIDs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<Data>() {
               @Override
               public void onSuccess(@NonNull Data data) {

                   idList.addAll(data.getData());

                   Log.d("ID LIST", idList.toString());

                   if(idList.contains(id))
                       Toast.makeText(getApplicationContext(), "중복된 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                   else{
                       Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                       binding.idCheck.setEnabled(false);
                       binding.joinComplete.setEnabled(true);
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

    private void joinUser(String id, String pw, String name){
        disposable.add(apiInterface.insertUser(id, pw, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserList>() {
                    @Override
                    public void onSuccess(@NonNull UserList userList) {
                        Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "ERROR" + e.getMessage());
                    }
                })
        );
    }

    public static Boolean checkNull(String value){
        return value.isEmpty();
    }
}

//    Retrofit Ver
//    아이디 중복검사
//    private void idChecking(String id){
//        if(checkNull(id)){
//            Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
//            editId.requestFocus();
//            return;
//        }
//
//        idList.clear();
//
//        Call<Data> getIDList = apiInterface.getUserIDs();
//        getIDList.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call, Response<Data> response) {
//                if(response.isSuccessful()){
//                    Data result = response.body();
//                    String debugResponse = "";
//
//                    for(String value: result.getData()){
//                        idList.add(value);
//                        debugResponse += value + " ";
//                    }
//                    Log.d("ID LIST", idList.toString());
//                    Log.d("ID", debugResponse);
//
//                    if(idList.contains(id))
//                        Toast.makeText(getApplicationContext(), "중복된 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
//                    else{
//                        Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
//                        check.setEnabled(false);
//                        join.setEnabled(true);
//                    }
//                }else{
//                    Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Data> call, Throwable t) {
//                Log.d("ERROR", t.getMessage());
//            }
//        });
//    }
//    회원가입
//    private void joinUser(String id, String pw, String name){
//        Call<UserList> joinNew = apiInterface.insertUser(id, pw, name);
//        joinNew.enqueue(new Callback<UserList>() {
//            @Override
//            public void onResponse(Call<UserList> call, Response<UserList> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }else{
//                    Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserList> call, Throwable t) {
//                Log.d("FAIL", t.getMessage());
//            }
//        });
//    }