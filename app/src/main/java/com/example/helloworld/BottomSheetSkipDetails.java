package com.example.helloworld;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class BottomSheetSkipDetails extends BottomSheetDialogFragment {

    private bottomSheetListenerRB mlistener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheetskip, container, false);

        RadioButton Mednotnear = v.findViewById(R.id.Mednotnear);
        RadioButton Forgot = v.findViewById(R.id.Forgot);
        RadioButton ranoutofmed = v.findViewById(R.id.ranoutofmed);
        RadioButton Dontneed = v.findViewById(R.id.Dontneed);
        RadioButton sideeffect = v.findViewById(R.id.sideeffect);
        RadioButton cost = v.findViewById(R.id.cost);
        RadioButton other = v.findViewById(R.id.other);

        Mednotnear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Med isn't near me");
                dismiss();
            }
        });

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Forgot / busy / asleep");
                dismiss();
            }
        });

        ranoutofmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Ran out of med");
                dismiss();
            }
        });

        Dontneed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Don't need to take this dose");
                dismiss();
            }
        });

        sideeffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Side effects / other health concerns");
                dismiss();
            }
        });

        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Worried about the cost");
                dismiss();
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRadioButtonClicked("Others");
                dismiss();
            }
        });


        return v;

    }

    public interface bottomSheetListenerRB{
        void onRadioButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mlistener = (bottomSheetListenerRB) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                        + "must implement Bottomsheetlistner");
        }
    }
}
