package com.example.ayomide.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity
{

    private Button CreateAccountButton;
    private MaterialEditText UserEmail, UserPassword;
    private TextView AlreadyHaveAccountLink;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );


        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference( );

        InitializeFields();

        AlreadyHaveAccountLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToLoginActivity();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateNewAccount();
            }
        });
    }

    private void InitializeFields()
    {
        CreateAccountButton = findViewById( R.id.register_button );
        UserEmail = findViewById( R.id.register_email );
        UserPassword = findViewById( R.id.register_password );
        AlreadyHaveAccountLink = findViewById( R.id.already_have_account_link );

        loadingBar = new ProgressDialog(this);
    }

    private void CreateNewAccount()
    {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if (!validateInputs( email, password))
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating a new account for you...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(currentUserID).setValue("");

                                SendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText( RegisterActivity.this, "Error : " + message, Toast.LENGTH_LONG ).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }


    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private boolean validateInputs(String email, String password)
    {
        if (email.isEmpty()) {
            UserEmail.setError("email required");
            UserEmail.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            UserEmail.setError("Invalid email");
            UserEmail.requestFocus();
            Toast.makeText(RegisterActivity.this, "Put in a valid email", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (password.isEmpty()) {
            UserPassword.setError("password required");
            UserPassword.requestFocus();
            return true;
        }
        if (password.length() < 6) {
            UserPassword.setError("Password shouldn't be less than six characters");
            UserPassword.requestFocus();
            return true;
        }

        return false;
    }
}
