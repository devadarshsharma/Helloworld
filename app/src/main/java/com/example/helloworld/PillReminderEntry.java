package com.example.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class PillReminderEntry extends AppCompatActivity {

    Spinner unitSpinner;
    ArrayAdapter<CharSequence> adapter;
    Button next;
    Toolbar toolbar;
    EditText nameEdit;
    CardView unit;
    String item_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminder_entry);

        unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        //toolbar = (Toolbar) findViewById(R.id.pillReminderToolBar);
        next = (Button) findViewById(R.id.nextbutton);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        unit = (CardView) findViewById(R.id.unit);

        adapter = ArrayAdapter.createFromResource(this, R.array.unit,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        unitSpinner.setSelection(10);


        unit.setVisibility(View.GONE);
        next.setVisibility(View.GONE);

        nameEdit.addTextChangedListener(filterTextWatcher);



        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ " selected", Toast.LENGTH_SHORT).show();
                item_selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PillReminderEntry.this, PillReminderEntryDetails.class);
                i.putExtra("Units", item_selected);
                i.putExtra("MedName", nameEdit.getText().toString());
                startActivity(i);
            }
        });

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           if (nameEdit.getText().toString().trim().length() != 0){
               unit.setVisibility(View.VISIBLE);
               next.setVisibility(View.VISIBLE);
           }else{
               unit.setVisibility(View.GONE);
               next.setVisibility(View.GONE);
           }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
