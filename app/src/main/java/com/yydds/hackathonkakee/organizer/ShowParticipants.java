package com.yydds.hackathonkakee.organizer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

import java.util.ArrayList;

public class ShowParticipants extends AppCompatActivity {
    RecyclerView participantsRV;
    ParticipantsAdapter adapter;
    ImageView backArrowIv;
    TextView pageTitleTv;

    String hackathonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_show_participants);
        hackathonID = getIntent().getStringExtra("hackathonID");

        initializeComponent();
        setupRecycleView();
    }

    private void setupRecycleView() {
        System.out.println(hackathonID);
        Query query = FirebaseFirestore.getInstance().collection("Participants")
                .whereArrayContains("participatedHackathonId", hackathonID);
        FirestoreRecyclerOptions<Participant> options = new FirestoreRecyclerOptions.Builder<Participant>().setQuery(query, Participant.class).build();
        participantsRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParticipantsAdapter(options, this, hackathonID);
        participantsRV.setAdapter(adapter);
    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        backArrowIv = findViewById(R.id.backArrowIv);
        participantsRV = findViewById(R.id.participantsRV);

        pageTitleTv.setText("Show Participants");
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}