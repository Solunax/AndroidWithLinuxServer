package com.example.ownserver.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ownserver.databinding.HomeFragmentBinding;
import com.example.ownserver.model.ParsingModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private String URL = "http://192.168.56.117/serverInfo.php";
    private Context context;
    private HomeFragmentBinding binding;
    private HashMap<String, String> infoValues = new HashMap<>();

    @Override
    public void onAttach(@NonNull Context ct) {
        super.onAttach(ct);
        context = getContext();
        Log.d("ATTACH", "HOME");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        Log.d("DETACH", "HOME");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("DESTROY VIEW", "HOME");
        binding = null;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    ParsingModel data = new ParsingModel(infoValues.get("name"), infoValues.get("date"), infoValues.get("serverAPI"), infoValues.get("phpAPI"));
                    binding.setData(data);
                    break;
            }
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
