package com.example.ayomide.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class GroupChatActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_group_chat );

        InitializeFields();
    }

    private void InitializeFields()
    {
        mToolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group Name");

        sendMessageButton = findViewById(R.id.send_message_button);
        userMessageInput = findViewById(R.id.input_group_message);
        mScrollView = findViewById(R.id.my_scroll_view);
        displayTextMessages = findViewById(R.id.group_chat_text_display);
    }
}
