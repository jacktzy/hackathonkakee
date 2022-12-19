package com.yydds.hackathonkakee.participant.hackathonDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Announcement;

import java.text.SimpleDateFormat;

public class AnnouncementAdapter extends FirestoreRecyclerAdapter<Announcement, AnnouncementAdapter.AnnouncementViewHolder> {

    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnnouncementAdapter(@NonNull FirestoreRecyclerOptions<Announcement> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_organizer_announcement, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position, @NonNull Announcement announcement) {
        holder.titleTV.setText(announcement.getTitle());
        holder.contentTV.setText(announcement.getContent());
        holder.timestampTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(announcement.getTimestamp().toDate()));
    }

    class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, contentTV, timestampTV;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV =itemView.findViewById(R.id.titleTV);
            contentTV = itemView.findViewById(R.id.contentTV);
            timestampTV = itemView.findViewById(R.id.timestampTV);
        }
    }
}
