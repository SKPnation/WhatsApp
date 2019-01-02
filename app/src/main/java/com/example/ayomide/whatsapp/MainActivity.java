package com.example.ayomide.whatsapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.example.ayomide.whatsapp.Adapter.TabsAccessorAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

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
        if(item.getItemId() == R.id.main_find_people_option)
        {

        }

        return true;
    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent( MainActivity.this, LoginActivity.class );
        startActivity( loginIntent );
    }

    private void SendUserToSettingsActivity()
    {
        Intent settingsIntent = new Intent( MainActivity.this, SettingsActivity.class );
        startActivity( settingsIntent );
    }
}
