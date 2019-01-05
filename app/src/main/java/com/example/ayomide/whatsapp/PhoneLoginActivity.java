 package com.example.ayomide.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

 public class PhoneLoginActivity extends AppCompatActivity
{

    private MaterialEditText InputPhoneNumber, InputVerificationCode;
    private Button SendVerificationCodeButton, VerifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_phone_login );


        InitializeFields();


        SendVerificationCodeButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
            }
        });


    }



    private void InitializeFields()
    {
        InputPhoneNumber = findViewById(R.id.phone_number_input);
        InputVerificationCode = findViewById(R.id.verification_code_input);
        SendVerificationCodeButton = findViewById(R.id.send_ver_code_button);
        VerifyButton = findViewById(R.id.verify_button);
    }
}
