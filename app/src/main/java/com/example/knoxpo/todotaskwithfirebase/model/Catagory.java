package com.example.knoxpo.todotaskwithfirebase.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by knoxpo on 25/7/17.
 */

public class Catagory {
    private String id;
    private String title;
    private String mUserId;

    public Catagory(){
    }


    public Catagory(String id, String title,String userId) {
        this.id = id;
        this.title = title;
        mUserId = userId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id" , id);
        map.put("title" , title);
        map.put("user_id",mUserId);

        return map;
    }
}
