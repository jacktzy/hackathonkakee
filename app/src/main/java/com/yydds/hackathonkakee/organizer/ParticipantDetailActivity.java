package com.yydds.hackathonkakee.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

import java.text.SimpleDateFormat;

public class ParticipantDetailActivity extends AppCompatActivity {

    ImageView profilePictureIV, backArrowTV;
    TextView nameTV, emailTV, birthDateTV, genderTV, phoneNumberTV, insNameTV, fieldMajorTV, levelOfEducationTV, CGPATV, interestFieldTV, jobPositionTV, noResumeAlertTV, pageTitleTV;
    MaterialButton viewResumeBtn;
    String participantID, resumeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_detail);
        participantID = getIntent().getStringExtra("participantID");

        initComponents();
        assignValue();
    }

    private void initComponents() {
        profilePictureIV = findViewById(R.id.profilePictureIV);
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        birthDateTV = findViewById(R.id.birthDateTV);
        genderTV = findViewById(R.id.genderTV);
        phoneNumberTV = findViewById(R.id.phoneNumberTV);
        insNameTV = findViewById(R.id.institutionNameTV);
        fieldMajorTV = findViewById(R.id.fieldMajorTV);
        levelOfEducationTV = findViewById(R.id.levelOfEducationTV);
        CGPATV = findViewById(R.id.CGPATV);
        interestFieldTV = findViewById(R.id.interestFieldTV);
        jobPositionTV = findViewById(R.id.jobPositionTV);
        noResumeAlertTV = findViewById(R.id.noResumeAlertTV);
        viewResumeBtn = findViewById(R.id.viewResumeBtn);
        pageTitleTV = findViewById(R.id.pageTitleTv);
        backArrowTV = findViewById(R.id.backArrowIv);

        backArrowTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void assignValue() {
        System.out.println(participantID);
        DocumentReference df = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Participant participant = documentSnapshot.toObject(Participant.class);
                if (!participant.getProfilePicUrl().isEmpty()) Picasso.get().load(participant.getProfilePicUrl()).into(profilePictureIV);

                pageTitleTV.setText(participant.getName() + "'s Profile");

                nameTV.setText(participant.getName());
                emailTV.setText(participant.getEmail());
                birthDateTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(participant.getBirthDate().toDate()));
                genderTV.setText(participant.getGender());
                phoneNumberTV.setText(participant.getPhoneNumber());
                insNameTV.setText(participant.getInstitutionName());
                fieldMajorTV.setText(participant.getFieldMajor());
                levelOfEducationTV.setText(participant.getLevelOfEducation());
                CGPATV.setText(Double.toString(participant.getCGPA()));
                interestFieldTV.setText(participant.getInterestField());
                interestFieldTV.setText(participant.getInterestJobPos());
                resumeUrl = participant.getResumeUrl();

                if (resumeUrl.isEmpty()) {
                    noResumeAlertTV.setVisibility(View.VISIBLE);
                    viewResumeBtn.setVisibility(View.INVISIBLE);
                } else {
                    noResumeAlertTV.setVisibility(View.INVISIBLE);
                    viewResumeBtn.setVisibility(View.VISIBLE);
                    viewResumeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setType("application/pdf");
                            intent.setData(Uri.parse(resumeUrl));
                            startActivity(intent);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ParticipantDetailActivity.this, "Failed to load profile information", Toast.LENGTH_SHORT).show();;
                return;
            }
        });

    }

}