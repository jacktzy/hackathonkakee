package com.yydds.hackathonkakee;

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
import com.yydds.hackathonkakee.classes.Team;
import com.yydds.hackathonkakee.organizer.TeamDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamAdapter extends FirestoreRecyclerAdapter<Team, TeamAdapter.TeamViewHolder> {
    Context context;
    String hackathonID;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeamAdapter(@NonNull FirestoreRecyclerOptions<Team> options, Context context, String hackathonID) {
        super(options);
        this.context = context;
        this.hackathonID = hackathonID;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamViewHolder holder, int position, @NonNull Team team) {
        String teamID = this.getSnapshots().getSnapshot(position).getId();
        HashMap<String, Object> teamMap = new HashMap<>();
        teamMap.put("hackathonID", team.getHackathonID());
        teamMap.put("leaderContact", team.getLeaderContact());
        teamMap.put("membersID", team.getMembersID());
        teamMap.put("membersName", team.getMembersName());
        teamMap.put("ranking", team.getRanking());
        teamMap.put("teamDescription", team.getTeamDescription());
        teamMap.put("teamName", team.getTeamName());
        teamMap.put("teamVisibility", team.getTeamVisibility());


        holder.noTV.setText(Integer.toString(position + 1));
        holder.teamIDTV.setText(teamID);
        holder.teamNameTV.setText(team.getTeamName());
        holder.teamRankingTV.setText(team.getRanking() > 0 ? "#" + Integer.toString(team.getRanking()) : "-");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TeamDetailActivity.class);
                intent.putExtra("teamID", teamID);
                intent.putExtra("teamDetails", teamMap);
                intent.putExtra("hackathonID", hackathonID);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_organizer_show_teams, parent, false);
        return new TeamViewHolder(view);
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView noTV, teamIDTV, teamNameTV, teamRankingTV;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            noTV = itemView.findViewById(R.id.noTV);
            teamIDTV = itemView.findViewById(R.id.teamIDTV);
            teamNameTV = itemView.findViewById(R.id.teamNameTV);
            teamRankingTV = itemView.findViewById(R.id.teamRankingTV);
        }
    }
}
