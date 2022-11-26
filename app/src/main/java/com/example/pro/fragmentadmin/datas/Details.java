package com.example.pro.fragmentadmin.datas;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Mahadi on 3/11/2018.
 */
@IgnoreExtraProperties
public class Details {

    private String username;
    private String message;


     Details() {

    }



    public Details(String username, String message) {
        this.username = username;
        this.message = message;

    }
    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

}
