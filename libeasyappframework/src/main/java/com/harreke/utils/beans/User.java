package com.harreke.utils.beans;

public class User {
    public User() {
        userImg = "/Misc/avatar";
        userId = 0;
        username = "";
    }

    private int userId;
    private String userImg;
    private String username;

    public final int getUserId() {
        return userId;
    }

    public final String getUserImg() {
        if (userImg == null) {
            userImg = "/Misc/avatar";
        }

        return userImg;
    }

    public final String getUsername() {
        return username;
    }
}