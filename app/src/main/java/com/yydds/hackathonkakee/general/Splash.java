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
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(currentUser.getUid());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getString("role").equals("Participant")) {
                                startActivity(new Intent(getApplicationContext(), ParticipantHomePageActivity.class));
                                finish();
                            } else if (documentSnapshot.getString("role").equals("Organizer")) {
                                startActivity(new Intent(getApplicationContext(), OrganizerHomePageActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        }, 1000);
    }
}