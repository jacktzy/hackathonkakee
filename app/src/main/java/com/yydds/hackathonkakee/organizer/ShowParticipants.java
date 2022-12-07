package com.yydds.hackathonkakee.organizer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yydds.hackathonkakee.ParticipantAdapter;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

import java.util.ArrayList;

public class ShowParticipants extends AppCompatActivity {
    RecyclerView recyclerView;
    ParticipantAdapter adapter;
    ArrayList<Participant> list;
    ImageView backArrowIv;
    TextView pageTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_show_participants);

        initializeComponent();
//        setupRecycleView();
    }

    private void setupRecycleView() {
        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<>();

//        list.add(new Participant(1, "Wong", "NO idea"));
//        list.add(new Participant(2, "Tan", "Naudas-la"));
//        list.add(new Participant(3, "Jacky", "Indo"));
//        list.add(new Participant(4, "Yang", "Malaysia"));

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