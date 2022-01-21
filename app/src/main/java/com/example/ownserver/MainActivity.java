package com.example.ownserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ownserver.model.Data;
import com.example.ownserver.model.LoginInfo;
import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String serverBasePath = "http://192.168.56.117/userImage/";
    private ArrayList<String> userIdList = new ArrayList<>();
    private ArrayList<String> userNameList = new ArrayList<>();
    private ArrayList<String> myInfo = new ArrayList<>();
    private ListView mListView;
    private Button getInfo, toUpdate, toDelete, toUpload;
    private TextView userID, userName, userAuth;
    private ImageButton img;
    private Context context = this;
    private final int code = 1;
    private Handler handler;
    public static ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent beforeData = getIntent();
        String loginId = beforeData.getStringExtra("id");

        loadUserInfo(loginId);

        mListView = (ListView)findViewById(R.id.infoList);

        getInfo = (Button)findViewById(R.id.getInfo);
        toUpdate = (Button)findViewById(R.id.toUpdateUserInfo);
        toDelete = (Button)findViewById(R.id.deleteInfo);
        toUpload = (Button)findViewById(R.id.toUpload);
        img= (ImageButton)findViewById(R.id.profile_image);

        userID = (TextView)findViewById(R.id.id);
        userName = (TextView)findViewById(R.id.name);
        userAuth = (TextView)findViewById(R.id.auth);

        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserList();
            }
        });

        toUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateUserInformation.class);
                startActivity(intent);
            }
        });

        toDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });

        toUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageUpload.class);
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, code);
            }
        });
    }

    private void loadUserInfo(String id){
        Call<Data> getMyInfo = apiInterface.getMyInfo(id);
        getMyInfo.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data result = response.body();
                String debugResponse = "";

                for(String value: result.getData()){
                    myInfo.add(value);
                    debugResponse += value + " ";
                }
                Log.d("VALUE", debugResponse);

                userID.setText(myInfo.get(0));
                userName.setText(myInfo.get(1));
                if(myInfo.get(2).equals("A"))
                    userAuth.setText("관리자");
                else
                    userAuth.setText("유저");

                if(myInfo.get(3) != null)
                    Glide.with(context).load(myInfo.get(3)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(img);
                else
                    Glide.with(context).load(R.drawable.ic_launcher_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(img);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
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
                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
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

    private void getUserList(){
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
                    Log.d("RESULT/USER LIST", debugResponse);

                    UserListAdapter userListAdapter = new UserListAdapter(MainActivity.this, userIdList, userNameList);
                    userListAdapter.notifyDataSetChanged();
                    mListView.setAdapter(userListAdapter);
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

    private void uploadImages(String id, String imageFile){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(imageFile);
                String serverPath = serverBasePath + id;
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", id, requestFile);

                Call<Void> upload = apiInterface.uploadImage(body, id, serverPath);
                upload.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("SUCCESS", "SUCCESS");
                        Toast.makeText(context, "업로드에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        String url = serverBasePath + id;

                        Glide.with(context).load(url).circleCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(img);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("FAIL", t.getMessage());
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == code){
            String path = getPath(data.getData());
            uploadImages(userID.getText().toString().trim(), path);
        }
    }

    private String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();

        return path;
    }
}