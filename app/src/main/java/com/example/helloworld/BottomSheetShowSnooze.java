package com.example.helloworld;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BottomSheetShowSnooze extends BottomSheetDialogFragment {

    private bottomSheetListenerSnooze mlistener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheetsnoozed, container, false);

        Button five = v.findViewById(R.id.five);
        Button ten = v.findViewById(R.id.ten);
        Button thirty = v.findViewById(R.id.thirty);
        Button fortyfive = v.findViewById(R.id.fortyfive);
        Button sixty = v.findViewById(R.id.sixty);
        Button onetwenty = v.findViewById(R.id.onetwenty);

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked(5);
                dismiss();
            }
        });

        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked(10);
                dismiss();
            }
        });

        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked(30);
                dismiss();
            }
        });

        fortyfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked(45);
                dismiss();
            }
        });

        sixty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked(60);
                dismiss();
            }
        });

        onetwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked(120);
                dismiss();
            }
        });

        return v;
    }

    public interface bottomSheetListenerSnooze{
        void onButtonClicked(int time);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mlistener = (bottomSheetListenerSnooze) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "must implement Bottomsheetlistner");
        }
    }
}
