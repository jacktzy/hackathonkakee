package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Announcement;
import com.yydds.hackathonkakee.classes.News;

public class ManageNewsActivity extends AppCompatActivity {
    String hackathonID;
    MaterialButton createNewsBtn;
    RecyclerView newsListRV;
    TextView pageTitleTV;
    ImageView backArrowIV;
    NewsAdapter newsAdapter;
    MaterialCardView noNewsMCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_news);
        hackathonID = getIntent().getStringExtra("hackathonID");

        initComponents();
        setupRecyclerView();
    }

    private void initComponents() {
        createNewsBtn = findViewById(R.id.createNewsBtn);
        newsListRV = findViewById(R.id.newsListRV);
        pageTitleTV = findViewById(R.id.pageTitleTv);
        backArrowIV = findViewById(R.id.backArrowIv);
        noNewsMCV = findViewById(R.id.noNewsMCV);

        pageTitleTV.setText("Manage News");
        createNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageNewsActivity.this, CreateNewsActivity.class);
                intent.putExtra("hackathonID", hackathonID);
                startActivity(intent);
            }
        });
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("News")
                .whereEqualTo("hackathonID", hackathonID)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<News> options = new FirestoreRecyclerOptions.Builder<News>().setQuery(query, News.class).build();
//        AggregateQuery aggregateQuery = query.count();
//        aggregateQuery.get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
//            @Override
//            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
//                long size = aggregateQuerySnapshot.getCount();
//                if (size <= 0) {
//                    noNewsMCV.setVisibility(View.VISIBLE);
//                } else {
//                    noNewsMCV.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
        newsListRV.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(options, this, hackathonID);
        newsListRV.setAdapter(newsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        newsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        newsAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsAdapter.notifyDataSetChanged();
    }
}