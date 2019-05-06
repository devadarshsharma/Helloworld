package com.example.helloworld;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public  class AddReminderAdapter extends RecyclerView.Adapter<AddReminderAdapter.AddReminderViewHolder> {
    private ArrayList<AddReminderItems> maddReminderItems;
    private OnItemClickListener onItemClickListener;
    private static int positions;


    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onTimeClick(int position);
        void onTabClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public int setPosition(){
        return positions;
    }

    public  class AddReminderViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textViewTime;
        public TextView textViewTablet;


        public AddReminderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewsw);
            textViewTime = itemView.findViewById(R.id.time);
            textViewTablet = itemView.findViewById(R.id.tablets);



            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            textViewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            PillReminderEntryDetails l = PillReminderEntryDetails.instance;
                            if (l != null) {
                                l.showToast();
                                listener.onTimeClick(position);
                            }
                        }
                    }
                }
            });

            textViewTablet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            PillReminderEntryDetails l = PillReminderEntryDetails.instance;
                            if (l != null) {
                                l.openTabletDialog("Adapter");
                                listener.onTabClick(position);
                            }
                        }
                    }
                }
            });
        }
    }



    public AddReminderAdapter(ArrayList<AddReminderItems> addReminderItems){
        maddReminderItems = addReminderItems;
    }

    @NonNull
    @Override
    public AddReminderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addremindertimes, viewGroup, false);
       AddReminderViewHolder avh = new AddReminderViewHolder(v, onItemClickListener);
       return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AddReminderViewHolder addReminderViewHolder, int i) {
        AddReminderItems addReminderItems = maddReminderItems.get(i);

        addReminderViewHolder.imageView.setImageResource(addReminderItems.getImageView());
        addReminderViewHolder.textViewTime.setText(addReminderItems.getTextViewTime());
        addReminderViewHolder.textViewTablet.setText(addReminderItems.getTextViewTablets());
    }

    @Override
    public int getItemCount() {
        return maddReminderItems.size();
    }
}
