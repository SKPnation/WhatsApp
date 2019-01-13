 package com.example.ayomide.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

 public class PhoneLoginActivity extends AppCompatActivity
{

    private MaterialEditText InputPhoneNumber, InputVerificationCode;
    private Button SendVerificationCodeButton, VerifyButton;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;

    ProgressDialog loadingBar;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);


        mAuth = FirebaseAuth.getInstance();


        InitializeFields();


        SendVerificationCodeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                //to get phoneNumber input
                String phoneNumber = InputPhoneNumber.getText().toString();

                if(TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(PhoneLoginActivity.this, "Phone number is required", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        loadingBar.setTitle("Phone Verification");
                        loadingBar.setMessage("Your phone is being authenticated, please wait...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                PhoneLoginActivity.this,               // Activity (for callback binding)
                                callbacks);        // OnVerificationStateChangedCallbacks
                    }
            }
        });


        VerifyButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                String verificationCode = InputVerificationCode.getText().toString();

                if(TextUtils.isEmpty(verificationCode))
                {
                    Toast.makeText(PhoneLoginActivity.this, "verification code rquired", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Verification Code");
                    loadingBar.setMessage("Your code is being verified, please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    //After the user enters the verification code that Firebase sent to the user's phone, create a PhoneAuthCredential object,
                    // using the verification code and the verification ID that was passed to the onCodeSent or onCodeAutoRetrievalTimeOut callback

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);

                    //ones the user enters the verification code, we'll call this method
                    signInWithPhoneAuthCredential(credential); //this method will check if it's correct or wrong
                }
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone number, " +
                        "Please enter correct phone number with your country code", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                SendVerificationCodeButton.setVisibility(View.VISIBLE);
                InputPhoneNumber.setVisibility(View.VISIBLE);

                VerifyButton.setVisibility(View.INVISIBLE);
                InputVerificationCode.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token)
            {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(PhoneLoginActivity.this, "Code has been sent", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
            }
        };

    }



    private void InitializeFields()
    {
        InputPhoneNumber = findViewById(R.id.phone_number_input);
        InputVerificationCode = findViewById(R.id.verification_code_input);
        SendVerificationCodeButton = findViewById(R.id.send_ver_code_button);
        VerifyButton = findViewById(R.id.verify_button);
        loadingBar = new ProgressDialog(this);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            loadingBar.setTitle("Phone verification");
                            loadingBar.setMessage("Your phone is being authenticated...");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();

                            Toast.makeText(PhoneLoginActivity.this, "Congratulations you've logged in successfully", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();
                        }
                        else
                            {
                                String message = task.getException().toString();
                                Toast.makeText( PhoneLoginActivity.this, "Error : " + message, Toast.LENGTH_LONG ).show();
                                loadingBar.dismiss();
                            }
                    }
                });
    }



    private void SendUserToMainActivity()
    {
        startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));
        finish();
    }
}
