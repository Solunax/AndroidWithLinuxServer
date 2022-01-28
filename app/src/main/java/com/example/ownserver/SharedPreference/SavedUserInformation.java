package com.example.ownserver.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SavedUserInformation {
    static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //정보 기입
    public static void setUserInformation(Context context, String id, String pw, String state){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("username", id);
        editor.putString("password", pw);
        editor.putString("saveState", state);
        editor.commit();
    }

    //정보 불러오기
    public static HashMap<String, String> getUserInformation(Context context){
        HashMap<String, String> savedUserInfo = new HashMap<>();
        savedUserInfo.put("id", getSharedPreferences(context).getString("username", ""));
        savedUserInfo.put("password", getSharedPreferences(context).getString("password", ""));
        savedUserInfo.put("state", getSharedPreferences(context).getString("saveState", "false"));
        return savedUserInfo;
    }

    //자동 로그인 체크 해제시
    public static void deleteUserInformation(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
