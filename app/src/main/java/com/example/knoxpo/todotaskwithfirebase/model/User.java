package com.example.knoxpo.todotaskwithfirebase.model;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by knoxpo on 28/7/17.
 */

public class User {
    private String mUserId;
    private String mUserName;
    private String mUserEmail;
    private Context mContext;
    private static User sUser;

    private User(Context context,String id,String name, String email) {
        mContext = context.getApplicationContext();
        mUserId = id;
        mUserName = name;
        mUserEmail = email;
    }

    public static User getInstance(Context context,String id,String name, String email ){
        if(sUser == null){
            sUser = new User(context,id,name,email);
        }
        return sUser;
    }

    public static User getInstance(){
        if(sUser != null){
            return sUser;
        }else{
            return null;
        }
    }

    public static User unLoadUser(){
        sUser = null;
        return sUser;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();

        map.put("user_id" , mUserId);
        map.put("user_name", mUserName);
        map.put("email" , mUserEmail);
        return map;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }
}
