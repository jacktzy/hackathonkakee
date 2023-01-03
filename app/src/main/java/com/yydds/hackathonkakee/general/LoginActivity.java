package com.yydds.hackathonkakee.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.versionedparcelable.ParcelImpl;

import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.organizer.OrganizerHomePageActivity;
import com.yydds.hackathonkakee.participant.ParticipantHomePageActivity;

public class LoginActivity extends AppCompatActivity {

//    EditText emailEt, passwordEt;
    TextInputEditText emailTIET, passwordTIET;
    TextInputLayout emailTIL, passwordTIL;
    Button loginBtn;
    TextView createAccountTvBtn, forgotPasswordTvBtn;
    ProgressBar login_load_PB;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        initializeComponent();
    }

    private void initializeComponent() {
        emailTIET = findViewById(R.id.emailTIET);
        emailTIL = findViewById(R.id.emailTIL);
        passwordTIET = findViewById(R.id.passwordTIET);
        passwordTIL = findViewById(R.id.passwordTIL);
        loginBtn = findViewById(R.id.loginBtn);
        createAccountTvBtn = findViewById(R.id.createAccountTvBtn);
        forgotPasswordTvBtn = findViewById(R.id.forgotPasswordTvBtn);
        login_load_PB = findViewById(R.id.createAccLoadPB);

        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountTvBtn.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
        forgotPasswordTvBtn.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        
    }

    private void loginUser() {
        String email = emailTIET.getText().toString();
        String password = passwordTIET.getText().toString();
        boolean isValid = true;

        if (email.isEmpty()) {
            emailTIL.setErrorEnabled(true);
            emailTIL.setError("Please enter email.");
            isValid = false;
        } else {
            emailTIL.setErrorEnabled(false);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTIL.setErrorEnabled(true);
            emailTIL.setError("Email is invalid");
            isValid = false;
        } else {
            emailTIL.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            passwordTIL.setErrorEnabled(true);
            passwordTIL.setError("Please enter password.");
            isValid = false;
        } else {
            passwordTIL.setErrorEnabled(false);
        }

        if (!isValid) return;

        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Participants").document(authResult.getUser().getUid());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Participant participant = documentSnapshot.toObject(Participant.class);
                            Intent intent = new Intent(getApplicationContext(), ParticipantHomePageActivity.class);
                            intent.putExtra("participantID", firebaseAuth.getCurrentUser().getUid());
                            intent.putExtra("participantName", participant.getName());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), OrganizerHomePageActivity.class);
                            intent.putExtra("organizerID", firebaseAuth.getCurrentUser().getUid());
                            startActivity(intent);
                        }
                        changeInProgress(false);
                        Toast.makeText(LoginActivity.this, "Login successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        changeInProgress(false);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Wrong email or password.", Toast.LENGTH_SHORT).show();
                changeInProgress(false);
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            login_load_PB.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
        } else {
            login_load_PB.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }


}