package com.yydds.hackathonkakee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yydds.hackathonkakee.classes.Participant;

import java.util.ArrayList;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Participant> list;

    public ParticipantAdapter(Context context, ArrayList<Participant> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_design_organizer_show_participants,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.no.setText("No: " );
        holder.name.setText("Name: " + list.get(position).getName());
        holder.team.setText("Team: " );

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no, name, team;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            no = itemView.findViewById(R.id.no);
            name = itemView.findViewById(R.id.name);
            team = itemView.findViewById(R.id.team);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}