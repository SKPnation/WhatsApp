package com.example.ayomide.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity
{

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private Button LoginButton, PhoneLoginButton;
    private MaterialEditText UserEmail, UserPassword;
    private TextView DontHaveAccountLink, ForgotPasswordLink;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        InitializeFields();

        DontHaveAccountLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AllowUserToLogin();
            }
        } );
    }


    private void InitializeFields()
    {
        LoginButton = findViewById( R.id.login_button );
        PhoneLoginButton = findViewById( R.id.phone_login_button );
        UserEmail = findViewById( R.id.login_email );
        UserPassword = findViewById( R.id.login_password );
        DontHaveAccountLink = findViewById( R.id.dont_have_account_link );
        ForgotPasswordLink = findViewById( R.id.forgot_password_link );

        loadingBar = new ProgressDialog( this);
    }


    private void AllowUserToLogin()
    {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText( LoginActivity.this, "Error : " + message, Toast.LENGTH_SHORT ).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
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
