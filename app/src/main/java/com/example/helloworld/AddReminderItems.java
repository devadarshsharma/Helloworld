package com.example.helloworld;

import java.text.DecimalFormat;

public class AddReminderItems {
    private int mImageView;
    private String mTextViewTime;
    private String mTextViewTablets;

    public AddReminderItems(int imageResource, String TextViewTime, String TextViewTablets){
        mImageView = imageResource;
        mTextViewTime = TextViewTime;
        mTextViewTablets = TextViewTablets;
    }
    public int getImageView(){
        return mImageView;
    }

    public String getTextViewTime(){
        return mTextViewTime;
    }

    public String getTextViewTablets(){
        return mTextViewTablets;
    }

    public void changeTextTime(String Text){
        mTextViewTime = Text;
    }

    public void changeTextTablet(String Text){
        mTextViewTablets = Text;
    }

}
