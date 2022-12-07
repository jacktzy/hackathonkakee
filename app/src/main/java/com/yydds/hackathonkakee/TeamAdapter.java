package com.yydds.hackathonkakee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yydds.hackathonkakee.classes.Team;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Team> list;
    private ItemClickListener mItemListener;


    public TeamAdapter(Context context, ArrayList<Team> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.mItemListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_design_organizer_show_teams,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.no.setText("No: " + list.get(position).getNo());
        holder.teamName.setText("Team Name: " + list.get(position).getTeamName());
        String temp = "";
        for(int i = 0; i<list.get(position).getTeamMember().length; i++) {
            temp += (i+1) + ". " + list.get(position).getTeamMember()[i] + "\n";
        }
        holder.teamMember.setText(temp);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.editranking.setOnClickListener(view -> {
            mItemListener.onItemClick(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener {
        void onItemClick(Team details);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no, teamName, teamMember;
        ImageView editranking, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            no = itemView.findViewById(R.id.no);
            teamName = itemView.findViewById(R.id.teamName);
            teamMember = itemView.findViewById(R.id.teamMember);
            editranking = itemView.findViewById(R.id.editRanking);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
