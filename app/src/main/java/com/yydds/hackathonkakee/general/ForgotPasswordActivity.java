package com.yydds.hackathonkakee.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.yydds.hackathonkakee.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText emailEt;
    Button resetPasswordBtn;
    TextView loginTvBtn;
    ProgressBar resetPasswordLoadPB;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        initializeComponents();
    }

    private void initializeComponents() {
        emailEt = findViewById(R.id.emailEt);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        loginTvBtn = findViewById(R.id.loginTvBtn);
        resetPasswordLoadPB = findViewById(R.id.resetPasswordLoadPB);

        loginTvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emailEt.getText().toString();
        if (email.isEmpty()) {
            emailEt.setError("Please provide email.");
            emailEt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Email is invalid.");
            emailEt.requestFocus();
            return;
        }
        changeInProgress(true);
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgotPasswordActivity.this, "Please check your email to reset your password", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPasswordActivity.this, "Reset password failed, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        changeInProgress(false);
        finish();
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            resetPasswordLoadPB.setVisibility(View.VISIBLE);
            resetPasswordBtn.setVisibility(View.INVISIBLE);
        } else {
            resetPasswordLoadPB.setVisibility(View.INVISIBLE);
            resetPasswordBtn.setVisibility(View.VISIBLE);
        }
    }
}