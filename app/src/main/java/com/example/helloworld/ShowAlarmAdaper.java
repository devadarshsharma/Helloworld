package com.example.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ShowAlarmAdaper extends RecyclerView.Adapter<ShowAlarmAdaper.ShowAlarmViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener onItemClickListener;
    private String ids;

    public interface OnItemClickListener{
        void onItemClick(long id);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public ShowAlarmAdaper(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public class ShowAlarmViewHolder extends RecyclerView.ViewHolder{

        public TextView timeText;
        public TextView medText;
        public TextView tabletText;
        public TextView Description;
        public TextView SkippedDet;
        public CardView relativeLayout;
        public ImageView check, cancel , skipped, snooze;

        public ShowAlarmViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            timeText = itemView.findViewById(R.id.timeAlarm);
            medText = itemView.findViewById(R.id.medanme);
            tabletText = itemView.findViewById(R.id.tabletsrecycleview);
            relativeLayout = itemView.findViewById(R.id.RecycleShowAlarm);
            Description = itemView.findViewById(R.id.Description);
            check = itemView.findViewById(R.id.whitechecks);
            cancel=itemView.findViewById(R.id.cancels);
            skipped = itemView.findViewById(R.id.skipped);
            SkippedDet = itemView.findViewById(R.id.skippedtext);
            snooze = itemView.findViewById(R.id.snooze);

            check.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            skipped.setVisibility(View.GONE);
            SkippedDet.setVisibility(View.GONE);

//            timeText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(listener != null){
//                        int positions = getAdapterPosition();
//                        if(positions != RecyclerView.NO_POSITION) {
//                            long position = (long) itemView.getTag();
//                            listener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }

    @NonNull
    @Override
    public ShowAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.showalarm, viewGroup, false);
        return new ShowAlarmViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAlarmViewHolder showAlarmViewHolder, final int i) {
        if(!mCursor.moveToPosition(i)){
            return;
        }

        Date fromDB, current;
        Calendar calendar = Calendar.getInstance();

        long ID = mCursor.getLong(mCursor.getColumnIndex(PHDbClasses.AlarmDetails._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmMaster.COLUMN_MEDNAME));
        String Time = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIME));
        String tablet = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TABLETS));
        String timetaken = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TIMETAKEN));
        String Date = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_DATE));
        int taken = mCursor.getInt(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_TAKEN));
        int skipped = mCursor.getInt(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_SKIPPED));
        String skippedDets = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_SKIPDETAILS));
        int snooze = mCursor.getInt(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_SNOOZE));
        String snoozeDet = mCursor.getString(mCursor.getColumnIndex(PHDbClasses.AlarmDetails.COLUMN_SNOOZETIME));

        showAlarmViewHolder.medText.setText(name);
        showAlarmViewHolder.timeText.setText(Time);
        showAlarmViewHolder.tabletText.setText(tablet);
        showAlarmViewHolder.itemView.setTag(ID);
        ids = String.valueOf(ID);

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");


        showAlarmViewHolder.snooze.setVisibility(View.GONE);

        String Times = dateFormat.format(calendar.getTime());
        String Today = mdformat.format(calendar.getTime());
        try {
            fromDB = formatter.parse(Time);
            current = formatter.parse(Times);
            Date date = mdformat.parse(Date);
            Date today = mdformat.parse(Today);

            if((fromDB.compareTo(current) < 0) && (taken == 0) && (skipped == 0) && (date.compareTo(today) == 0) || (date.compareTo(today))< 0){
                showAlarmViewHolder.Description.setText("Pill Missed");
                showAlarmViewHolder.Description.setTextColor(Color.rgb(76,1,1));
                showAlarmViewHolder.check.setVisibility(View.GONE);
                showAlarmViewHolder.skipped.setVisibility(View.GONE);
                showAlarmViewHolder.cancel.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(taken == 1){
            showAlarmViewHolder.Description.setText("Pill Taken at "+timetaken);
            showAlarmViewHolder.Description.setTextColor(Color.rgb(1,76,10));
            showAlarmViewHolder.check.setVisibility(View.VISIBLE);
            showAlarmViewHolder.cancel.setVisibility(View.GONE);
            showAlarmViewHolder.skipped.setVisibility(View.GONE);
        }

        if(skipped == 1){
            showAlarmViewHolder.SkippedDet.setText(skippedDets);
            showAlarmViewHolder.SkippedDet.setTextColor(Color.rgb(219,123,21));
            showAlarmViewHolder.check.setVisibility(View.GONE);
            showAlarmViewHolder.cancel.setVisibility(View.GONE);
            showAlarmViewHolder.skipped.setVisibility(View.VISIBLE);
            showAlarmViewHolder.SkippedDet.setVisibility(View.VISIBLE);
            showAlarmViewHolder.Description.setText("Pill skipped at "+timetaken);
        }

        if(snooze == 1){
            showAlarmViewHolder.Description.setText("Snoozed unitl "+snoozeDet);
            showAlarmViewHolder.Description.setTextColor(Color.rgb(10,36,79));
            showAlarmViewHolder.check.setVisibility(View.GONE);
            showAlarmViewHolder.cancel.setVisibility(View.GONE);
            showAlarmViewHolder.skipped.setVisibility(View.GONE);
            showAlarmViewHolder.snooze.setVisibility(View.VISIBLE);
        }

        showAlarmViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long array[] = new long[mCursor.getCount()];
                int in = 0;

                mCursor.moveToFirst();
                while (!mCursor.isAfterLast()) {
                    array[in] = mCursor.getLong(0);
                    in++;
                    mCursor.moveToNext();
                }

               PillReminder l = PillReminder.instance;
                l.showAlarmDetails(array[i]);
                //Toast.makeText(mContext, String.valueOf(array[i]), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null){
            notifyDataSetChanged();
        }
    }
}
