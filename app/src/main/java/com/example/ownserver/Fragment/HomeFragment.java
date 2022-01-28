package com.example.ownserver.Fragment;

import static com.example.ownserver.Fragment.SettingFragment.apiInterface;
import static com.example.ownserver.Fragment.SettingFragment.disposable;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ownserver.R;
import com.example.ownserver.model.Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment {
    private TextView name, serverApi, phpApi, buildDate;
    private String URL = "http://192.168.56.117/serverInfo.php";
    private Context context;
    private HashMap<String, String> infoValues = new HashMap<>();

    @Override
    public void onAttach(@NonNull Context ct) {
        super.onAttach(ct);
        context = getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    name.setText(infoValues.get("name"));
                    buildDate.setText(infoValues.get("date"));
                    serverApi.setText(infoValues.get("serverAPI"));
                    phpApi.setText(infoValues.get("phpAPI"));
                    break;
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        name = (TextView)view.findViewById(R.id.system_name);
        serverApi = (TextView)view.findViewById(R.id.server_api);
        phpApi = (TextView)view.findViewById(R.id.php_api);
        buildDate = (TextView)view.findViewById(R.id.build_date);

        getServerInformation();

        return view;
    }

    private void getServerInformation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = handler.obtainMessage();
                    Document document = Jsoup.connect(URL).get();
                    Elements values = document.select(".v");

                    Log.d("Value", values.get(0).text());
                    Log.d("Value", values.get(1).text());
                    Log.d("Value", values.get(2).text());
                    Log.d("Value", values.get(8).text());
                    String nameValue = values.get(0).text();
                    nameValue = nameValue.substring(0, nameValue.indexOf(" 4"));
                    infoValues.put("name", nameValue);
                    infoValues.put("date", values.get(1).text());
                    infoValues.put("serverAPI", values.get(2).text());
                    infoValues.put("phpAPI", values.get(8).text());

                    message.what = 1;
                    handler.sendMessage(message);
                    Log.d("THREAD", "FINISH");
                } catch (IOException e) {
                    Log.d("ERROR", e.getMessage());
                }
            }
        }).start();
    }
}
