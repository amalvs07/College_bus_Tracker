package com.example.pro.fragmentadmin.datas;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DriverData {
    private String  busno;
    private String email;
    private String fullname;
    private String phoneno;



    private String username;


    public DriverData(String busno, String email, String fullname, String phoneno,String username) {
        this.busno = busno;
        this.email = email;
        this.fullname = fullname;
        this.phoneno = phoneno;
        this.username = username;
    }

    public String getBusno() {
        return busno;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhoneno() {
        return phoneno;
    }
    public String getUsername() {
        return username;
    }
    DriverData(){

    }






}
