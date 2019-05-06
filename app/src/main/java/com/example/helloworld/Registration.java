package com.example.helloworld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private Button bt;
    private EditText textWmail,textpwd;
    private Button Login;

    private ProgressDialog progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //FirebaseApp.initializeApp(Registration.this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressDialog(this);
        bt = (Button) findViewById(R.id.register);
        textWmail = (EditText) findViewById(R.id.email);
        textpwd = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.login);
        String uID = getIntent().getStringExtra("UID");

        bt.setOnClickListener(this);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Login();
            }
        });

    }

    public void Login(){
        Intent intentLogin = new Intent(Registration.this, Login.class);
        startActivity(intentLogin);
    }

    public void registerUSer(){
        String email = textWmail.getText().toString().trim();
        String pwd = textpwd.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Pleasse enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setMessage("Registering User...");
        progressBar.show();

        firebaseAuth.createUserWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.cancel();
                            Toast.makeText(Registration.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
                            String UID = firebaseAuth.getCurrentUser().getUid();
                            Intent basicSetup = new Intent(Registration.this, BasicSetup.class);
                            basicSetup.putExtra("UID", UID );
                            startActivity(basicSetup);
                        }
                        else{

                            progressBar.cancel();
                            Toast.makeText(Registration.this, "Failed to register. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
            if (v == bt){
                registerUSer();
            }
    }
}
