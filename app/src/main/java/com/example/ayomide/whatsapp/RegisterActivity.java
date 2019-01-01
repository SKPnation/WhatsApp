package com.example.ayomide.whatsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity
{

    private Button CreateAccountButton;
    private MaterialEditText UserEmail, UserPassword;
    private TextView AlreadyHaveAccountLink;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        InitializeFields();

        AlreadyHaveAccountLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToLoginActivity();
            }
        });
    }

    private void InitializeFields()
    {
        CreateAccountButton = findViewById( R.id.register_button );
        UserEmail = findViewById( R.id.register_email );
        UserPassword = findViewById( R.id.register_password );
        AlreadyHaveAccountLink = findViewById( R.id.already_have_account_link );
    }

    private void SendUserToMainActivity()
    {
        Intent registerIntent = new Intent(RegisterActivity .this, MainActivity.class);
        startActivity(registerIntent);
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(RegisterActivity .this, LoginActivity .class);
        startActivity(loginIntent);
    }
}
