package com.yydds.hackathonkakee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yydds.hackathonkakee.organizer.ShowParticipants;
import com.yydds.hackathonkakee.organizer.ShowTeams;

public class organizerMainPage extends AppCompatActivity {
    private TextView editDetails, showParticipants, showTeams, announcement, manageNews;
    ImageView backArrowIv;
    TextView pageTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_main_page);

        initializeComponent();
    }

    private void initializeComponent() {
        String hackathonEvent = "HACKATHON_NAME";
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText(hackathonEvent);
        backArrowIv = findViewById(R.id.backArrowIv);
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editDetails = (TextView) findViewById(R.id.button5);
        showParticipants = (TextView) findViewById(R.id.button);
        showTeams = (TextView) findViewById(R.id.button2);
        announcement = (TextView) findViewById(R.id.button3);

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(organizerMainPage.this, organizerEditDetails.class);
//                startActivity(intent);
            }
        });

        showParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(organizerMainPage.this, ShowParticipants.class);
                startActivity(intent);
            }
        });

        showTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(organizerMainPage.this, ShowTeams.class);
                startActivity(intent);
            }
        });

        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(organizerMainPage.this, organizerAnnouncement.class);
                startActivity(intent);
            }
        });
    }
}