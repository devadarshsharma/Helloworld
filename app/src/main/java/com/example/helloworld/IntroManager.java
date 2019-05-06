package com.example.helloworld;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public  IntroManager (Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("first", 0);
        editor = sharedPreferences.edit();
    }



    public void setFirst(boolean isFirst){
        editor.putBoolean("check", isFirst);
        editor.commit();
    }

    public  boolean Check(){
        return sharedPreferences.getBoolean("check", true);
    }

    public void setPendingIntent(int intentCode){
        editor.putInt("intentcode", intentCode);
        editor.commit();
    }

    public int GetPendintIntent(){return sharedPreferences.getInt("intentcode", 0);}
}
