package com.yydds.hackathonkakee.organizer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.TeamAdapter;
import com.yydds.hackathonkakee.classes.Team;

import java.util.ArrayList;

public class ShowTeams extends AppCompatActivity {
    RecyclerView teamsRV;
    TeamAdapter adapter;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    ImageView backArrowIv;
    TextView pageTitleTv;
    String hackathonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_show_teams);
        hackathonID = getIntent().getStringExtra("hackathonID");

        initializeComponent();
        setupRecycleView();

    }

    private void setupRecycleView() {
        Query query = FirebaseFirestore.getInstance().collection("Teams")
                .whereEqualTo("hackathonID", hackathonID);
        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>().setQuery(query, Team.class).build();
        teamsRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamAdapter(options, this, hackathonID);
        teamsRV.setAdapter(adapter);
    }

//
//    private void setupRecycleView() {
//        recyclerView = findViewById(R.id.recyclerview);
//        list = new ArrayList<>();
//
//        adapter = new TeamAdapter(this, list, new TeamAdapter.ItemClickListener() {
//            @Override
//            public void onItemClick(Team details) {
//                dialogBuilder = new AlertDialog.Builder(ShowTeams.this);
//                final View contactPopView = getLayoutInflater().inflate(R.layout.pop_up_window_ranking, null);
//                dialogBuilder.setView(contactPopView);
//                dialog = dialogBuilder.create();
//                dialog.show();
//
//                save = contactPopView.findViewById(R.id.save);
//
//                save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(ShowTeams.this, "Successfully", Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//
//                back = contactPopView.findViewById(R.id.back);
//
//                back.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
//
//        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
//        recyclerView.setLayoutManager(llm);
//        recyclerView.setAdapter(adapter);
//    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText("Show Teams");
        backArrowIv = findViewById(R.id.backArrowIv);
        teamsRV = findViewById(R.id.teamsRV);


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