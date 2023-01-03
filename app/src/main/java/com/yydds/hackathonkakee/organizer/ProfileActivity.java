package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Organizer;

import java.text.SimpleDateFormat;

public class ProfileActivity extends AppCompatActivity {

    TextView nameTV, emailTV, birthDateTV, genderTV, phoneNumberTV, jobPositionTV, employerTV, pageTitleTV;

    String organizerID;
    ImageView profilePictureIV, backArrowIV;
    FloatingActionButton editProfileBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        organizerID = getIntent().getStringExtra("organizerID");

        initComponents();
        assignValue();
    }

    private void initComponents() {
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        birthDateTV = findViewById(R.id.birthDateTV);
        genderTV = findViewById(R.id.genderTV);
        phoneNumberTV = findViewById(R.id.phoneNumberTV);
        jobPositionTV = findViewById(R.id.jobPositionET);
        employerTV = findViewById(R.id.employerTV);
        profilePictureIV = findViewById(R.id.profilePictureIV);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        pageTitleTV = findViewById(R.id.pageTitleTv);
        backArrowIV = findViewById(R.id.backArrowIv);

        pageTitleTV.setText("Profile");

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("organizerID", organizerID);
                startActivity(intent);
            }
        });
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void assignValue() {
        DocumentReference organizerDR = FirebaseFirestore.getInstance().collection("Organizers").document(organizerID);
        organizerDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Organizer organizer = documentSnapshot.toObject(Organizer.class);
                if (!organizer.getProfilePicUrl().isEmpty()) Picasso.get().load(organizer.getProfilePicUrl()).into(profilePictureIV);
                nameTV.setText(organizer.getName());
                emailTV.setText(organizer.getEmail());
                birthDateTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(organizer.getBirthDate().toDate()));
                genderTV.setText(organizer.getGender());
                phoneNumberTV.setText(organizer.getPhoneNumber());
                jobPositionTV.setText(organizer.getJobPosition());
                employerTV.setText(organizer.getEmployer());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignValue();
    }
}