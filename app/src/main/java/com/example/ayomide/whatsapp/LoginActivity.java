package com.example.ayomide.whatsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        //if user isn't authenticated
        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }



    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity  .this, MainActivity.class);
        startActivity(mainIntent);
    }
}
