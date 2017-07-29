package com.example.knoxpo.todotaskwithfirebase.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by knoxpo on 25/7/17.
 */

public class Task {
    private String mTaskId;
    private String mCatagoryId;
    private String mTask;


    public Task(){}

    public Task(String id,String catagoryId,String task){
        mTaskId = id;
        mCatagoryId = catagoryId;
        mTask = task;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();

        map.put("id" , mTaskId);
        map.put("catagory" , mCatagoryId);
        map.put("tasks" , mTask);
        return map;
    }

    public String getmTaskId() {
        return mTaskId;
    }

    public String getmCatagoryId() {
        return mCatagoryId;
    }

    public String getmTask() {
        return mTask;
    }
}
