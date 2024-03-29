package com.yydds.hackathonkakee.participant;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.organizer.HackathonSettingActivity;
import com.yydds.hackathonkakee.participant.hackathonDashboard.HackathonDashboardActivity;

import java.text.SimpleDateFormat;

public class HackathonItemAdapter extends FirestoreRecyclerAdapter<Hackathon, HackathonItemAdapter.HackathonViewHolder> {
    Context context;
    String participantID;
    boolean isParticipatedHackathons;

    public HackathonItemAdapter(@NonNull FirestoreRecyclerOptions<Hackathon> options, Context context, String participantID, boolean isParticipatedHackathons) {
        super(options);
        this.context = context;
        this.participantID = participantID;
        this.isParticipatedHackathons = isParticipatedHackathons;
    }

    @Override
    protected void onBindViewHolder(@NonNull HackathonViewHolder holder, int position, @NonNull Hackathon hackathon) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        String startDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getStartDateTS().toDate());
        String endDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getEndDateTS().toDate());
        String period = startDate + " - " + endDate;

        Picasso.get().load(hackathon.getIconUri()).into(holder.hackathonIcon);
        holder.hackathonTitle.setText(hackathon.getName());
        holder.date.setText(period);
        holder.mode.setText(hackathon.getMode());
        holder.venue.setText(hackathon.getMode().equals("Online") ? "-" : hackathon.getVenue());
        holder.shortDesc.setText(hackathon.getShortDesc());

        if (isParticipatedHackathons) {
            holder.itemView.setOnClickListener((v) -> {
                Intent intent = new Intent(context, HackathonDashboardActivity.class);
                intent.putExtra("participantID", participantID);
                String hackathonID = this.getSnapshots().getSnapshot(position).getId();
                intent.putExtra("hackathonID", hackathonID);
                intent.putExtra("hackathonName", hackathon.getName());
                context.startActivity(intent);
            });
        } else {
            holder.itemView.setOnClickListener((v) -> {
                Intent intent = new Intent(context, HackathonDetailActivity.class);
                intent.putExtra("participantID", participantID);
                String hackathonID = this.getSnapshots().getSnapshot(position).getId();
                intent.putExtra("hackathonID", hackathonID);
                intent.putExtra("hackathonName", hackathon.getName());
                context.startActivity(intent);
            });
        }

        holder.itemView.startAnimation(animation);
    }

    @NonNull
    @Override
    public HackathonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_hackathon_item, parent, false);
        return new HackathonViewHolder(view);
    }

    class HackathonViewHolder extends RecyclerView.ViewHolder {
        TextView hackathonTitle, date, mode, venue, shortDesc;
        ImageView hackathonIcon;

        public HackathonViewHolder(@NonNull View itemView) {
            super(itemView);
            hackathonTitle = itemView.findViewById(R.id.hackathonTitle);
            date = itemView.findViewById(R.id.date);
            mode = itemView.findViewById(R.id.mode);
            venue = itemView.findViewById(R.id.venue);
            shortDesc = itemView.findViewById(R.id.shortDesc);
            hackathonIcon = itemView.findViewById(R.id.hackathonIcon);
        }
    }
}
