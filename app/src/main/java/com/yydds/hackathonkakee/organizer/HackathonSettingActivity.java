package com.yydds.hackathonkakee.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yydds.hackathonkakee.R;

public class HackathonSettingActivity extends AppCompatActivity {
    private TextView editDetails, showParticipants, showTeams, announcement, manageNews;
    ImageView backArrowIv;
    TextView pageTitleTv;
    String organizerID, hackathonID, hackathonName;

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
                Intent intent = new Intent(HackathonSettingActivity.this, CreateNewHackathonActivity.class);
                intent.putExtra("organizerID", organizerID);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });

        showParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ShowParticipants.class);
                startActivity(intent);
            }
        });

        showTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ShowTeams.class);
                startActivity(intent);
            }
        });

        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HackathonSettingActivity.this, ManageAnnouncementActivity.class);
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