package com.yydds.hackathonkakee.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Organizer;
import com.yydds.hackathonkakee.classes.Participant;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    String[] roles = {"Participant", "Organizer"};
    String role = "";

    TextInputLayout nameTIL, emailTIL, passwordTIL, confirmPasswordTIL;
    TextInputEditText nameTIET, emailTIET, passwordTIET, confirmPasswordTIET;
    Button createAccountBtn;
    TextView loginTvBtn;
    AutoCompleteTextView autoCompleteTV;
    ProgressBar createAccLoadPB;

    ArrayAdapter<String> adapterItems;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
//        FirebaseFirestore.setLoggingEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        initializeComponents();

    }

    private void initializeComponents() {
        nameTIL = findViewById(R.id.nameTIL);
        nameTIET = findViewById(R.id.nameTIET);
        emailTIL = findViewById(R.id.emailTIL);
        emailTIET = findViewById(R.id.emailTIET);
        passwordTIL = findViewById(R.id.passwordTIL);
        passwordTIET = findViewById(R.id.passwordTIET);
        confirmPasswordTIL = findViewById(R.id.confirmPasswordTIL);
        confirmPasswordTIET = findViewById(R.id.confirmPasswordTIET);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        loginTvBtn = findViewById(R.id.loginTvBtn);
        createAccLoadPB = findViewById(R.id.createAccLoadPB);

        createAccountBtn.setOnClickListener((v) -> createAccount());
        loginTvBtn.setOnClickListener((v) -> finish());

        autoCompleteTV = findViewById(R.id.autoCompleteTV);

        adapterItems = new ArrayAdapter<String>(this, R.layout.login_role_list_view, roles);
        autoCompleteTV.setAdapter(adapterItems);
        autoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                role = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(CreateAccountActivity.this, role, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount() {
        String name = nameTIET.getText().toString();
        String email = emailTIET.getText().toString();
        String password = passwordTIET.getText().toString();
        String confirmPassword = confirmPasswordTIET.getText().toString();

        if (!validateData(name, email, password, confirmPassword, role)) return;

        createAccountInFirebase(name, email, password, role);

    }

    private boolean validateData(String name, String email, String password, String confirmPassword, String role) {
        boolean isValid = true;
        if (name.isEmpty()) {
            nameTIL.setErrorEnabled(true);
            nameTIL.setError("Please enter name.");
            isValid = false;
        } else {
            nameTIL.setErrorEnabled(false);
        }
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
            passwordTIL.setError("Please enter password");
            isValid = false;
        } else {
            passwordTIL.setErrorEnabled(false);
        }
        if (password.length() < 6) {
            passwordTIL.setError("Password length must be at least 6 characters");
            isValid = false;
        } else {
            passwordTIL.setErrorEnabled(false);
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordTIL.setError("Password not matched");
            isValid = false;
        } else {
            confirmPasswordTIL.setErrorEnabled(false);
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordTIL.setError("Please confirm password.");
            isValid = false;
        } else {
            confirmPasswordTIL.setErrorEnabled(false);
        }
        if (role.isEmpty()) {
            autoCompleteTV.setError("Please select role");
            isValid = false;
        }
        return isValid;
    }

    private void createAccountInFirebase(String name, String email, String password, String role) {
        changeInProgress(true);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Save user data in firestore database.
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (role.equals("Participant")) {
                    DocumentReference documentReference = firebaseFirestore.collection("Participants").document(user.getUid());
                    Participant participant = null;
                    try {
                        participant = new Participant(name, email);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    documentReference.set(participant).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            firebaseAuth.signOut();
                            Toast.makeText(CreateAccountActivity.this, "Create account successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (role.equals("Organizer")) {
                    DocumentReference documentReference = firebaseFirestore.collection("Organizers").document(user.getUid());
                    Organizer organizer = null;
                    try {
                        organizer = new Organizer(name, email);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    documentReference.set(organizer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            firebaseAuth.signOut();
                            Toast.makeText(CreateAccountActivity.this, "Create account successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                changeInProgress(false);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccountActivity.this, "Create account failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            createAccLoadPB.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.INVISIBLE);
        } else {
            createAccLoadPB.setVisibility(View.INVISIBLE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }
}