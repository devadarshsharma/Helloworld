package com.example.helloworld;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PillReminder extends AppCompatActivity implements BottomSheetShowAlarm.bottomSheetListener,
                                                               BottomSheetSkipDetails.bottomSheetListenerRB,
                                                               BottomSheetShowSnooze.bottomSheetListenerSnooze{

    private CollapsibleCalendar collapsibleCalendar;
    private FloatingActionButton fab;
    private SQLiteDatabase mDatabase;
    private Cursor cursor;
    private ShowAlarmAdaper showAlarmAdaper;
    private RelativeLayout ifNoPill;
    private String strDate;
    private LinearLayout showAlarmsRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private PHDbHelper phDbHelper;
    private int i = 0;
    private long id;
    private String dates = "";
    private IntroManager introManager;
    private int pendingintent;
    private Button cancelalarms;


    static PillReminder instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminder);
        instance = this;
        introManager = new IntroManager(this);

        ifNoPill = findViewById(R.id.ifNoPill);
        showAlarmsRecycler = findViewById(R.id.showAlarmsRecycler);

        ifNoPill.setVisibility(View.GONE);
        cancelalarms = findViewById(R.id.cancelaallalarms);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        strDate =mdformat.format(calendar.getTime());

        phDbHelper = new PHDbHelper(this);
        mDatabase = phDbHelper.getReadableDatabase();

        collapsibleCalendar = findViewById(R.id.collapsibleCalendar);
        fab = findViewById(R.id.floatingActionButton);

        BuildRecyclerView(strDate);

//        showAlarmAdaper.setOnItemClickListener(new ShowAlarmAdaper.OnItemClickListener() {
//            @Override
//            public void onItemClick(long id) {
//                showAlarmDetails(id);
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PillReminder.this, PillReminderEntry.class);
                startActivity(i);
            }
        });

        cancelalarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancelAlarms(introManager.GetPendintIntent());
//                AddAlarmDetails addAlarmDetails = new AddAlarmDetails();
//                addAlarmDetails.getAllDetailsForEveryday();
                String results = phDbHelper.getAllDetailsForEveryday();

                    Toast.makeText(PillReminder.this, results, Toast.LENGTH_SHORT).show();
                
            }
        });

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
            }

            @Override
            public void onItemClick(View view) {
                Day day = collapsibleCalendar.getSelectedDay();

                //showAlarmAdaper.swapCursor(cursor);
                i = 1;

                dates = day.getYear()+"-"+String.format("%02d",(day.getMonth()+1))+"-"+String.format("%02d", day.getDay());
                BuildRecyclerView(dates);
            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });

    }

    public void showAlarmDetails(long isd){
        // Toast.makeText(this, "From PillReminder"+String.valueOf(id), Toast.LENGTH_LONG).show();
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetShowAlarm();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), "BottomSheet");
        id = isd;
    }

    private Cursor getAllItems(String startdateSerarch){

        String sql = "SELECT a._ID, a.time,a.date, a.tablets, a.active, a.taken, a.next, a.timetaken, a.datetaken ,a.skipdetails, a.skipped,a.snooze, a.snoozetime, b.medName FROM AlarmDetails a " +
                "INNER JOIN AlarmMaster b on a.alarmMasterID = b._ID "+
                "WHERE a.date " + "=" + "'"+startdateSerarch+"'" +
                " ORDER BY a.timeformatted ASC";

        Cursor c = mDatabase.rawQuery(sql, null);

        return c;

    }

    private void BuildRecyclerView(String date){

        cursor = getAllItems(date);
        RecyclerView recyclerView = findViewById(R.id.showAlarmsRecyclerView);
        int count = cursor.getCount();


        if (cursor.getCount() > 0){

            recyclerView.setHasFixedSize(false);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            showAlarmAdaper = new ShowAlarmAdaper(this, cursor);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(showAlarmAdaper);
            ifNoPill.setVisibility(View.GONE);
            showAlarmsRecycler.setVisibility(View.VISIBLE);
        }else {
            ifNoPill.setVisibility(View.VISIBLE);
            showAlarmsRecycler.setVisibility(View.GONE);
        }



    }

    @Override
    public void onButtonClicked(String text) {
        if (text.equals("Confirm"))
            takeThePill();
        else if (text.equals("Skip"))
            skipThePill();
        else if (text.equals("Snoozed")){
            snoozeThePill();
        }
    }

    public void takeThePill(){
        int ids = checkIfTaken();

        String sqls = "SELECT a.skipped FROM AlarmDetails a " +
                "WHERE a._id " + "=" + "'"+id+"'";

        Cursor cs = mDatabase.rawQuery(sqls, null);
        cs.moveToFirst();
        int skipped = cs.getInt(cs.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_SKIPPED));

        if (skipped == 1)
            Toast.makeText(this, "You have skipped this pill", Toast.LENGTH_LONG).show();
        else if(ids == 0) {
            mDatabase = phDbHelper.getWritableDatabase();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
            String Date = mdformat.format(calendar.getTime());

            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String Time = dateFormat.format(calendar.getTime());

            ContentValues cv = new ContentValues();
            cv.put(PHDbClasses.AlarmDetails.COLUMN_TAKEN, 1);
            cv.put(PHDbClasses.AlarmDetails.COLUMN_TIMETAKEN, Time);
            cv.put(PHDbClasses.AlarmDetails.COLUMN_DATECTAKEN, Date);

            mDatabase.update(PHDbClasses.AlarmDetails.TABLE_NAME, cv, PHDbClasses.AlarmDetails._ID + " = ?", new String[]{String.valueOf(id)});

            String Gettime = getTime();
            int isBefore = checkTimeIFBefore(Gettime);

            if(isBefore == 1){
                cancelAlarm(getPendingIntent());
            }

            BuildRecyclerView(dates);
        }else
            Toast.makeText(this, "Pill already taken", Toast.LENGTH_LONG).show();
    }

    public void skipThePill(){
        int taken = checkIfTaken();
        int skipped = checkIfSkipped();

        if(taken == 1){
            Toast.makeText(this, "Pill already taken", Toast.LENGTH_LONG).show();
        }else if (skipped == 1){
            Toast.makeText(this, "You have skipped this pill", Toast.LENGTH_LONG).show();
        }else {
            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetSkipDetails();
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "BottomSheet");
        }
    }

    @Override
    public void onRadioButtonClicked(String text) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String Time = dateFormat.format(calendar.getTime());

        ContentValues cv = new ContentValues();
        cv.put(PHDbClasses.AlarmDetails.COLUMN_SKIPPED, 1);
        cv.put(PHDbClasses.AlarmDetails.COLUMN_SKIPDETAILS, text);
        cv.put(PHDbClasses.AlarmDetails.COLUMN_TIMETAKEN, Time);

        mDatabase.update(PHDbClasses.AlarmDetails.TABLE_NAME, cv, PHDbClasses.AlarmDetails._ID + " = ?", new String[]{String.valueOf(id)});

        String Gettime = getTime();
        int isBefore = checkTimeIFBefore(Gettime);

        if(isBefore == 1){
            cancelAlarm(getPendingIntent());
        }

        BuildRecyclerView(dates);
        Toast.makeText(this, "Thanks! Your response has been saved", Toast.LENGTH_LONG).show();
    }

    public void snoozeThePill(){
        int taken = checkIfTaken();

        if(taken == 1){
            Toast.makeText(this, "Pill already taken", Toast.LENGTH_LONG).show();
        }else {
            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetShowSnooze();
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "BottomSheet");
        }
    }

    public int checkIfTaken(){
        String sqlTaken = "SELECT a.taken FROM AlarmDetails a " +
                "WHERE a._id " + "=" + "'"+id+"'";

        Cursor c = mDatabase.rawQuery(sqlTaken, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TAKEN));
    }

    public int checkIfSkipped(){
        String sql = "SELECT a.skipped FROM AlarmDetails a " +
                "WHERE a._id " + "=" + "'"+id+"'";

        Cursor cs = mDatabase.rawQuery(sql, null);
        cs.moveToFirst();
        return cs.getInt(cs.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_SKIPPED));

    }

    public int getPendingIntent(){
        String sql = "SELECT a.pendingitent FROM AlarmDetails a " +
                "WHERE a._id " + "=" + "'"+id+"'";

        Cursor cs = mDatabase.rawQuery(sql, null);
        cs.moveToFirst();
        return cs.getInt(cs.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_PENDINGINTENT));
    }

    public String  getTime(){
        String sql = "SELECT a.time FROM AlarmDetails a " +
                "WHERE a._id " + "=" + "'"+id+"'";

        Cursor cs = mDatabase.rawQuery(sql, null);
        cs.moveToFirst();
        return cs.getString(cs.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIME));
    }

    public int checkTimeIFBefore(String fromDB){
        Date orgtime, dbtime;
        int retValue = 0;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        String Times = dateFormat.format(calendar.getTime());

        try {
            dbtime = formatter.parse(fromDB);
            orgtime = formatter.parse(Times);

            if(orgtime.compareTo(dbtime) < 0){
                retValue = 1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  retValue;
    }

    @Override
    public void onButtonClicked(int time) {
        String times = startAlarmOnce(time);
        saveSnoozeData(times);
    }

    public String startAlarmOnce(int time){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, time);

        pendingintent= introManager.GetPendintIntent();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("AlarmTitle", "Medication due");
        intent.putExtra("AlarmContent","Please take your meds");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pendingintent, intent, 0);
        introManager.setPendingIntent(pendingintent + 1);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent);

        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String Time = dateFormat.format(now.getTime());

        return Time;

    }

    public void saveSnoozeData(String text){

        int getPI = getPendingIntent();
        cancelAlarm(getPI);

        ContentValues cv = new ContentValues();
        cv.put(PHDbClasses.AlarmDetails.COLUMN_SNOOZE, 1);
        cv.put(PHDbClasses.AlarmDetails.COLUMN_SNOOZETIME, text);
        cv.put(PHDbClasses.AlarmDetails.COLUMN_PENDINGINTENT, pendingintent);

        mDatabase.update(PHDbClasses.AlarmDetails.TABLE_NAME, cv, PHDbClasses.AlarmDetails._ID + " = ?", new String[]{String.valueOf(id)});
        BuildRecyclerView(dates);
        Toast.makeText(this, "Snooze has been set", Toast.LENGTH_LONG).show();
    }

    public void cancelAlarms(int requestCode){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        for(int i = 0; i<=requestCode; i++){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManager.cancel(pendingIntent);
        }

        // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 60 * 2, pendingIntent);

    }

    public void cancelAlarm(int requestCode){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);

        // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 60 * 2, pendingIntent);

    }
}
