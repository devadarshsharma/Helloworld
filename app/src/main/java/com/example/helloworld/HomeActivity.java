package com.example.helloworld;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    ImageButton BMIbutton, pillButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BMIbutton = (ImageButton) findViewById(R.id.bmi_button);
        pillButton = (ImageButton) findViewById(R.id.pill_button);

        BMIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBMI = new Intent(HomeActivity.this, BMI.class);
                startActivity(intentBMI);
            }
        });

        pillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPill = new Intent(HomeActivity.this, PillReminder.class);
                startActivity(intentPill);
            }
        });

    }
}
