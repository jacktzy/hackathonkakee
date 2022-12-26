package com.yydds.hackathonkakee.participant.hackathonDashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.general.Utility;

import java.util.ArrayList;

public class MyTeamMemberItem extends RecyclerView.Adapter<MyTeamMemberItem.ViewHolder> {
    private ArrayList<String> membersName, membersID;
    private String hackathonID, teamID, participantID;
    private Context context;

    public MyTeamMemberItem(Context context, ArrayList<String> membersName, ArrayList<String> membersID, String hackathonID, String teamID, String participantID) {
        this.membersName = membersName;
        this.membersID = membersID;
        this.hackathonID = hackathonID;
        this.teamID = teamID;
        this.context = context;
        this.participantID = participantID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_design_my_team_member_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        System.out.println(position);
        if (participantID.equals(membersID.get(position))) {
            holder.deleteMemberBtn.setVisibility(View.INVISIBLE);
        }
        holder.memberNameTV.setText(membersName.get(position));
        holder.deleteMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO delete hackathon
                holder.memberNameTV.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                Utility.deleteAMemberFromTeam(membersID.get(position), teamID, hackathonID);
                Toast.makeText(view.getContext(), "Delete " + membersName.get(position) + " successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return membersName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteMemberBtn;
        public TextView memberNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteMemberBtn = itemView.findViewById(R.id.deleteMemberBtn);
            memberNameTV = itemView.findViewById(R.id.memberNameTV);
        }
    }
}
