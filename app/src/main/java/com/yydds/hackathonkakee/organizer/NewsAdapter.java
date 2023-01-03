package com.yydds.hackathonkakee.organizer;

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

public class NewsAdapter extends FirestoreRecyclerAdapter<News, NewsAdapter.NewsViewHolder> {
    Context context;
    String hackathonID;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NewsAdapter(@NonNull FirestoreRecyclerOptions<News> options, Context context, String hackathonID) {
        super(options);
        this.context = context;
        this.hackathonID = hackathonID;
    }

    @Override
    protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull News news) {
        holder.titleTV.setText(news.getTitle());
        holder.contentTV.setText(news.getContent());
        holder.timestampTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(news.getTimestamp().toDate()));
        Picasso.get().load(news.getPictureUri()).into(holder.pictureIV);
        String newsID = this.getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateNewsActivity.class);
                intent.putExtra("newsID", newsID);
                intent.putExtra("hackathonID", hackathonID);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_organizer_manage_news_item, parent, false);
        return new NewsViewHolder(view);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, contentTV, timestampTV;
        ImageView pictureIV;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            contentTV = itemView.findViewById(R.id.contentTV);
            timestampTV = itemView.findViewById(R.id.timestampTV);
            pictureIV = itemView.findViewById(R.id.pictureIV);
        }
    }
}
