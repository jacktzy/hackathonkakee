package com.yydds.hackathonkakee.participant;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.News;

import java.text.SimpleDateFormat;

public class HomeNewsAdapter extends FirestoreRecyclerAdapter<News, HomeNewsAdapter.HomeNewsViewHolder> {
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HomeNewsAdapter(@NonNull FirestoreRecyclerOptions<News> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeNewsViewHolder holder, int position, @NonNull News news) {
        holder.titleTV.setText(news.getTitle());
        String publishDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(news.getTimestamp().toDate());
        holder.timestampTV.setText(publishDate);
        Picasso.get().load(news.getPictureUri()).into(holder.pictureIV);
        String newsID = this.getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("newsID", newsID);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public HomeNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_participant_home_news_item, parent, false);
        return new HomeNewsViewHolder(view);
    }

    class HomeNewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, timestampTV;
        ImageView pictureIV;

        public HomeNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            timestampTV = itemView.findViewById(R.id.timestampTV);
            pictureIV = itemView.findViewById(R.id.pictureIV);
        }
    }
}
