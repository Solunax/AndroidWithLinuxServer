package com.example.ownserver.Fragment;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ownserver.ApiClient;
import com.example.ownserver.ApiInterface;
import com.example.ownserver.R;
import com.example.ownserver.UpdateUserInformation;
import com.example.ownserver.UserListAdapter;
import com.example.ownserver.databinding.SettingFragmentBinding;
import com.example.ownserver.model.Data;
import com.example.ownserver.model.HomeViewModel;
import com.example.ownserver.model.User;
import com.example.ownserver.model.UserList;
import com.example.ownserver.model.UserModel;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    private String serverBasePath = "http://192.168.56.117/userImage/";
    private ArrayList<String> userIdList = new ArrayList<>();
    private ArrayList<String> userNameList = new ArrayList<>();
    private ArrayList<String> myInfo = new ArrayList<>();
    private ListView mListView;
    private Button getInfo, toUpdate, toDelete;
    private TextView userID, userName, userAuth;
    private ImageButton img;
    private Context context;
    private HomeViewModel viewModel;
    private SettingFragmentBinding binding;
    public static ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    public static CompositeDisposable disposable = new CompositeDisposable();

    private ActivityResultLauncher<Intent> imageUpload = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d("RESULT", String.valueOf(result.getResultCode()));
                if(result.getResultCode() == RESULT_OK){
                    Intent intent= result.getData();
                    String path = getPath(intent.getData());
                    uploadImages(binding.idF.getText().toString().trim(), path);
            }
        }
    });

    @Override
    public void onAttach(@NonNull Context cont) {
        super.onAttach(cont);
        context = getContext();
        Log.d("ATTACH", "SETTING");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        Log.d("DETACH", "SETTING");
        disposable.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("DESTROY VIEW", "SETTING");
        binding = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SettingFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle bundle = getArguments();
        String loginId = bundle.getString("id");

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        viewModel.getViewModelList();

        loadUserInfo(loginId);

        binding.getInfoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserList();
            }
        });

        binding.toUpdateUserInfoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateUserInformation.class);
                startActivity(intent);
            }
        });

        binding.deleteInfoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });

        binding.profileImageF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                imageUpload.launch(intent);
            }
        });

        return view;
    }

//RxJava 사용버전
    private void loadUserInfo(String id){
        disposable.add(apiInterface.getMyInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Data>() {
                    @Override
                    public void onSuccess(@NonNull Data data) {
                        Log.d("SUCCESS!/USER INFO", "SUCCESS!");
                        String debugResponse = "";

                        for(String value: data.getData()){
                            myInfo.add(value);
                            debugResponse += value + " ";
                        }
                        Log.d("VALUE", debugResponse);

                        String auth;
                        if(myInfo.get(2).equals("A"))
                            auth = "관리자";
                        else
                            auth = "유저";

                        UserModel user = new UserModel(myInfo.get(0), myInfo.get(1), auth);
                        binding.setUser(user);

                        if(myInfo.get(3) != null)
                            Glide.with(context).load(myInfo.get(3)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(binding.profileImageF);
                        else
                            Glide.with(context).load(R.drawable.ic_launcher_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(binding.profileImageF);
                        Log.d("SETTING", "SET VIEW MODEL");
                        viewModel.setInfoList(myInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, "에러 발생!", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "ERROR" + e.getMessage());
                    }
                })
        );
    }

    private void getUserList(){
        userIdList.clear();
        userNameList.clear();

        disposable.add(apiInterface.getUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserList>() {
                    @Override
                    public void onSuccess(@NonNull UserList userList) {
                        Log.d("SUCCESS!/USER LIST", "SUCCESS!");

                        String debugResponse = "";

                        for(UserList.Users user : userList.data){
                            userIdList.add(user.id);
                            userNameList.add(user.name);
                            debugResponse += "ID : " + user.id + "  NAME : " + user.name + ", ";
                        }
                        Log.d("RESULT/USER LIST", debugResponse);

                        UserListAdapter userListAdapter = new UserListAdapter(context, userIdList, userNameList);
                        userListAdapter.notifyDataSetChanged();
                        binding.infoListF.setAdapter(userListAdapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, "에러 발생!", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "ERROR" + e.getMessage());
                    }
                })
        );
    }

    private void uploadImages(String id, String imageFile) {
        File file = new File(imageFile);
        String serverPath = serverBasePath + id;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", id, requestFile);

        disposable.add(apiInterface.uploadImage(body, id, serverPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Data>() {
                    @Override
                    public void onSuccess(@NonNull Data data) {
                        Log.d("SUCCESS", "SUCCESS");
                        Toast.makeText(context, "업로드에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        String url = serverBasePath + id;

                        Glide.with(context).load(url).circleCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.profileImageF);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(context, "에러 발생!", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", "ERROR" + e.getMessage());
                    }
                })
        );
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

                disposable.add(apiInterface.deleteUser(deleteIdValue)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<UserList>() {
                            @Override
                            public void onSuccess(@NonNull UserList userList) {
                                Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                                Log.d("SUCCESS!/ DELETE", "SUCCESS!");
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                                Log.d("ERROR", "ERROR" + e.getMessage());
                            }
                        })
                );

//                Delete User Method Retrofit Ver
//                Call<Void> deleteUser = apiInterface.deleteUser(deleteIdValue);
//                deleteUser.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if(response.isSuccessful()){
//                            Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
//                            Log.d("SUCCESS!/ DELETE", "SUCCESS!");
//                            dialog.dismiss();
//                        }else{
//                            Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
//                            Log.d("ERROR", "ERROR" + response.code());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
//                        Log.d("FAIL", t.getMessage());
//                    }
//                });
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

    private String getPath(Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();

        return path;
    }


// 기존 Retrofit 비동기 처리
//    private void loadUserInfo(String id){
//        Call<Data> getMyInfo = apiInterface.getMyInfo(id);
//        getMyInfo.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call, Response<Data> response) {
//                if(response.isSuccessful()){
//                    Data result = response.body();
//                    String debugResponse = "";
//
//                    for(String value: result.getData()){
//                        myInfo.add(value);
//                        debugResponse += value + " ";
//                    }
//                    Log.d("VALUE", debugResponse);
//
//                    userID.setText(myInfo.get(0));
//                    userName.setText(myInfo.get(1));
//
//                    if(myInfo.get(2).equals("A"))
//                        userAuth.setText("관리자");
//                    else
//                        userAuth.setText("유저");
//
//                    if(myInfo.get(3) != null)
//                        Glide.with(context).load(myInfo.get(3)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(img);
//                    else
//                        Glide.with(context).load(R.drawable.ic_launcher_background).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().into(img);
//                }else{
//                    Toast.makeText(context, "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Data> call, Throwable t) {
//                Log.d("ERROR", t.getMessage());
//            }
//        });
//    }
//
//    유저 목록 불러오기
//    private void getUserList(){
//        userIdList.clear();
//        userNameList.clear();
//
//        Call<UserList> getListInfo = apiInterface.getUserList();
//        getListInfo.enqueue(new Callback<UserList>() {
//            @Override
//            public void onResponse(Call<UserList> call, Response<UserList> response) {
//                if(response.isSuccessful()){
//                    Log.d("SUCCESS!/USER LIST", "SUCCESS!");
//                    UserList resultList = response.body();
//                    List<UserList.Users> usersList = resultList.data;
//                    String debugResponse = "";
//
//                    for(UserList.Users user : usersList){
//                        userIdList.add(user.id);
//                        userNameList.add(user.name);
//                        debugResponse += "ID : " + user.id + "  NAME : " + user.name + ", ";
//                    }
//                    Log.d("RESULT/USER LIST", debugResponse);
//
//                    UserListAdapter userListAdapter = new UserListAdapter(context, userIdList, userNameList);
//                    userListAdapter.notifyDataSetChanged();
//                    mListView.setAdapter(userListAdapter);
//                }else{
//                    Toast.makeText(context, "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserList> call, Throwable t) {
//                Toast.makeText(context, "불러오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                Log.d("FAIL", t.getMessage());
//            }
//        });
//
//    }
//
//    프로필 이미지 업로드
//    private void uploadImages(String id, String imageFile){
//        File file = new File(imageFile);
//        String serverPath = serverBasePath + id;
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", id, requestFile);
//
//        Call<Void> upload = apiInterface.uploadImage(body, id, serverPath);
//        upload.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.isSuccessful()){
//                    Log.d("SUCCESS", "SUCCESS");
//                    Toast.makeText(context, "업로드에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                    String url = serverBasePath + id;
//
//                    Glide.with(context).load(url).circleCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(img);
//                }else{
//                    Toast.makeText(context, "에러 발생!", Toast.LENGTH_SHORT).show();
//                    Log.d("ERROR", "ERROR" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                Log.d("FAIL", t.getMessage());
//            }
//        });
//    }
}