package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;

public class OrganizerMyHackathon extends AppCompatActivity {
    TextView pageTitleTv;
    String organizerID;
    RecyclerView recyclerView;
    MyHackathonAdapter hackathonAdapter;
    ImageView backArrowIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_my_hackathon);
        organizerID = getIntent().getStringExtra("organizerID");

        initializeComponents();
        setupRecycleView();
    }

    private void initializeComponents() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        recyclerView = findViewById(R.id.recyclerView);
        backArrowIv = findViewById(R.id.backArrowIv);

        pageTitleTv.setText("My Hackathon");
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupRecycleView() {
        Query query = FirebaseFirestore.getInstance().collection("Hackathons").whereEqualTo("organizerID", organizerID);
        FirestoreRecyclerOptions<Hackathon> options = new FirestoreRecyclerOptions.Builder<Hackathon>().setQuery(query, Hackathon.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hackathonAdapter = new MyHackathonAdapter(options, this);
        recyclerView.setAdapter(hackathonAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hackathonAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hackathonAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hackathonAdapter.notifyDataSetChanged();
    }
}