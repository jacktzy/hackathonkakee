package com.yydds.hackathonkakee.participant.hackathonDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Team;

public class TeamAdapter extends FirestoreRecyclerAdapter<Team, TeamAdapter.TeamViewHolder> {
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeamAdapter(@NonNull FirestoreRecyclerOptions<Team> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamViewHolder holder, int position, @NonNull Team team) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        holder.teamNameTV.setText(team.getTeamName());
        if (team.getMembersName().size() != 0) {
            holder.leaderNameTV.setText(team.getMembersName().get(0));
        }
        holder.leaderContactTV.setText(team.getLeaderContact());

        holder.itemView.startAnimation(animation);
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_find_team_list, parent, false);
        return new TeamViewHolder(view);
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView teamNameTV, leaderNameTV, leaderContactTV;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            teamNameTV = itemView.findViewById(R.id.rankingTV);
            leaderNameTV = itemView.findViewById(R.id.leaderNameTV);
            leaderContactTV = itemView.findViewById(R.id.teamNameTV);
        }
    }
}
