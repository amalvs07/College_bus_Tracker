package com.example.pro.fragmentadmin.datas;



import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DemoData {


    private String busno;
    private String busplate;
    private String routeid;
    private String routename;
    DemoData(){

    }

    public DemoData(String busno,String busplate, String routeid, String routename) {
        this.busno=busno;
        this.busplate = busplate;
        this.routeid = routeid;
        this.routename = routename;
    }
    public String getBusno() {
        return busno;
    }
    public String getBusplate() {
        return busplate;
    }

    public String getRouteid() {
        return routeid;
    }

    public String getRoutename() {
        return routename;
    }
}
