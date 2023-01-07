package com.yydds.hackathonkakee.organizer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.general.LoginActivity;
import com.yydds.hackathonkakee.general.Utility;

import java.util.ArrayList;

public class ParticipantsAdapter extends FirestoreRecyclerAdapter<Participant, ParticipantsAdapter.ParticipantViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    ArrayList<String> hackathonTeamsID;
    String hackathonID, participantTeamID;
    boolean hasTeam;

    public ParticipantsAdapter(@NonNull FirestoreRecyclerOptions<Participant> options, Context context, String hackathonID) {
        super(options);
        this.context = context;
        this.hackathonTeamsID = hackathonTeamsID;
        this.hackathonID = hackathonID;
    }

    @Override
    protected void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position, @NonNull Participant participant) {
        hasTeam = false;
        participantTeamID = "";
        String participantID = this.getSnapshots().getSnapshot(position).getId();
        if (!participant.getProfilePicUrl().isEmpty()) Picasso.get().load(participant.getProfilePicUrl()).into(holder.profilePicIV);
        else holder.profilePicIV.setImageResource(R.drawable.ic_baseline_person_24);
        holder.noTV.setText("No: " + (position + 1));
        holder.nameTV.setText("Name: " + participant.getName());
        holder.teamTV.setText("Team ID: -");

        Query query = FirebaseFirestore.getInstance().collection("Teams");
        query = query.whereEqualTo("hackathonID", hackathonID);
        query = query.whereArrayContains("membersID", this.getSnapshots().getSnapshot(position).getId());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() == 1) hasTeam = true;
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            participantTeamID = documentSnapshot.getId();
                        }
                        holder.teamTV.setText((hasTeam) ? "Team ID: " + participantTeamID : "Team ID: None");
                        holder.deleteIV.setOnClickListener((v) -> {
                            Dialog dialog;
                            MaterialButton yesBtn, noBtn;
                            dialog = new Dialog(context);
                            dialog.setContentView(R.layout.confirmation_pop_up);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_background));
                            }
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false); //Optional
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                            TextView titleTV = dialog.findViewById(R.id.titleTV), contentTV = dialog.findViewById(R.id.contentTV);
                            titleTV.setText("Delete Participant");
                            contentTV.setText("Are you sure to delete participant \""+ participant.getName() +"\"?");
                            yesBtn = dialog.findViewById(R.id.yesBtn);
                            noBtn = dialog.findViewById(R.id.noBtn);

                            dialog.show();

                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (hasTeam) {
                                        String finalParticipantTeamID = participantTeamID;
                                        FirebaseFirestore.getInstance().collection("Participants").document(participantID).update("participatedHackathonId", FieldValue.arrayRemove(hackathonID));
                                        FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID).update("participantsID", FieldValue.arrayRemove(participantID));
                                        Utility.deleteAMemberFromTeam(participantID, finalParticipantTeamID, hackathonID);
                                    } else {
                                        FirebaseFirestore.getInstance().collection("Participants").document(participantID).update("participatedHackathonId", FieldValue.arrayRemove(hackathonID));
                                        FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID).update("participantsID", FieldValue.arrayRemove(participantID));
                                    }
                                    dialog.dismiss();
                                }
                            });
                            noBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        });
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ParticipantDetailActivity.class);
                intent.putExtra("participantID", participantID);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design_organizer_show_participants, parent, false);
        return new ParticipantViewHolder(view);
    }

    class ParticipantViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicIV, deleteIV;
        TextView noTV, nameTV, teamTV;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicIV = itemView.findViewById(R.id.profilePictureIV);
            deleteIV = itemView.findViewById(R.id.deleteIV);
            noTV = itemView.findViewById(R.id.noTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            teamTV = itemView.findViewById(R.id.teamTV);
        }
    }
}
