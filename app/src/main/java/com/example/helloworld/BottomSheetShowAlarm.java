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
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class BottomSheetShowAlarm extends BottomSheetDialogFragment {

    private bottomSheetListener mlistener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomesheetconfirm, container, false);

        ImageButton btn_skip = v.findViewById(R.id.btn_skip);
        ImageButton confirm = v.findViewById(R.id.btn_confirm);
        ImageButton snooze = v.findViewById(R.id.btn_snooze);
        ImageButton edit = v.findViewById(R.id.img1);
        ImageButton delete = v.findViewById(R.id.img2);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked("Confirm");
                dismiss();
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked("Skip");
                dismiss();
            }
        });

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked("Snoozed");
                dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked("Edit");
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onButtonClicked("Delete");
                dismiss();
            }
        });


        return v;
    }

    public interface bottomSheetListener{
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mlistener = (bottomSheetListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement BottomSheetListener");
        }
    }
}
