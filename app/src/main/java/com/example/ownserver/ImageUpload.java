package com.example.ownserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.File;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUpload extends Activity {
    private String serverBasePath = "http://192.168.56.117/userImage/";
    private Context context = this;
    private Button upload, getImage;
    private EditText id;
    private ImageView img;
    private ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    private final int code = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);

        id = (EditText)findViewById(R.id.imageUploadUserID);

        upload = (Button)findViewById(R.id.uploadImage);
        getImage = (Button)findViewById(R.id.getImage);

        img = (ImageView)findViewById(R.id.image);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().trim().length() == 0){
                    Toast.makeText(context, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, code);
            }
        });

        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().trim().length() == 0){
                    Toast.makeText(context, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = serverBasePath + id.getText().toString().trim();
                Log.d("URL", url);
                Glide.with(context).load(url).into(img);
            }
        });
    }

    private void uploadImages(String id, String imageFile){
        File file = new File(imageFile);
        String serverPath = serverBasePath + id;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", id, requestFile);

        Call<Void> upload = apiInterface.uploadImage(body, id, serverPath);
        upload.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("SUCCESS", "SUCCESS");
                    Toast.makeText(context, "업로드에 성공했습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "에러 발생!", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", "ERROR" + response.code());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("FAIL", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == code){
            String uploadId = id.getText().toString().trim();
            String path = getPath(data.getData());
            uploadImages(uploadId, path);
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
