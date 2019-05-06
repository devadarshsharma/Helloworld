package com.example.helloworld;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddAlarmDetails extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private PHDbHelper phDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phDbHelper = new PHDbHelper(this);
        mDatabase = phDbHelper.getReadableDatabase();

//        String GetEveryday = "SELECT a._ID, a.time,a.date, a.tablets, a.active, a.taken, a.next, a.timetaken, a.datetaken ,a.skipdetails, a.skipped,a.snooze, a.snoozetime, b.medName FROM AlarmDetails a " +
//                "INNER JOIN AlarmMaster b on a.alarmMasterID = b._ID "+
//                "WHERE a.date " + "=" + "'"+startdateSerarch+"'" +
//                " ORDER BY a.timeformatted ASC";
//
//        Cursor c = mDatabase.rawQuery(sql, null);
//
//
//        String sql = "SELECT a._ID, a.time,a.date, a.tablets, a.active, a.taken, a.next, a.timetaken, a.datetaken ,a.skipdetails, a.skipped,a.snooze, a.snoozetime, b.medName FROM AlarmDetails a " +
//                "INNER JOIN AlarmMaster b on a.alarmMasterID = b._ID "+
//                "WHERE a.date " + "=" + "'"+startdateSerarch+"'" +
//                " ORDER BY a.timeformatted ASC";
//
//        Cursor c = mDatabase.rawQuery(sql, null);
    }

    public void getAllDetailsForEveryday(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");

        String Times = mdformat.format(cal.getTime());
        Times = "2019-05-04";

        String sql = "SELECT a._ID, c.medName,a.alarmMasterID, a.time, max_date, a.timeformatted, a.tablets "+
                     "FROM AlarmDetails a " +
                        "INNER JOIN "+
                                "(select _id, alarmMasterID, max(date) as max_date "+
                                 "from AlarmDetails "+
                                 "GROUP by TIME) b "+
                        "on a._id = b._id and b.max_date = max_date "+
                        "inner join AlarmMaster c "+
                        "on c._id = a.alarmMasterID and c.everyday = 1 and max_date " + "="+ "'"+Times+"'";
        Cursor c = mDatabase.rawQuery(sql, null);

        if(c.getCount() > 0){
            c.moveToFirst();

            while(c.moveToNext()){
                int AlarmMasterId = c.getInt(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_ALARMMASTERID));
                String time = c.getString(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIME));
                Long timeformatted = c.getLong(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIMEFORMATTED));
                int tablets = c.getInt(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TABLETS));
                String date = c.getString(c.getColumnIndex("max_date"));

            }

        }

    }



}
