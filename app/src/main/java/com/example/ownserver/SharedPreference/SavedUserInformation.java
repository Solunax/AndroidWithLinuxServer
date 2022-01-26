package com.example.ownserver.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SavedUserInformation {
    static final String SAVED_USER_ID = "username";
    static final String SAVED_USER_PW = "password";
    static final String SAVED_LOGIN_STATE = "saveState";

    static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    //정보 기입
    public static void setUserInformation(Context context, String id, String pw, String state){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SAVED_USER_ID, id);
        editor.putString(SAVED_USER_PW, pw);
        editor.putString(SAVED_LOGIN_STATE, state);
        editor.commit();
    }

    //정보 불러오기
    public static HashMap<String, String> getUserInformation(Context context){
        HashMap<String, String> savedUserInfo = new HashMap<>();
        savedUserInfo.put("id", getSharedPreferences(context).getString(SAVED_USER_ID, ""));
        savedUserInfo.put("password", getSharedPreferences(context).getString(SAVED_USER_PW, ""));
        savedUserInfo.put("state", getSharedPreferences(context).getString(SAVED_LOGIN_STATE, "false"));
        return savedUserInfo;
    }

    //자동 로그인 체크 해제시
    public static void deleteUserInformation(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
