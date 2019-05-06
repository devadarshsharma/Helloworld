package com.example.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BMI extends AppCompatActivity {

    private EditText height;
    private EditText weight;
    private TextView results;
    private Button butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        height = (EditText) findViewById(R.id.heightEditText);
        weight = (EditText) findViewById(R.id.weightBMI);
        results = (TextView) findViewById(R.id.result);

        butt = (Button) findViewById(R.id.button_bmi);

        butt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                calculateBMI(v);
            }
        });
    }

    public void calculateBMI(View v){
        String heightStr = height.getText().toString();
        String weightStr = weight.getText().toString();

        if (heightStr != null && !"".equals(heightStr) && weightStr != null && !"".equals(weightStr)){
            float heightValue = Float.parseFloat(heightStr)/100;
            float weightValue = Float.parseFloat(weightStr);

            float bmi = weightValue / (heightValue * heightValue);

            displayBMI(bmi);
        }
    }

    public void displayBMI(float bmi){
        String bmiLabel = "";

        if(Float.compare(bmi,15f) <=0){
            bmiLabel = getString(R.string.very_severely_underweight);
        }else if (Float.compare(bmi, 15f) > 0 && Float.compare(bmi, 16f) <=0){
            bmiLabel = getString(R.string.severely_underweight);
        }else if (Float.compare(bmi, 16f) > 0 && Float.compare(bmi, 18.5f) <=0){
            bmiLabel = getString(R.string.underweight);
        }else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <=0){
            bmiLabel = getString(R.string.normal);
        }else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <=0){
            bmiLabel = getString(R.string.overweight);
        }else if (Float.compare(bmi, 30f) > 0 && Float.compare(bmi, 35f) <=0){
            bmiLabel = getString(R.string.obese_class_i);
        }else if (Float.compare(bmi, 35f) > 0 && Float.compare(bmi, 40f) <=0){
            bmiLabel = getString(R.string.obese_class_ii);
        }else {
            bmiLabel = getString(R.string.obese_class_iii);
        }

        bmiLabel = bmi + "\n\n" + bmiLabel;
        results.setText(bmiLabel);
    }

}
