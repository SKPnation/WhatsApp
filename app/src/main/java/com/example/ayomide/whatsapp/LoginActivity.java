package com.example.ayomide.whatsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity
{

    FirebaseUser currentUser;
    private Button LoginButton, PhoneLoginButton;
    private MaterialEditText UserEmail, UserPassword;
    private TextView DontHaveAccountLink, ForgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        
        InitializeFields();

        DontHaveAccountLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToRegisterActivity();
            }
        });
    }

    private void InitializeFields()
    {
        LoginButton = findViewById( R.id.login_button );
        PhoneLoginButton = findViewById( R.id.phone_login_button );
        UserEmail = findViewById( R.id.login_email );
        UserPassword = findViewById( R.id.login_password );
        DontHaveAccountLink = findViewById( R.id.dont_have_account_link );
        ForgotPasswordLink = findViewById( R.id.forgot_password_link );
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
        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
