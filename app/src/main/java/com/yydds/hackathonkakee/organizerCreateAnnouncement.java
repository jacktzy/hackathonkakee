package com.yydds.hackathonkakee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class organizerCreateAnnouncement extends AppCompatActivity {
    ImageView backArrowIv;
    TextView pageTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_create_announcement);


        initializeComponent();
    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText("Create Announcement");
        backArrowIv = findViewById(R.id.backArrowIv);
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}