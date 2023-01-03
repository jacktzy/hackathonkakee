package com.yydds.hackathonkakee.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HackathonSettingActivity extends AppCompatActivity {
    private MaterialButton editDetailsBtn, showParticipantsBtn, showTeamsBtn, announcementBtn, newsBtn;
    ImageView backArrowIv, hackathonIcon;
    TextView pageTitleTv;
    String organizerID, hackathonID, hackathonName;
    TextView hackathonTitle, date, mode, venue, shortDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon_setting);
        organizerID = getIntent().getStringExtra("organizerID");
        hackathonID = getIntent().getStringExtra("hackathonID");
        hackathonName = getIntent().getStringExtra("hackathonName");

        initializeComponent();
    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText(hackathonName);
        backArrowIv = findViewById(R.id.backArrowIv);
        editDetailsBtn = findViewById(R.id.editDetailsBtn);
        showParticipantsBtn = findViewById(R.id.showParticipantsBtn);
        showTeamsBtn = findViewById(R.id.showTeamsBtn);
        announcementBtn = findViewById(R.id.announcementBtn);
        newsBtn = findViewById(R.id.newsBtn);
        hackathonTitle = findViewById(R.id.hackathonTitle);
        date = findViewById(R.id.date);
        mode = findViewById(R.id.mode);
        venue = findViewById(R.id.venue);
        shortDesc = findViewById(R.id.shortDesc);
        hackathonIcon = findViewById(R.id.hackathonIcon);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Hackathon hackathon = documentSnapshot.toObject(Hackathon.class);
                String startDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getStartDateTS().toDate());
                String endDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getEndDateTS().toDate());
                String period = startDate + " - " + endDate;

                Picasso.get().load(hackathon.getIconUri()).into(hackathonIcon);
                hackathonTitle.setText(hackathon.getName());
                date.setText(period);
                mode.setText(hackathon.getMode());
                venue.setText(hackathon.getVenue());
                shortDesc.setText(hackathon.getShortDesc());
            }
        });

        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, CreateNewHackathonActivity.class);
                intent.putExtra("organizerID", organizerID);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });
        showParticipantsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ShowParticipants.class);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });
        showTeamsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ShowTeams.class);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });
        announcementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ManageAnnouncementActivity.class);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });
        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ManageNewsActivity.class);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();
//        if (hackathonID == null || hackathonID.isEmpty()) {
//            finish();
//        }
//    }
}