package com.example.ayomide.whatsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.ayomide.whatsapp.Adapter.TabsAccessorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    DatabaseReference RootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        RootRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WhatsApp");

        myViewPager = findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter( myTabsAccessorAdapter );

        myTabLayout = findViewById( R.id.main_tabs );
        myTabLayout.setupWithViewPager( myViewPager );
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //if user isn't authenticated
        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
            {
                VerifyUserExistence();
            }
    }



    private void VerifyUserExistence()
    {
        String currentUserID = mAuth.getCurrentUser().getUid();

        /*under the parent node which is Users, we have different IDs for different users and under
         that currentUserID we'll have name and status */
        RootRef.child("Users").child(currentUserID).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists()))
                {
                    Log.d("TAG", "Welcome");
                }
                else
                    {
                        SendUserToSettingsActivity();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
     {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected( item );

        if(item.getItemId() == R.id.main_logout_option)
        {
            mAuth.signOut();
            SendUserToLoginActivity();
        }
        if(item.getItemId() == R.id.main_settings_option)
        {
            SendUserToSettingsActivity();
        }
        if(item.getItemId() == R.id.main_create_group_option)
        {
            RequestNewGroup();
        }
        if(item.getItemId() == R.id.main_find_people_option)
        {
            SendUserToFindFriendsActivity();
        }

        return true;
    }

    private void RequestNewGroup()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");

        final MaterialEditText groupNameField = new MaterialEditText(MainActivity.this);
        groupNameField.setHint("\t\t\t\t e.g Chinese friends ");
        builder.setView(groupNameField);

        builder.setPositiveButton( "Create \t\t\t", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNameField.getText().toString();

                if(TextUtils.isEmpty(groupName))
                {
                    Toast.makeText( MainActivity.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        CreateNewGroup(groupName);
                    }
            }
        });

        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName)
    {
        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, groupName + " group is created successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent( MainActivity.this, LoginActivity.class );
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity( loginIntent );
        finish();

    }

    private void SendUserToSettingsActivity()
    {
        Intent settingsIntent = new Intent( MainActivity.this, SettingsActivity.class );
        startActivity( settingsIntent );
    }

    private void SendUserToFindFriendsActivity()
    {
        Intent findFriendsIntent = new Intent( MainActivity.this, FindFriendsActivity.class );
        startActivity( findFriendsIntent );
    }
}
