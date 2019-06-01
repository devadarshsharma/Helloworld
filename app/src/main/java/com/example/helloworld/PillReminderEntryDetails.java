package com.example.helloworld;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PillReminderEntryDetails extends AppCompatActivity implements  TimePickerFragment.onTimePickerReadListener{

    LinearLayout Time;
    private RecyclerView recyclerView;
    private AddReminderAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<AddReminderItems> reminderItems;
    String textTime,tab;
    int position,decision, time_position, tab_position;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private LinearLayout durationLL, frequencyLL, dosageLL,FirstIntake, Frequency, linearlayoutmeds;
    private TextView firstintakeinhours, MedName,UnitName, DurationDet, FrequncyDet, DosageDetail;
    private Spinner reminderSpinner, lastintakeSpinner;
    private int xtimesaday,everyday, xdays, xhours, specificdays, comp, name, mealdecison;
    private int mon,tues, wed, thurs, fri,sat,sun, years, months, days;
    private String startdate, duration, units, company, medname, customText, intakeadvise,remindSpinnerinString;
    private int hour, minut;
    private Button save;
    private IntroManager introManager;
    private LinearLayout timeRecyclerview;
    List<Integer> hoursArray ;
    List<Integer> minuteArray;
    List<String> timeinString;
    List<Integer> pendingIntents;
    List<Integer> tabletArray;
    List<String> listOfSpecificDays;
    List<Long> listOfDatesInMillis;
    private SQLiteDatabase mDatabase;
    int requestCode;
    String tabvalue = "1";
    long master_id = 0;
    long mastID = 0;
    long mastIDs = 0;
    int isBefore, tabCounter, pendingIntentCounter =  0;

    Calendar detailCalendar = Calendar.getInstance();
    int [] interval = {1,2,3,4,6,8,12};
    private static final String TAG = "PillReminderEntryDetail";
    private int fromWhere = 0;
    private int idForEdit = 0;

    int specificDaycounter = 0;
    PHDbHelper phDbHelper;

    Long MasterID;
    String masterMedName = "";
    String masterCompany = "";
    String masterIntakeAdvise = "";
    String masterStartDate = "";
    int masterDuration = 0;
    int masterEveryday = 0;
    int masterXdays = 0;
    int masterXhours = 0;
    String masterFirstIntake = "";
    int masterActive = 0;

    String detailTime = "";
    long detailTimeFormatted  = 0;
    int detailTablets = 0;
    String detailTabUnits= "";

    Intent getstuff = getIntent();
    View mview = null;
    LinearLayout weekdays = null;



    static PillReminderEntryDetails instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminder_entry_details);
        reminderItems = new ArrayList<>();
        Time = (LinearLayout) findViewById(R.id.Time);
        durationLL = (LinearLayout) findViewById(R.id.DurationDetails) ;
        frequencyLL = (LinearLayout) findViewById(R.id.FrequencyofIntake);
        dosageLL = (LinearLayout) findViewById(R.id.FrequencyofDosage);
        FirstIntake = (LinearLayout) findViewById(R.id.FirstIntake);
        firstintakeinhours = (TextView) findViewById(R.id.firstintakeinhours);
        reminderSpinner = (Spinner) findViewById(R.id.remindSpinner);
        //lastintakeSpinner = (Spinner) findViewById(R.id.lastintakeSpinner);
        Frequency = (LinearLayout) findViewById(R.id.Frequency);
        MedName = (TextView) findViewById(R.id.MedName);
        UnitName = (TextView) findViewById(R.id.UnitName);
        DurationDet = (TextView)findViewById(R.id.DurationDet);
        FrequncyDet = (TextView) findViewById(R.id.FrequncyDet);
        DosageDetail = (TextView) findViewById(R.id.DosageDetail);
        linearlayoutmeds = (LinearLayout) findViewById(R.id.linearlayoutmeds);
        timeRecyclerview = findViewById(R.id.ReminderTimes);
        save = (Button) findViewById(R.id.SavereminderDetails);
        startdate = "Today";
        introManager = new IntroManager(this);
        hoursArray = new ArrayList<>();
        minuteArray = new ArrayList<>();
        timeinString = new ArrayList<>();
        pendingIntents = new ArrayList<>();
        tabletArray = new ArrayList<>();
        listOfSpecificDays = new ArrayList<>();
        listOfDatesInMillis = new ArrayList<>();
        PHDbHelper phDbHelper = new PHDbHelper(this);
        mDatabase = phDbHelper.getWritableDatabase();
        duration = "";
        instance = this;
        decision = 0;
        tab = "1";
        everyday = 1;
        xdays=0;

        getstuff = getIntent();
        MedName.setText(getstuff.getStringExtra("MedName"));
        units = getstuff.getStringExtra("Units");
        UnitName.setText(units);
        String lidForEdit = getstuff.getStringExtra("IDforEdit");



        ArrayAdapter<CharSequence> adapter;

        phDbHelper = new PHDbHelper(this);

        buildRecyclerView();

        Intent i = new Intent(getBaseContext(), AddReminderAdapter.class);
        startService(i);

        adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(adapter);
        reminderSpinner.setSelection(0);
        Frequency.setVisibility(View.GONE);

        reminderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemIdAtPosition(position)+ " selected", Toast.LENGTH_SHORT).show();
                remindSpinnerinString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(getstuff.getStringExtra("FromWhere").equals("Edit")){
            fromWhere = 1;
            letsEdit(Integer.parseInt(lidForEdit));
        }
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openTimePicker();
            }
        });
        durationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDurationDialog();
            }
        });
        frequencyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFrequencyDialog();
            }
        });
        FirstIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
                decision = 2;
            }
        });
        dosageLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTabletDialog("Frequency");
            }
        });

        linearlayoutmeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMedDetails();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fromWhere == 1)
                    EditTheTabs();

                if(xhours == 1){
                    calculateIntervalTimes();
                    setAlaram();
                }else if (specificdays == 1){
                    setAlarmForSpecificDays();
                    setAlaram();
                }else{
                    setAlaram();
                }
                Intent intentPill = new Intent(PillReminderEntryDetails.this, PillReminderBottNav.class);
                startActivity(intentPill);
            }
        });
    }

    @Override
    public void onTimeRead(String message, int hourOfDay, int minute) {
        textTime = message;
        hour = hourOfDay;
        minut = minute;

        position = reminderItems.size();
        Toast.makeText(PillReminderEntryDetails.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
        ReminderTimesChangeOrInsert(hourOfDay, minute, message, 1);

    }

    public void setHoursAndMinutesInArray(int hour, int minute, String timeinStrings){
        hoursArray.add(hour);
        minuteArray.add(minute);
        timeinString.add(timeinStrings);

    }

    public void showToast() {
        openTimePicker();
        decision = 1;
    }

    public void openMedDetails(){
        AlertDialog.Builder mtabDialog = new AlertDialog.Builder(PillReminderEntryDetails.this);
        View mview = getLayoutInflater().inflate(R.layout.dialog_entermeddetails, null);
        mtabDialog.setTitle("Update Med Detail");

        final EditText customentr = (EditText) mview.findViewById(R.id.customentr);
        final EditText medname_dialog = (EditText) mview.findViewById(R.id.medname_dialog);
        final EditText company_dialog = (EditText) mview.findViewById(R.id.company_dialog);
        final RadioButton custom = (RadioButton) mview.findViewById(R.id.custom);
        final RadioButton none = (RadioButton) mview.findViewById(R.id.none);
        final RadioButton beforemeal = (RadioButton) mview.findViewById(R.id.beforemeal);
        final RadioButton withmeal = (RadioButton) mview.findViewById(R.id.withmeal);
        final RadioButton aftermeal = (RadioButton) mview.findViewById(R.id.aftermeal);

        customentr.setVisibility(View.GONE);

        medname_dialog.setText(MedName.getText().toString());
        company_dialog.setText(company);

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitName.setText(units);
                mealdecison = 1;
            }
        });
        beforemeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitName.setText(units+", Before meal");
                mealdecison = 2;
                intakeadvise = "";
                intakeadvise = "Before Meal";
            }
        });
        withmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitName.setText(units+", With meal");
                mealdecison = 3;
                intakeadvise = "";
                intakeadvise = "With Meal";
            }
        });
        aftermeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitName.setText(units+", After meal");
                mealdecison = 4;
                intakeadvise = "";
                intakeadvise = "After Meal";
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customentr.setVisibility(View.VISIBLE);
                UnitName.setText(units+", "+ customText);
                mealdecison = 5;
                intakeadvise = "";
                intakeadvise = customText;
            }
        });

        if(mealdecison == 1){
            none.setChecked(true);
        }else if(mealdecison == 2){
            beforemeal.setChecked(true);
        } else if(mealdecison == 3){
            withmeal.setChecked(true);
        }else if(mealdecison == 4){
            aftermeal.setChecked(true);
        }else if(mealdecison == 5){
            custom.setChecked(true);
            customentr.setVisibility(View.VISIBLE);
            customentr.setText(customText);
        }

        mtabDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customText = customentr.getText().toString();
                MedName.setText(medname_dialog.getText().toString());
                company = company_dialog.getText().toString();
                dialog.dismiss();
            }
        });
        mtabDialog.setView(mview);
        AlertDialog dialog = mtabDialog.create();
        dialog.show();

    }

    public void openTabletDialog(final String fromwhere){
        AlertDialog.Builder mtabDialog = new AlertDialog.Builder(PillReminderEntryDetails.this);
        View mview = getLayoutInflater().inflate(R.layout.dialog_tablet, null);
        mtabDialog.setTitle("Enter Dosage");
        final EditText tabEdit = (EditText) mview.findViewById(R.id.tabletEditText);

        mtabDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tabvalue = tabEdit.getText().toString();
                if(fromwhere.equals("Adapter")){
                   Log.d("tabedit","***********************" + tabvalue + "*******************************************");
                   changeTablet(tab_position, tabvalue+ " "+units);
               }else if (fromwhere.equals("Frequency")){
                    DosageDetail.setText(tabvalue+" "+units);
               }

                dialog.dismiss();

            }
        });
        mtabDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mtabDialog.setView(mview);
        AlertDialog dialog = mtabDialog.create();
        dialog.show();
    }

    public void openDurationDialog(){
        AlertDialog.Builder mtabDialog = new AlertDialog.Builder(PillReminderEntryDetails.this);
        final View mview = getLayoutInflater().inflate(R.layout.dialog_duration, null);
        mtabDialog.setTitle("Duration Details");
        final LinearLayout DurationLL = (LinearLayout) mview.findViewById(R.id.LLDuration);
        final LinearLayout startDateLL = (LinearLayout) mview.findViewById(R.id.linearlayoutstartDate);
        final RadioButton radioButtonEveryday = (RadioButton) mview.findViewById(R.id.radioButtonEveryday);
        final RadioButton radioButtonsSelectedDays = (RadioButton) mview.findViewById(R.id.radioButtonsSelectedDays);
        final EditText editTextDuration = (EditText) mview.findViewById(R.id.durationEdittext);
        final TextView today = (TextView) mview.findViewById(R.id.Today);
        radioButtonEveryday.setChecked(true);

        DurationLL.setVisibility(mview.GONE);

        startDateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PillReminderEntryDetails.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                years = year;
                months = month;
                days = dayOfMonth;
                String dates = years+"-"+String.format("%02d",months)+"-"+String.format("%02d",days);
                today.setText(dates);
                startdate=dates;
            }
        };


        radioButtonEveryday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationLL.setVisibility(mview.GONE);
                everyday = 1;
                xdays = 0;
            }
        });

        radioButtonsSelectedDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationLL.setVisibility(mview.VISIBLE);
                xdays = 1;
                everyday = 0;
            }
        });

        mtabDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                duration = editTextDuration.getText().toString();

                if (radioButtonEveryday.isChecked()){
                    DurationDet.setText("No end date");
                }else if (radioButtonsSelectedDays.isChecked()){
                    DurationDet.setText("For "+duration+ " days");
                }
            }
        });

        if (!startdate.equals("Today")){
            today.setText(startdate);
        }

        if (xdays == 1){
            radioButtonsSelectedDays.setChecked(true);
            DurationLL.setVisibility(mview.VISIBLE);
            editTextDuration.setText(duration);
        }else
            editTextDuration.setText(duration);

        mtabDialog.setView(mview);
        AlertDialog dialog = mtabDialog.create();
        dialog.show();
    }

    public void openFrequencyDialog(){
        AlertDialog.Builder mtabDialog = new AlertDialog.Builder(PillReminderEntryDetails.this);
        mview = getLayoutInflater().inflate(R.layout.dialog_frequencyofintake, null);
        mtabDialog.setTitle("Frequency Details");
        final RadioButton xtimesday = (RadioButton) mview.findViewById(R.id.xtimesaday);
        final RadioButton everyxhours = (RadioButton) mview.findViewById(R.id.everyxhours);
        final RadioButton specificdaysofweek = (RadioButton) mview.findViewById(R.id.specificdaysofweek);
        weekdays = (LinearLayout) mview.findViewById(R.id.weekdays);

        final CheckBox moncheck = (CheckBox) mview.findViewById(R.id.mon);
        final CheckBox tuecheck = (CheckBox) mview.findViewById(R.id.tues);
        final CheckBox wedcheck = (CheckBox) mview.findViewById(R.id.wed);
        final CheckBox thurcheck = (CheckBox) mview.findViewById(R.id.thurs);
        final CheckBox fricheck = (CheckBox) mview.findViewById(R.id.fri);
        final CheckBox satcheck = (CheckBox) mview.findViewById(R.id.sat);
        final CheckBox suncheck = (CheckBox) mview.findViewById(R.id.sun);

        moncheck.setChecked(true);
        tuecheck.setChecked(true);
        wedcheck.setChecked(true);
        thurcheck.setChecked(true);
        fricheck.setChecked(true);

        weekdays.setVisibility(mview.GONE);

        if(xtimesaday == 1){
            xtimesday.setChecked(true);
        }
        else if (xhours == 1){
            everyxhours.setChecked(true);
        }
        else if (specificdays == 1){
            weekdays.setVisibility(mview.VISIBLE);
            specificdaysofweek.setChecked(true);

            if (mon == 1){
                moncheck.setChecked(true);
            }else moncheck.setChecked(false);
            if (tues == 1){
                tuecheck.setChecked(true);
            }else  tuecheck.setChecked(false);
            if (wed == 1){
                wedcheck.setChecked(true);
            }else wedcheck.setChecked(false);
            if (thurs == 1){
                thurcheck.setChecked(true);
            }else thurcheck.setChecked(false);
            if (fri == 1){
                fricheck.setChecked(true);
            }else fricheck.setChecked(false);
            if (sat == 1){
                satcheck.setChecked(true);
            }else satcheck.setChecked(false);
            if (sun == 1){
                suncheck.setChecked(true);
            }else suncheck.setChecked(false);

        }
        if(getstuff.getStringExtra("FromWhere").equals("Edit")){


        }

        xtimesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time.setVisibility(mview.VISIBLE);
                timeRecyclerview.setVisibility(mview.VISIBLE);
                weekdays.setVisibility(mview.GONE);
                Frequency.setVisibility(View.GONE);
                xtimesaday =1;
                xhours = 0;
                specificdays = 0;
                FrequncyDet.setText("Daily");
            }
        });

        everyxhours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time.setVisibility(mview.GONE);
                timeRecyclerview.setVisibility(mview.GONE);
                weekdays.setVisibility(mview.GONE);
                Frequency.setVisibility(View.VISIBLE);
                xhours = 1;
                xtimesaday =0;
                specificdays = 0;
                FrequncyDet.setText("Daily, every X hours");
            }
        });

        specificdaysofweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time.setVisibility(mview.VISIBLE);
                timeRecyclerview.setVisibility(mview.VISIBLE);
                weekdays.setVisibility(mview.VISIBLE);
                Frequency.setVisibility(View.GONE);
                xhours = 0;
                xtimesaday =0;
                specificdays = 1;
                FrequncyDet.setText("Specific Days of Week");
            }
        });

        mtabDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (moncheck.isChecked()){
                    mon = 1;
                }else
                    mon = 0;

                if (tuecheck.isChecked()){
                    tues = 1;
                }else
                    tues = 0;

                if (wedcheck.isChecked()){
                    wed = 1;
                }else
                    wed = 0;

                if (thurcheck.isChecked()){
                    thurs = 1;
                }else
                    thurs = 0;

                if (fricheck.isChecked()){
                    fri = 1;
                }else
                    fri = 0;

                if (satcheck.isChecked()){
                    sat = 1;
                }else
                    sat = 0;

                if (suncheck.isChecked()){
                    sun = 1;
                }else
                    sun = 0;


                dialog.dismiss();
            }
        });

        mtabDialog.setView(mview);
        AlertDialog dialog = mtabDialog.create();
        dialog.show();
    }

    public void openTimePicker(){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "TimePicker");
        Toast.makeText(getBaseContext(), "called from service", Toast.LENGTH_LONG).show();
    }

    public void insertReminderTimes(int position, int hourOfDay, int minute, String message, int tab){
        reminderItems.add(position,new AddReminderItems(R.drawable.ic_cancel, textTime, String.valueOf(tab)+" "+ units));
        setHoursAndMinutesInArray(hourOfDay, minute, message);
        adapter.notifyItemInserted(position);
        tabletArray.add(tab);
    }

    public void RemoveReminderTimes(int position){
        reminderItems.remove(position);
        adapter.notifyItemRemoved(position);
        hoursArray.remove(position);
        minuteArray.remove(position);
        timeinString.remove(position);
        tabletArray.remove(position);
    }

    public void changeTime(int position, String Text){
        reminderItems.get(position).changeTextTime(Text);
        adapter.notifyItemChanged(position);
        hoursArray.set(position,hour);
        minuteArray.set(position, minut);
        timeinString.set(position,Text);

    }

    public void changeTablet(int position, String Text){
        reminderItems.get(position).changeTextTablet(Text);
        tabletArray.set(position, Integer.parseInt(tabvalue));
        adapter.notifyItemChanged(position);

    }

    public void buildRecyclerView (){
        recyclerView = findViewById(R.id.timerecyclerview);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AddReminderAdapter(reminderItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AddReminderAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                RemoveReminderTimes(position);
            }

            @Override
            public void onTimeClick(int position) {
                time_position = position;
            }

            @Override
            public void onTabClick(int position) { tab_position = position;}
        });
    }

    public void ReminderTimesChangeOrInsert( int hourOfDay, int minute, String message, int tab){
        if(decision == 0){
            insertReminderTimes(position, hourOfDay, minute, message, tab);

        }else if (decision == 2){
            firstintakeinhours.setText(textTime);
        }
        else{
            changeTime(time_position, textTime);
        }
        decision = 0;
    }

    public void calculateIntervalTimes(){
        int index = reminderSpinner.getSelectedItemPosition();
        Date intervalTime = null;
        Date intervalTime24 = null;
        Calendar intervalTimeCov = Calendar.getInstance();
        Calendar intervalTimein24Hours = Calendar.getInstance();

        String time = firstintakeinhours.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        DateFormat t4hourformat = new SimpleDateFormat("HH:mm");
        int hours, minute, hr;
        int counter = 0;
        String timeInString = "";


        try {
            intervalTime = formatter.parse(time);
            intervalTimeCov.setTime(intervalTime);

            String output = t4hourformat.format(intervalTime);
            intervalTime24 = t4hourformat.parse(output);
            intervalTimein24Hours.setTime(intervalTime24);


            hours = intervalTimein24Hours.get(Calendar.HOUR_OF_DAY);
            minute = intervalTimein24Hours.get(Calendar.MINUTE);

            counter = 24/interval[index];

            for(int i = 0; i<counter; i++){
                if (hours >= 24)
                    hr = hours - 24;
                else
                    hr = hours;

                tabletArray.add(Integer.parseInt(tabvalue));

                if(hr > 12){
                    int hour = hr - 12;
                    timeInString = String.valueOf(hour) + ":" + String.valueOf(minute) +" PM";
                }else if (hr == 0){
                    timeInString = "12:" + String.valueOf(minute) +" AM";
                }else if(hr == 12)
                    timeInString = String.valueOf(hr) + ":" + String.valueOf(minute) +" PM";
                else
                    timeInString = String.valueOf(hr) + ":" + String.valueOf(minute) +" AM";

                setHoursAndMinutesInArray(hr, minute, timeInString);


                hours = hours + interval[index];


            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void setAlarmForSpecificDays(){

        if (mon == 1){
            specificDaycounter++;
            listOfSpecificDays.add("monday");
        }
        if (tues == 1){
            specificDaycounter++;
            listOfSpecificDays.add("tuesday");
        }
        if (wed == 1){
            specificDaycounter++;
            listOfSpecificDays.add("wednesday");
        }
        if (thurs == 1){
            listOfSpecificDays.add("thursday");
            specificDaycounter++;
        }
        if (fri == 1){
            specificDaycounter++;
            listOfSpecificDays.add("friday");
        }
        if (sat == 1) {
            specificDaycounter++;
            listOfSpecificDays.add("saturday");
        }
        if (sun == 1) {
            specificDaycounter++;
            listOfSpecificDays.add("sunday");
        }
    }

    public void setAlaram(){
        int forloopcounter, isBefores = 0;


        for(int i = 0; i < hoursArray.size(); i++){
            Calendar Impcalendar = Calendar.getInstance();

                if (!startdate.equals("Today")){
                    Impcalendar.set(Calendar.YEAR, years);
                    Impcalendar.set(Calendar.MONTH, months - 1);
                    Impcalendar.set(Calendar.DAY_OF_MONTH, days);
                }
                    // rather than adding all those different hours, try adding the intervals, for example, add 6 as in 6 intervals and so on
                    // also try to instantiate impclendar here.

                    int hour = hoursArray.get(i);
                    int min = minuteArray.get(i);
                    String message = timeinString.get(i);
                    Impcalendar.set(Calendar.HOUR_OF_DAY, hour);
                    Impcalendar.set(Calendar.MINUTE, min);
                    Impcalendar.set(Calendar.SECOND, 0);

                    //check to see whether it is everyday or not. If everyday is ticked, then we have to save it for 15 times or if for a number of duration, the the for loop will change accordingly.
                    if (everyday == 1){
                        forloopcounter = 3;
                    }
                    else
                        forloopcounter = Integer.parseInt(duration);

                    //detailCalendar = Impcalendar;
                    int k = 0;
                    int p = 0;


                    if (specificdays == 1){
                        do{
                            String days = "";
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Impcalendar.add(Calendar.DATE, k);
                            days= format.format(Impcalendar.getTime());
                            String Day = getDayFromDateString(days,"yyyy-MM-dd HH:mm:ss" );
                            Log.d(TAG, "Check Claendar Day Value: "+Day);
                            if(listOfSpecificDays.contains(Day)){
                                isBefores = startAlarmOnce(Impcalendar, message);
                                p++;
                            }
                            Impcalendar.add(Calendar.DATE, -1*k);
                            k++;
                        }while(p < forloopcounter);
                    }else{
                        for(int j = 0; j<forloopcounter; j++) {
                            Impcalendar.add(Calendar.DATE, j);

                            isBefores = startAlarmOnce(Impcalendar, message);
                            long milies = Impcalendar.getTimeInMillis();
                            Log.d(TAG, "setAlaram: before subtraction : " +milies);
                            Impcalendar.add(Calendar.DATE, -1*j);
                            milies = Impcalendar.getTimeInMillis();
                            Log.d(TAG, "setAlaram: after subtraction : " +milies);
                        }
                    }

                    saveAlarmRecords(isBefores, forloopcounter, message);
                    isBefores = 0;
                    isBefore = 0;
                    tabCounter++;
                    listOfDatesInMillis.clear();
        }
            tabCounter = 0;

    }

    public static String getDayFromDateString(String stringDate,String dateTimeFormat)
    {
        String[] daysArray = new String[] {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
        String day = "";

        int dayOfWeek =0;
        //dateTimeFormat = yyyy-MM-dd HH:mm:ss
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            day = daysArray[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return day;
    }

    public int startAlarmOnce(Calendar c, String textTime){
        int requestCode = introManager.GetPendintIntent();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("AlarmTitle", "Medication due at " + textTime);
        intent.putExtra("AlarmContent",MedName.getText().toString()+ " " +  units);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        pendingIntents.add(requestCode);
        introManager.setPendingIntent(requestCode+1);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
            isBefore = 1;
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        listOfDatesInMillis.add(c.getTimeInMillis());
        return isBefore;
    }

    public void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

       for(int i = 0; i<=requestCode; i++){
           PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
           alarmManager.cancel(pendingIntent);
       }

        // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 60 * 2, pendingIntent);

    }

    public void startAlarmRepeating(Calendar c, String textTime){
        int requestCode = introManager.GetPendintIntent();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("AlarmTitle", "Medication due at " + textTime);
        intent.putExtra("AlarmContent",MedName.getText().toString()+ " " +  units);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        introManager.setPendingIntent(requestCode+1);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

       // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 60 * 2, pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void saveAlarmRecords(int isBefore, int forloopcounter, String message){
        String strDate = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        int q = 0;
        int w = 0;

        if (master_id == 0) {
            ContentValues cv = new ContentValues();
            cv.put(PHDbClasses.AlarmMaster.COLUMN_MEDNAME, MedName.getText().toString());
            cv.put(PHDbClasses.AlarmMaster.COLUMN_COMPANY, company);
            cv.put(PHDbClasses.AlarmMaster.COLUMN_INTAKEADVISE, intakeadvise);
            if(startdate.equals("Today") && isBefore == 0){
                 strDate =mdformat.format(cal.getTime());
                 cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, strDate);
            }else if(startdate.equals("Today") && isBefore == 1){
                cal.add(cal.DATE, 1);
                strDate =mdformat.format(cal.getTime());
                cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, strDate);
            }
            else if (isBefore == 1) {
                cal.add(cal.DATE, 1);
                strDate =mdformat.format(cal.getTime());
                cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, strDate);
            }else
                cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, startdate);
            if (!duration.equals(""))
                cv.put(PHDbClasses.AlarmMaster.COLUMN_DURATION, Integer.valueOf(duration));
            cv.put(PHDbClasses.AlarmMaster.COLUMN_EVERYDAY, everyday);
            cv.put(PHDbClasses.AlarmMaster.COLUMN_XDAY, xdays);
            cv.put(PHDbClasses.AlarmMaster.COLUMN_XHOURS, remindSpinnerinString);
            cv.put(PHDbClasses.AlarmMaster.COLUMN_FIRSTINTAKE, firstintakeinhours.getText().toString());
            //lets wait for first intake one to come

            cv.put(PHDbClasses.AlarmMaster.COLUMN_ACTIVE, 1);

            master_id = mDatabase.insert(PHDbClasses.AlarmMaster.TABLE_NAME, null, cv);
        }

        ContentValues ad = new ContentValues();
        if(specificdays == 0){
            for (int i = 0; i<forloopcounter; i++){
                Date timeformatted = null;
                Date startDateFormatted = null;
                int times = 0;
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                try {
                    timeformatted = formatter.parse(message);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String days = "";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if(startdate.equals("Today") && isBefore == 0){
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, i);
                    strDate =mdformat.format(calendar.getTime());

                    days= format.format(calendar.getTime());

                }else if(startdate.equals("Today") && isBefore == 1){
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(calendar.DATE, 1+i);
                    strDate =mdformat.format(calendar.getTime());

                    days= format.format(calendar.getTime());

                }
                else if (isBefore == 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(calendar.DATE, 1);
                    calendar.add(calendar.DATE, i);
                    strDate =mdformat.format(calendar.getTime());

                    days= format.format(calendar.getTime());

                }else{
                    try {
                        startDateFormatted = mdformat.parse(startdate);
                        detailCalendar.setTime(startDateFormatted);
                        detailCalendar.add(Calendar.DATE, i);
                        strDate =mdformat.format(detailCalendar.getTime());

                        days= format.format(detailCalendar.getTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                //String Day = getDayFromDateString(days,"yyyy-MM-dd HH:mm:ss" );

                ad.put(PHDbClasses.AlarmDetails.COLUMN_ALARMMASTERID, master_id);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_TIME, message);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_TIMEFORMATTED, timeformatted.getTime());
                ad.put(PHDbClasses.AlarmDetails.COLUMN_DATE, strDate);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_TABLETS, tabletArray.get(tabCounter));
                ad.put(PHDbClasses.AlarmDetails.COLUMN_ACTIVE, 1);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_TAKEN, 0);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_NEXT, 1);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_SKIPPED, 0);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_SNOOZE, 0);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_TABLETUNIT, units);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_PENDINGINTENT, pendingIntents.get(pendingIntentCounter));
                ad.put(PHDbClasses.AlarmDetails.COLUMN_DATEINMILLIS, listOfDatesInMillis.get(i));
                mDatabase.insert(PHDbClasses.AlarmDetails.TABLE_NAME, null, ad);
                pendingIntentCounter++;
        }


        }else if (specificdays == 1){
            do {
                Date timeformatted = null;
                Date startDateFormatted = null;
                int times = 0;
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                try {
                    timeformatted = formatter.parse(message);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String days = "";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if(startdate.equals("Today") && isBefore == 0){
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, w);
                    strDate =mdformat.format(calendar.getTime());

                    days= format.format(calendar.getTime());

                }else if(startdate.equals("Today") && isBefore == 1){
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(calendar.DATE, 1+w);
                    strDate =mdformat.format(calendar.getTime());

                    days= format.format(calendar.getTime());

                }
                else if (isBefore == 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(calendar.DATE, 1);
                    calendar.add(calendar.DATE, w);
                    strDate =mdformat.format(calendar.getTime());

                    days= format.format(calendar.getTime());

                }else{
                    try {
                        startDateFormatted = mdformat.parse(startdate);
                        detailCalendar.setTime(startDateFormatted);
                        detailCalendar.add(Calendar.DATE, w);
                        strDate =mdformat.format(detailCalendar.getTime());

                        days= format.format(detailCalendar.getTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                String Day = getDayFromDateString(days,"yyyy-MM-dd HH:mm:ss" );

                    if (listOfSpecificDays.contains(Day)){
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_ALARMMASTERID, master_id);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_TIME, message);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_TIMEFORMATTED, timeformatted.getTime());
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_DATE, strDate);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_TABLETS, tabletArray.get(tabCounter));
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_ACTIVE, 1);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_TAKEN, 0);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_TABLETUNIT, units);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_NEXT, 1);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_SKIPPED, 0);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_SNOOZE, 0);
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_PENDINGINTENT, pendingIntents.get(pendingIntentCounter));
                        ad.put(PHDbClasses.AlarmDetails.COLUMN_DATEINMILLIS, listOfDatesInMillis.get(q));
                        mDatabase.insert(PHDbClasses.AlarmDetails.TABLE_NAME, null, ad);
                        pendingIntentCounter++;
                        q++;

                    }


                w++;
            }while (q < forloopcounter);
        }
    }

    public void letsEdit(int idForEdit){

        //also remember to check for startdate, best to make calendar object using the start date since it is recorded in the first instance.
        phDbHelper = new PHDbHelper(this);
        Cursor Cursorid = phDbHelper.getAlarmMasterID(idForEdit);
        Cursorid.moveToFirst();
        long id =  Cursorid.getLong(Cursorid.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_ALARMMASTERID));
        Cursor MasterDetails = phDbHelper.fetchAllMasterDetails((int)id);
        MasterDetails.moveToFirst();

         MasterID = MasterDetails.getLong(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster._ID));
         masterMedName = MasterDetails.getString(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_MEDNAME));
         masterCompany = MasterDetails.getString(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_COMPANY));
         masterIntakeAdvise = MasterDetails.getString(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_INTAKEADVISE));
         masterStartDate = MasterDetails.getString(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_STARTDATE));
         masterDuration = MasterDetails.getInt(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_DURATION));
         masterEveryday = MasterDetails.getInt(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_EVERYDAY));
         masterXdays = MasterDetails.getInt(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_XDAY));
         masterXhours = MasterDetails.getInt(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_XHOURS));
         masterFirstIntake = MasterDetails.getString(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_FIRSTINTAKE));
         masterActive = MasterDetails.getInt(MasterDetails.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_ACTIVE));

         id = MasterID;
         mastIDs = MasterID;
         Cursor DetailCursor = phDbHelper.fetchAllAlarmDetails((int)id);
         DetailCursor.moveToFirst();

         MedName.setText(masterMedName);
         UnitName.setText("Tablet");
         company = masterCompany;

         if (masterIntakeAdvise == null){
             mealdecison = 1;
         }else if (masterIntakeAdvise.equals("Before Meal")){
             mealdecison = 2;
         }else if (masterIntakeAdvise.equals("With Meal")){
             mealdecison = 3;
         }else if (masterIntakeAdvise.equals("After Meal")){
             mealdecison = 4;
         }else{
             customText = intakeadvise;
             mealdecison = 5;
         }

         if(masterDuration != 0){
             xdays = 1;
             everyday = 0;
             DurationDet.setText("For "+String.valueOf(masterDuration)+ " days");
             duration = String.valueOf(masterDuration);
         }


         startdate = masterStartDate;

         if(masterXdays == 1){
             Time.setVisibility(mview.VISIBLE);
             Frequency.setVisibility(View.GONE);
             xtimesaday =1;
             xhours = 0;
             specificdays = 0;
             FrequncyDet.setText("Daily");
         }

         if(masterXhours != 0){
             Time.setVisibility(mview.GONE);
             Frequency.setVisibility(View.VISIBLE);
             xhours = 1;
             xtimesaday =0;
             specificdays = 0;
             FrequncyDet.setText("Daily, every X hours");
             Toast.makeText(this, "master x hours", Toast.LENGTH_SHORT).show();
             firstintakeinhours.setText(masterFirstIntake);
             int position = 0;
             int i =0;

             while(i < interval.length){
                 if(interval[i] == masterXhours){
                     position = i;
                     i++;
                 }
                 else
                     i++;
             }

             reminderSpinner.setSelection(position);
         }else
         {
             while (!DetailCursor.isAfterLast()) {
                 detailTime = DetailCursor.getString(DetailCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIME));
                 detailTimeFormatted = DetailCursor.getLong(DetailCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIMEFORMATTED));
                 detailTablets = DetailCursor.getInt(DetailCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TABLETS));
                 units = DetailCursor.getString(DetailCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TABLETUNIT));
                 textTime = detailTime;

                 Calendar calendar = Calendar.getInstance();
                 calendar.setTimeInMillis(detailTimeFormatted);

                 position = reminderItems.size();
                 ReminderTimesChangeOrInsert(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), detailTime, detailTablets);

                 DetailCursor.moveToNext();
             }

         }

         // we have to first update all the master thing
        //den delete the details one and add the new ones in it.

    }

    public void EditTheTabs(){
        phDbHelper.updateAllAlarmDetailsToInactive((int)mastIDs);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");

        phDbHelper.deleteFromAlarmDetails(mastIDs, calendar.getTimeInMillis());

        String strDate = "";
        Calendar cal = Calendar.getInstance();

        ContentValues cv = new ContentValues();
        cv.put(PHDbClasses.AlarmMaster.COLUMN_MEDNAME, MedName.getText().toString());
        cv.put(PHDbClasses.AlarmMaster.COLUMN_COMPANY, company);
        cv.put(PHDbClasses.AlarmMaster.COLUMN_INTAKEADVISE, intakeadvise);
        if(startdate.equals("Today") && isBefore == 0){
            strDate =mdformat.format(cal.getTime());
            cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, strDate);
        }else if(startdate.equals("Today") && isBefore == 1){
            cal.add(cal.DATE, 1);
            strDate =mdformat.format(cal.getTime());
            cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, strDate);
        }
        else if (isBefore == 1) {
            cal.add(cal.DATE, 1);
            strDate =mdformat.format(cal.getTime());
            cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, strDate);
        }else
            cv.put(PHDbClasses.AlarmMaster.COLUMN_STARTDATE, startdate);
        if (!duration.equals(""))
            cv.put(PHDbClasses.AlarmMaster.COLUMN_DURATION, Integer.valueOf(duration));
        cv.put(PHDbClasses.AlarmMaster.COLUMN_EVERYDAY, everyday);
        cv.put(PHDbClasses.AlarmMaster.COLUMN_XDAY, xdays);
        cv.put(PHDbClasses.AlarmMaster.COLUMN_XHOURS, remindSpinnerinString);
        cv.put(PHDbClasses.AlarmMaster.COLUMN_FIRSTINTAKE, firstintakeinhours.getText().toString());
        //lets wait for first intake one to come

        cv.put(PHDbClasses.AlarmMaster.COLUMN_ACTIVE, 1);

        mDatabase.update(PHDbClasses.AlarmMaster.TABLE_NAME, cv, "_id="+mastIDs, null);

        master_id = mastIDs;

        Date date = null;
        try {
            date = mdformat.parse(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        years = c.get(Calendar.YEAR);
        months = c.get(Calendar.MONTH) + 1;
        days = c.get(Calendar.DAY_OF_MONTH);


    }
}

/// we have to handle specific days and dosage......
