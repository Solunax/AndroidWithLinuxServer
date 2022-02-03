package com.example.ownserver.model;

public class UserModel{
    private String userId;
    private String userName;
    private String userAuth;

    public UserModel(String userId, String userName, String userAuth) {
        this.userId = userId;
        this.userName = userName;
        this.userAuth = userAuth;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }
}
