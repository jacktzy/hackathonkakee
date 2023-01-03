package com.yydds.hackathonkakee.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Announcement;

public class ManageAnnouncementActivity extends AppCompatActivity {
    Button createAnnouncementBtn;
    RecyclerView recyclerView;
    ImageView backArrowIv;
    TextView pageTitleTv;
    String hackathonID;
    AnnouncementAdapter announcementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_announcement);
        hackathonID = getIntent().getStringExtra("hackathonID");

        initializeComponent();
        setupRecycleView();
    }

    private void initializeComponent() {
        createAnnouncementBtn = findViewById(R.id.createNewsBtn);
        pageTitleTv = findViewById(R.id.pageTitleTv);
        backArrowIv = findViewById(R.id.backArrowIv);
        recyclerView = findViewById(R.id.recyclerview);

        pageTitleTv.setText("Announcement");
        createAnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageAnnouncementActivity.this, CreateAnnouncementActivity.class);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });

        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupRecycleView() {
        Query query = FirebaseFirestore.getInstance().collection("Announcements")
                .whereEqualTo("hackathonID", hackathonID)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Announcement> options = new FirestoreRecyclerOptions.Builder<Announcement>().setQuery(query, Announcement.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementAdapter = new AnnouncementAdapter(options, this);
        recyclerView.setAdapter(announcementAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        announcementAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        announcementAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        announcementAdapter.notifyDataSetChanged();
    }
}
