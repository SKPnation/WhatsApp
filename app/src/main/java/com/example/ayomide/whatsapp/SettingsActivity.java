package com.example.ayomide.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{

    private Button UpdateAccountSettings;
    private MaterialEditText userName, userStatus;
    private CircleImageView userProfileImage;

    private String currentUserID;
    FirebaseAuth mAuth;
    DatabaseReference RootRef;

    private static final int GalleryPick = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();


        InitializeFields();


        userName.setVisibility( View.INVISIBLE );


        UpdateAccountSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateSettings();
            }
        });


        RetrieveUserInfo();


        userProfileImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ToAccessPhotoGallery();
            }
        });
    }



    private void InitializeFields()
    {

        UpdateAccountSettings = findViewById( R.id.update_settings_button );
        userName = findViewById( R.id.set_user_name );
        userStatus = findViewById( R.id.set_profile_status );
        userProfileImage = findViewById( R.id.set_profile_image );
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data );
        //this will get that image(i.e the result of that image)

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
        }

    }



    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();

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
                        public void onComplete(@NonNull Task<Void> task)
                        {
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

    private void RetrieveUserInfo()
    {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("status"))))
                         {
                             String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                             String retrieveStatus = dataSnapshot.child("status").getValue().toString();
                             //String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                             userName.setText(retrieveUserName);
                             userStatus.setText(retrieveStatus);

                         }
                         else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                         {
                             String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                             String retrieveStatus = dataSnapshot.child("status").getValue().toString();

                             userName.setText(retrieveUserName);
                             userStatus.setText(retrieveStatus);

                         }
                         else
                             {
                                 userName.setVisibility( View.VISIBLE );
                                 Toast.makeText(SettingsActivity.this, "Please set & update your profile information", Toast.LENGTH_SHORT).show();
                             }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void ToAccessPhotoGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
}
