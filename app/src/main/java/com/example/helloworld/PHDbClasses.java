package com.example.helloworld;

import android.provider.BaseColumns;

public class PHDbClasses {

    private PHDbClasses(){}

    public static final class BasicSetupEntry implements BaseColumns{
        public static final String TABLE_NAME = "basicSetup";
        public static final String COLUMN_USERID= "userID";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_DOB= "dob";
        public static final String COLUMN_GENDER= "gender";
        public static final String COLUMN_HEIGHT= "height";
        public static final String COLUMN_WEIGHT= "weight";
        public static final String COLUMN_TIMESTAMP= "TIMESTAMP";
    }

    public static final class AlarmMaster implements BaseColumns{
        public static final String TABLE_NAME = "AlarmMaster";
        public static final String COLUMN_MEDNAME= "medName";
        public static final String COLUMN_COMPANY= "company";
        public static final String COLUMN_INTAKEADVISE= "intakeAdvise";
        public static final String COLUMN_STARTDATE= "startDate";
        public static final String COLUMN_DURATION= "duration";
        public static final String COLUMN_EVERYDAY= "everyday";
        public static final String COLUMN_XHOURS= "xhours";
        public static final String COLUMN_SPECIFICDAYS= "specificdays";
        public static final String COLUMN_FIRSTINTAKE= "firstintake";
        public static final String COLUMN_ACTIVE= "active";
        public static final String COLUMN_DATECREATED= "datecreated";
        public static final String COLUMN_XDAY= "xdays";
    }

    public static final class AlarmDetails implements BaseColumns{
        public static final String TABLE_NAME = "AlarmDetails";
        public static final String COLUMN_ALARMMASTERID= "alarmMasterID";
        public static final String COLUMN_TIME= "time";
        public static final String COLUMN_DATE= "date";
        public static final String COLUMN_STARTDATE= "startDate";
        public static final String COLUMN_TABLETS= "tablets";
        public static final String COLUMN_ACTIVE= "active";
        public static final String COLUMN_TAKEN= "taken";
        public static final String COLUMN_NEXT= "next";
        public static final String COLUMN_DATECREATED= "datecreated";
        public static final String COLUMN_TIMETAKEN= "timetaken";
        public static final String COLUMN_DATECTAKEN= "datetaken";
        public static final String COLUMN_SKIPDETAILS= "skipdetails";
        public static final String COLUMN_SKIPPED= "skipped";
        public static final String COLUMN_SNOOZE = "snooze";
        public static final String COLUMN_SNOOZETIME= "snoozetime";
        public static final String COLUMN_PENDINGINTENT = "pendingitent";
        public static final String COLUMN_TIMEFORMATTED = "timeformatted";

    }
}
