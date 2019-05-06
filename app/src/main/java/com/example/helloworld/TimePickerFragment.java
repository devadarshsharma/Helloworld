package com.example.helloworld;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.app.Dialog;
import java.util.Calendar;
import java.util.TreeMap;

import android.widget.TimePicker;
import android.widget.Toast;



public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "MyTimeDialog";

    public interface onTimePickerReadListener{
         void onTimeRead(String message,int hourOfDay, int minute );
    }

    private onTimePickerReadListener monTimePickerReadListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog

        //android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog_Alert );

        TimePickerDialog datepickerdialog = new TimePickerDialog(getActivity(),
                                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                                this,hour,minute,false);

        datepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return datepickerdialog;
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        String Time;

        if(hourOfDay > 12){
            int hour = hourOfDay - 12;
            Time = String.valueOf(hour) + ":" + String.valueOf(minute) +" PM";
        }else if (hourOfDay == 0){
            Time = "12:" + String.valueOf(minute) +" AM";
        }else if(hourOfDay == 12)
            Time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) +" PM";
        else
            Time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) +" AM";


        Log.d("Timepicker","Hour : " + String.valueOf(hourOfDay)
                + "\nMinute : " + String.valueOf(minute) );

        monTimePickerReadListener.onTimeRead(Time, hourOfDay, minute);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            monTimePickerReadListener = (onTimePickerReadListener) getActivity();

        }catch (ClassCastException e){
            Log.e(TAG,"onAttach: ClassException: " + e.getMessage());
        }
    }
}