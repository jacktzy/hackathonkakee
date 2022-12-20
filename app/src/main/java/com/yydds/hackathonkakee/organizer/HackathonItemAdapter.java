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
import com.yydds.hackathonkakee.classes.Hackathon;

import java.text.SimpleDateFormat;

public class HackathonItemAdapter extends FirestoreRecyclerAdapter<Hackathon, HackathonItemAdapter.HackathonViewHolder> {

    Context context;

    public HackathonItemAdapter(@NonNull FirestoreRecyclerOptions<Hackathon> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull HackathonViewHolder holder, int position, @NonNull Hackathon hackathon) {
        String startDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getStartDateTS().toDate());
        String endDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getEndDateTS().toDate());
        String period = startDate + " - " + endDate;

        Picasso.get().load(hackathon.getIconUri()).into(holder.hackathonIcon);
        holder.hackathonTitle.setText(hackathon.getName());
        holder.date.setText(period);
        holder.mode.setText(hackathon.getMode());
        holder.venue.setText(hackathon.getVenue());
        holder.shortDesc.setText(hackathon.getShortDesc());

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, HackathonSettingActivity.class);
            intent.putExtra("organizerID", hackathon.getOrganizerID());
//            intent.putExtra("hackathonID", holder.getItemId());
            String hackathonID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("hackathonID", hackathonID);
            intent.putExtra("hackathonName", hackathon.getName());
            context.startActivity(intent);
        });
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
