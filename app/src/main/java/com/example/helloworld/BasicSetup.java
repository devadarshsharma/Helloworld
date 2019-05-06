package com.example.helloworld;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BasicSetup extends AppCompatActivity implements View.OnClickListener{

    private EditText date;
    private EditText name;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private RadioButton maleRadio, femaleRadio;
    private EditText height, weight;
    private Button submit;
    private String Gender;
    private String uid;
    private ProgressDialog progressBar;
    private SQLiteDatabase mDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_basic_setup);

        PHDbHelper phDbHelper = new PHDbHelper(this);
        mDataBase = phDbHelper.getWritableDatabase();

        date = (EditText) findViewById(R.id.DOB);
        name = (EditText) findViewById(R.id.Name);
        maleRadio = (RadioButton) findViewById(R.id.male);
        femaleRadio = (RadioButton) findViewById(R.id.female);
        submit = (Button) findViewById(R.id.submit);
        height = (EditText) findViewById(R.id.Height);
        weight = (EditText) findViewById(R.id.Weight);
        uid = getIntent().getStringExtra("UID");
        progressBar = new ProgressDialog(this);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BasicSetup.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String dates = dayOfMonth+"/"+month+"/"+year;
                date.setText(dates);
            }
        };

        submit.setOnClickListener(BasicSetup.this);
        maleRadio.setOnClickListener(BasicSetup.this);
        femaleRadio.setOnClickListener(BasicSetup.this);


    }

    @Override
    public void onClick(View v) {
            switch  (v.getId()){
               case R.id.submit:
                    DatabaseReference addUserDetails = FirebaseDatabase.getInstance().getReference().child("UserDetails").child(uid);

                   final String Name = name.getText().toString();
                   final String DOB = date.getText().toString();
                   final String Height = height.getText().toString();
                   final String Weight = weight.getText().toString();

                    if(maleRadio.isChecked()){
                        Gender = "Male";
                        Toast.makeText(this, Gender,Toast.LENGTH_SHORT).show();

                    }

                    if(femaleRadio.isChecked()){
                        Gender = "Female";
                        Toast.makeText(this, Gender,Toast.LENGTH_SHORT).show();

                    }

                    Map newPost = new HashMap();
                    newPost.put("Name", Name);
                    newPost.put("DOB", DOB);
                    newPost.put("Gender", Gender);
                    newPost.put("Height", Height);
                    newPost.put("Weight", Weight);
                   progressBar.setMessage("Registering User...");
                   progressBar.show();

                   if (uid.trim().length() == 0 || Name.trim().length() == 0 || DOB.trim().length() == 0 || Gender.trim().length() == 0 || Height.trim().length() == 0 || Weight.trim().length() == 0){
                       progressBar.cancel();
                       Toast.makeText(BasicSetup.this, "Please fill all the values", Toast.LENGTH_SHORT).show();
                       return;
                   }
                    addUserDetails.setValue(newPost, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError != null){
                                progressBar.cancel();
                                Toast.makeText(BasicSetup.this, "Some error occurred, try again", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressBar.cancel();
                                ContentValues cv = new ContentValues();
                                cv.put(PHDbClasses.BasicSetupEntry.COLUMN_USERID, uid);
                                cv.put(PHDbClasses.BasicSetupEntry.COLUMN_NAME, Name);
                                cv.put(PHDbClasses.BasicSetupEntry.COLUMN_DOB, DOB);
                                cv.put(PHDbClasses.BasicSetupEntry.COLUMN_GENDER, Gender);
                                cv.put(PHDbClasses.BasicSetupEntry.COLUMN_HEIGHT, Height);
                                cv.put(PHDbClasses.BasicSetupEntry.COLUMN_WEIGHT, Weight);

                                long ret = mDataBase.insert(PHDbClasses.BasicSetupEntry.TABLE_NAME, null, cv);
                                Log.d("Values of I = ", "********************************************************** "+ ret + "****************************");
                                Toast.makeText(BasicSetup.this, "User Details Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(BasicSetup.this, HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    });



                    break;

            }
    }
}
