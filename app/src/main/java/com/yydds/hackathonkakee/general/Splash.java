package com.yydds.hackathonkakee.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.organizer.OrganizerHomePageActivity;
import com.yydds.hackathonkakee.participant.ParticipantHomePageActivity;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                    finish();
                } else {
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Participants").document(currentUser.getUid());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Participant participant = documentSnapshot.toObject(Participant.class);
                                Intent intent = new Intent(getApplicationContext(), ParticipantHomePageActivity.class);
                                intent.putExtra("participantID", currentUser.getUid());
                                intent.putExtra("participantName", participant.getName());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), OrganizerHomePageActivity.class);
                                intent.putExtra("organizerID", currentUser.getUid());
                                startActivity(intent);
                            }
                            finish();
                        }
                    });
                }
            }
        }, 1000);
    }
}