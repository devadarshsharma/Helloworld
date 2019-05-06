package com.example.helloworld;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.widget.Toast;

import com.example.helloworld.PHDbClasses.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

public class PHDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "basicsetup.db";
    public static final int DATABASE_VERSION = 9;
    private int counter = 0;
    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a");
    SharedPreferences sharedPreferences;
    Context context;
    IntroManager introManager;
    private static final String TAG = "PHDbHelper";

    public PHDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BASICSETUP_TABLE = "CREATE TABLE " +
                    BasicSetupEntry.TABLE_NAME + " (" +
                    BasicSetupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BasicSetupEntry.COLUMN_USERID+ " TEXT NOT NULL, "+
                    BasicSetupEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    BasicSetupEntry.COLUMN_DOB + " TEXT NOT NULL, " +
                    BasicSetupEntry.COLUMN_GENDER + " TEXT NOT NULL, " +
                    BasicSetupEntry.COLUMN_HEIGHT + " INTEGER NOT NULL, "+
                    BasicSetupEntry.COLUMN_WEIGHT + " INTEGER NOT NULL, "+
                    BasicSetupEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

        final String SQL_CREATE_ALARM_TABLE = "CREATE TABLE " +
                AlarmMaster.TABLE_NAME + " (" +
                AlarmMaster._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AlarmMaster.COLUMN_MEDNAME+ " TEXT NOT NULL, "+
                AlarmMaster.COLUMN_COMPANY + " TEXT  NULL, " +
                AlarmMaster.COLUMN_INTAKEADVISE + " TEXT  NULL, " +
                AlarmMaster.COLUMN_STARTDATE + " TEXT NOT NULL, " +
                AlarmMaster.COLUMN_DURATION + " INTEGER  NULL, "+
                AlarmMaster.COLUMN_EVERYDAY + " INTEGER  NULL, "+
                AlarmMaster.COLUMN_XDAY + " INTEGER  NULL, "+
                AlarmMaster.COLUMN_XHOURS + " INTEGER NULL" +
                AlarmMaster.COLUMN_SPECIFICDAYS + " TEXT  NULL, " +
                AlarmMaster.COLUMN_FIRSTINTAKE + " TEXT NULL, " +
                AlarmMaster.COLUMN_ACTIVE + " INTEGER NOT NULL, "+
                AlarmMaster.COLUMN_DATECREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";


        final String SQL_CREATE_ALARMDETAIL_TABLE = "CREATE TABLE " +
                AlarmDetails.TABLE_NAME + " (" +
                AlarmDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AlarmDetails.COLUMN_ALARMMASTERID+ " INTEGER NOT NULL, "+
                AlarmDetails.COLUMN_TIME + " TEXT  NOT NULL, " +
                AlarmDetails.COLUMN_TIMEFORMATTED + " LONG  NOT NULL, "+
                AlarmDetails.COLUMN_DATE + " TEXT  NOT NULL, " +
                AlarmDetails.COLUMN_STARTDATE + " TEXT NULL, " +
                AlarmDetails.COLUMN_TABLETS + " INTEGER  NOT NULL, "+
                AlarmDetails.COLUMN_ACTIVE + " INTEGER NOT NULL, "+
                AlarmDetails.COLUMN_TAKEN + " INTEGER NOT NULL," +
                AlarmDetails.COLUMN_NEXT + " INTEGER  NOT NULL, " +
                AlarmDetails.COLUMN_TIMETAKEN + " TEXT  NULL, " +
                AlarmDetails.COLUMN_DATECTAKEN + " TEXT  NULL, " +
                AlarmDetails.COLUMN_SKIPDETAILS + " TEXT  NULL, " +
                AlarmDetails.COLUMN_SKIPPED + " INTEGER  NULL, " +
                AlarmDetails.COLUMN_SNOOZE + " INTEGER  NULL, " +
                AlarmDetails.COLUMN_SNOOZETIME + " TEXT  NULL, " +
                AlarmDetails.COLUMN_PENDINGINTENT + " INTEGER NOT NULL, "+
                AlarmDetails.COLUMN_DATECREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_BASICSETUP_TABLE);
        db.execSQL(SQL_CREATE_ALARM_TABLE);
        db.execSQL(SQL_CREATE_ALARMDETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BasicSetupEntry.TABLE_NAME );
            db.execSQL("DROP TABLE IF EXISTS " + AlarmMaster.TABLE_NAME );
            db.execSQL("DROP TABLE IF EXISTS " + AlarmDetails.TABLE_NAME );
            onCreate(db);
    }

    public String getAllDetailsForEveryday(){
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");

        String Times = mdformat.format(cal.getTime());
        Times = "2019-05-08";

        String sql = "SELECT a._ID, c.medName,a.alarmMasterID, a.time, max_date, a.timeformatted, a.tablets "+
                "FROM AlarmDetails a " +
                "INNER JOIN "+
                "(select _id, alarmMasterID, max(date) as max_date "+
                "from AlarmDetails "+
                "GROUP by TIME) b "+
                "on a._id = b._id and b.max_date = max_date "+
                "inner join AlarmMaster c "+
                "on c._id = a.alarmMasterID and c.everyday = 1 and max_date " + "="+ "'"+Times+"'";

        Cursor c = db.rawQuery(sql, null);
        counter = 2;

        if(c.getCount() > 0){
            saveEverydayAlarm(c);
           return "Successfull";
        }
        return "No records";
    }

    public void saveEverydayAlarm(Cursor c){
        SQLiteDatabase db = this.getWritableDatabase();

        Calendar getTime = Calendar.getInstance();
        Calendar dates = Calendar.getInstance();
        Calendar finalDate = Calendar.getInstance();
        ContentValues ad = new ContentValues();

        introManager = new IntroManager(context);

        Date dateFormatted = null;
        Date timeFormatted = null;

        c.moveToFirst();
        String date = null;

        do {
            int AlarmMasterId = c.getInt(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_ALARMMASTERID));
            String time = c.getString(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIME));
            Long timeformatted = c.getLong(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIMEFORMATTED));
            int tablets = c.getInt(c.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TABLETS));
            date= c.getString(c.getColumnIndex("max_date"));

            try {
                timeFormatted = TimeFormat.parse(time);
                getTime.setTime(timeFormatted);
                dateFormatted = DateFormat.parse(date);
                dates.setTime(dateFormatted);

                int hour = getTime.get(Calendar.HOUR);
                int min = getTime.get(Calendar.MINUTE);
                int am_pm = getTime.get(Calendar.AM_PM);

                dates.set(Calendar.HOUR, hour);
                dates.set(Calendar.MINUTE, min);
                dates.set(Calendar.AM_PM, am_pm);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 1; i<=counter; i++){
                int pendingintentcode = introManager.GetPendintIntent();
                dates.add(Calendar.DATE, i);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlertReceiver.class);
                intent.putExtra("AlarmTitle", "Medication due ");
                intent.putExtra("AlarmContent","Please take your meds!!!");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pendingintentcode, intent, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dates.getTimeInMillis(), pendingIntent);

                ad.put(AlarmDetails.COLUMN_ALARMMASTERID, AlarmMasterId);
                ad.put(AlarmDetails.COLUMN_TIME, time);
                ad.put(AlarmDetails.COLUMN_TIMEFORMATTED, timeformatted);
                ad.put(AlarmDetails.COLUMN_DATE, DateFormat.format(dates.getTime()));
                ad.put(AlarmDetails.COLUMN_TABLETS, tablets);
                ad.put(AlarmDetails.COLUMN_ACTIVE, 1);
                ad.put(AlarmDetails.COLUMN_TAKEN, 0);
                ad.put(AlarmDetails.COLUMN_NEXT, 1);
                ad.put(AlarmDetails.COLUMN_SKIPPED, 0);
                ad.put(AlarmDetails.COLUMN_SNOOZE, 0);
                ad.put(PHDbClasses.AlarmDetails.COLUMN_PENDINGINTENT, pendingintentcode);

                db.insert(PHDbClasses.AlarmDetails.TABLE_NAME, null, ad);

                dates.add(Calendar.DATE, -1*i);

                introManager.setPendingIntent(pendingintentcode+1);
            }
        }while(c.moveToNext());
    }

    private Cursor getAllItems(String startdateSerarch){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT a._ID, a.time,a.date, a.tablets, a.active, a.taken, a.next, a.timetaken, a.datetaken ,a.skipdetails, a.skipped,a.snooze, a.snoozetime, b.medName FROM AlarmDetails a " +
                "INNER JOIN AlarmMaster b on a.alarmMasterID = b._ID "+
                "WHERE a.date " + "=" + "'"+startdateSerarch+"'" +
                " ORDER BY a.timeformatted ASC";

        Cursor c = db.rawQuery(sql, null);

        return c;

    }

}






























