package com.example.ownserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {
    Context userListContext;
    LayoutInflater userListLayoutInflater;
    ArrayList<String> userIdList;
    ArrayList<String> userNameList;

    public UserListAdapter(Context context, ArrayList<String> intentUserIdList, ArrayList<String> intentUserNameList){
        userListContext = context;
        userIdList = intentUserIdList;
        userNameList = intentUserNameList;
        userListLayoutInflater = LayoutInflater.from(userListContext);
    }

    @Override
    public int getCount() {
        return userIdList.size();
    }

    @Override
    public Object getItem(int position) {
        return userIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = userListLayoutInflater.inflate(R.layout.list, null);

        TextView userID = (TextView)view.findViewById(R.id.list_id);
        TextView userName = (TextView)view.findViewById(R.id.list_name);

        userID.setText("ID : " + userIdList.get(position));
        userName.setText("NAME : " + userNameList.get(position));

        return view;
    }
}
