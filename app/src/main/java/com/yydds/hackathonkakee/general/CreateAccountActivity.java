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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    String[] roles = {"Participant", "Organizer"};
    String role = "";

    EditText nameEt, emailEt, passwordEt, confirmPasswordEt;
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
        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
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
        String name = nameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirmPassword = confirmPasswordEt.getText().toString();

        if (!validateData(name, email, password, confirmPassword, role)) return;

        createAccountInFirebase(name, email, password, role);

    }

    private boolean validateData(String name, String email, String password, String confirmPassword, String role) {
        if (name.isEmpty()) {
            nameEt.setError("Please enter name.");
            nameEt.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            emailEt.setError("Please enter email.");
            emailEt.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Email is invalid");

            return false;
        }
        if (password.isEmpty()) {
            passwordEt.setError("Please enter password");
            passwordEt.requestFocus();
            return false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordEt.setError("Please confirm password.");
            confirmPasswordEt.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            passwordEt.setError("Password length must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEt.setError("Password not matched");
            return false;
        }
        if (role.isEmpty()) {
            autoCompleteTV.setError("Please select role");
            return false;
        }
        return true;
    }

    private void createAccountInFirebase(String name, String email, String password, String role) {
        changeInProgress(true);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Save user data in firestore database.
                FirebaseUser user = firebaseAuth.getCurrentUser();
                System.out.println(user.getUid() + ", " + user.getEmail());
                DocumentReference documentReference = firebaseFirestore.collection("Users").document(user.getUid());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", name);
                userInfo.put("email", email);
                userInfo.put("role", role);
                System.out.println(userInfo);
                documentReference.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.signOut();
                        Toast.makeText(CreateAccountActivity.this, "Successfully create account.", Toast.LENGTH_SHORT).show();
                    }
                });
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