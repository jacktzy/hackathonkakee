package com.yydds.hackathonkakee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yydds.hackathonkakee.classes.Announcement;
import com.yydds.hackathonkakee.organizer.CreateAnnouncement;
import com.yydds.hackathonkakee.organizer.EditAnnouncement;

import java.util.ArrayList;

public class organizerAnnouncement extends AppCompatActivity {
    private Button createAnnouncement;
    RecyclerView recyclerView;
    AnnouncementAdapter adapter;
    ArrayList<Announcement> list1;
    ImageView backArrowIv;
    TextView pageTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_announcement);
        initializeComponent();
        setupRecycleView();

    }

    private void setupRecycleView() {
        recyclerView = findViewById(R.id.recyclerview1);
        list1 = new ArrayList<>();

        list1.add(new Announcement("14-11-2022", "I love Java"));
        list1.add(new Announcement("14-11-2022", "I love Java"));
        list1.add(new Announcement("14-11-2022", "I love Java"));
        list1.add(new Announcement("14-11-2022", "I love Java"));

        adapter = new AnnouncementAdapter(this, list1, new AnnouncementAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Announcement details) {
                Intent intent = new Intent(organizerAnnouncement.this, EditAnnouncement.class);
                startActivity(intent);

//                Toast.makeText(organizerAnnouncement.this, "Make me", Toast.LENGTH_LONG).show();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    private void initializeComponent() {
        createAnnouncement = findViewById(R.id.createAnnouncement);

        createAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(organizerAnnouncement.this, CreateAnnouncement.class);
                startActivity(intent);
            }
        });

        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText("Announcement");
        backArrowIv = findViewById(R.id.backArrowIv);
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}