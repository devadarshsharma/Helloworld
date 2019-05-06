package com.example.helloworld;

public class ShowAlarmItems {
    private String time;
    private String medname;
    private String tablet;

    public ShowAlarmItems(String mtime, String mMedname, String mTablet){
        time = mtime;
        medname = mMedname;
        tablet = mTablet;
    }

    public String getTime(){return time;}
    public String getMedname(){return medname;}
    public String getTablet(){return tablet;}

}
