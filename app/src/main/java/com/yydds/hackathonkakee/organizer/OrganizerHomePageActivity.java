package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.general.LoginActivity;

public class OrganizerHomePageActivity extends AppCompatActivity {

    ImageView logoutIV;
    Button createHackathonBtn, myHackathonBtn;
    String organizerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home_page);
        organizerID = getIntent().getStringExtra("organizerID");
//        Toast.makeText(this, "" + organizerID, Toast.LENGTH_SHORT).show();

        initializeComponent();
    }

    private void initializeComponent() {
        createHackathonBtn = findViewById(R.id.createHackathonBtn);
        myHackathonBtn = findViewById(R.id.myHackathonBtn);
        logoutIV = findViewById(R.id.logoutIV);

        createHackathonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerHomePageActivity.this, CreateNewHackathonActivity.class);
                System.out.println(organizerID);
                intent.putExtra("organizerID", organizerID);
                startActivity(intent);
            }
        });
        myHackathonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO open my hackathon activity
            }
        });
        logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OrganizerHomePageActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}