package com.yydds.hackathonkakee.participant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.News;

import java.text.SimpleDateFormat;

public class NewsDetailActivity extends AppCompatActivity {

    String newsID;
    TextView pageTitleTV, titleTV, dateTimeTV, contentTV;
    ImageView backArrowIV, pictureIV;
    News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        newsID = getIntent().getStringExtra("newsID");

        FirebaseFirestore.getInstance().collection("News").document(newsID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                news = documentSnapshot.toObject(News.class);
                initComponents();
            }
        });
    }

    private void initComponents() {
        pageTitleTV = findViewById(R.id.pageTitleTv);
        backArrowIV = findViewById(R.id.backArrowIv);
        titleTV = findViewById(R.id.titleTV);
        dateTimeTV = findViewById(R.id.dateTimeTV);
        contentTV = findViewById(R.id.contentTV);
        pictureIV = findViewById(R.id.pictureIV);

        pageTitleTV.setText("News");
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Picasso.get().load(news.getPictureUri()).into(pictureIV);
        titleTV.setText(news.getTitle());
        String publishDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(news.getTimestamp().toDate());
        dateTimeTV.setText(publishDate);
        contentTV.setText(news.getContent());
    }

}