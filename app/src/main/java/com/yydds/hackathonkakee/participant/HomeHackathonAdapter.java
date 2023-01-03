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
import com.yydds.hackathonkakee.classes.Hackathon;

import java.text.SimpleDateFormat;

public class HomeHackathonAdapter extends FirestoreRecyclerAdapter<Hackathon, HomeHackathonAdapter.HomeHackathonViewHolder> {
    Context context;
    String participantID;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HomeHackathonAdapter(@NonNull FirestoreRecyclerOptions<Hackathon> options, Context context, String participantID) {
        super(options);
        this.context = context;
        this.participantID = participantID;
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeHackathonViewHolder holder, int position, @NonNull Hackathon hackathon) {
        holder.hackathonNameTV.setText(hackathon.getName());
        String startDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getStartDateTS().toDate());
        holder.dateTV.setText(startDate);
        Picasso.get().load(hackathon.getIconUri()).into(holder.hackathonLogoIV);
        String hackathonID = this.getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HackathonDetailActivity.class);
                intent.putExtra("participantID", participantID);
                intent.putExtra("hackathonID", hackathonID);
                intent.putExtra("hackathonName", hackathon.getName());
                System.out.println(participantID + hackathon.getName() + hackathonID);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public HomeHackathonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_participant_home_hackathon_item, parent, false);
        return new HomeHackathonViewHolder(view);
    }

    class HomeHackathonViewHolder extends RecyclerView.ViewHolder {
        TextView hackathonNameTV, dateTV;
        ImageView hackathonLogoIV;

        public HomeHackathonViewHolder(@NonNull View itemView) {
            super(itemView);
            hackathonNameTV = itemView.findViewById(R.id.hackathonNameTV);
            hackathonLogoIV = itemView.findViewById(R.id.hackathonLogoIV);
            dateTV = itemView.findViewById(R.id.dateTV);
        }
    }
}
