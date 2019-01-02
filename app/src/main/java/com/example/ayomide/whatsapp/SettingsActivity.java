package com.example.ayomide.whatsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{

    private Button UpdateAccountSettings;
    private MaterialEditText Username, UserStatus;
    private CircleImageView UserProfileImage;

    private String currentUserID;
    FirebaseAuth mAuth;
    DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();


        InitializeFields();

        UpdateAccountSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateSettings();
            }
        });
    }


    private void InitializeFields()
    {

        UpdateAccountSettings = findViewById( R.id.update_settings_button );
        Username = findViewById( R.id.set_user_name );
        UserStatus = findViewById( R.id.set_profile_status );
        UserProfileImage = findViewById( R.id.set_profile_image );
    }


    private void UpdateSettings()
    {
        String setUserName = Username.getText().toString();
        String setStatus = UserStatus.getText().toString();

        if(TextUtils.isEmpty(setUserName))
        {
            Toast.makeText( this, "Please enter your username...", Toast.LENGTH_SHORT ).show();
        }
        if(TextUtils.isEmpty(setStatus))
        {
            Toast.makeText( this, "Please type your status...", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            //To save this data inside firebase database, i'll use HashMap
            HashMap<String, String> ProfileMap = new HashMap<>();
            ProfileMap.put("uid", currentUserID);
            ProfileMap.put("name", setUserName);
            ProfileMap.put("status", setStatus);

            RootRef.child("Users").child(currentUserID).setValue(ProfileMap)
                    .addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText( SettingsActivity.this, "Error : " + message, Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
        }
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
