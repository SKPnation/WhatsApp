package com.example.ayomide.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{

    private Button UpdateAccountSettings;
    private EditText Username, UserStatus;
    private CircleImageView UserProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );


        InitializeFields();
    }

    private void InitializeFields()
    {

        UpdateAccountSettings = findViewById( R.id.update_settings_button );
        Username = findViewById( R.id.set_user_name );
        UserStatus = findViewById( R.id.set_profile_status );
        UserProfileImage = findViewById( R.id.set_profile_image );
    }
}
