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

import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.TeamAdapter;
import com.yydds.hackathonkakee.classes.Team;

import java.util.ArrayList;

public class ShowTeams extends AppCompatActivity {
    RecyclerView recyclerView;
    TeamAdapter adapter;
    ArrayList<Team> list;
    ImageView editRanking;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    TextView save, back;
    ImageView backArrowIv;
    TextView pageTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_show_teams);

        initializeComponent();
        setupRecycleView();

    }

    private void setupRecycleView() {
        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<>();

        list.add(new Team(1, "Wong", new String[]{"Wong" , "Jacky"}));
        list.add(new Team(2, "Tan", new String[]{"Wong" , "Jacky"}));
        list.add(new Team(3, "Jacky", new String[]{"Wong" , "Jacky"}));
        list.add(new Team(4, "Yang", new String[]{"Wong" , "Jacky"}));

        adapter = new TeamAdapter(this, list, new TeamAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Team details) {
                dialogBuilder = new AlertDialog.Builder(ShowTeams.this);
                final View contactPopView = getLayoutInflater().inflate(R.layout.pop_up_window_ranking, null);
                dialogBuilder.setView(contactPopView);
                dialog = dialogBuilder.create();
                dialog.show();

                save = contactPopView.findViewById(R.id.save);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ShowTeams.this, "Successfully", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                back = contactPopView.findViewById(R.id.back);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText("Show Teams");
        backArrowIv = findViewById(R.id.backArrowIv);
        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}