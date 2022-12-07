package com.yydds.hackathonkakee;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class organizerShowParticipants extends AppCompatActivity {
    RecyclerView recyclerView;
    ParticipantAdapter adapter;
    ArrayList<Participants> list;
    ImageView backArrowIv;
    TextView pageTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_show_participants);

        initializeComponent();
        setupRecycleView();
    }

    private void setupRecycleView() {
        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<>();

        list.add(new Participants(1, "Wong", "NO idea"));
        list.add(new Participants(2, "Tan", "Naudas-la"));
        list.add(new Participants(3, "Jacky", "Indo"));
        list.add(new Participants(4, "Yang", "Malaysia"));

        adapter = new ParticipantAdapter(this,list);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText("Show Participants");
        backArrowIv = findViewById(R.id.backArrowIv);
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}