package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.classes.Team;
import com.yydds.hackathonkakee.general.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamDetailActivity extends AppCompatActivity {
    HashMap<String, Object> teamMap;
    String teamID, hackathonID;
    int currentTeamRanking;
    EditText rankingET;
    ImageView backArrowIv;
    ImageButton editRankingBtn;
    MaterialButton saveRankingBtn, deleteTeamBtn;
    TextView pageTitleTv, teamNameTV, teamDescTV, visibilityTV, rankingTV, leaderNameTV, leaderContactTV, currNumTeamMembersTV, membersTV, teamIDTV;
    public static boolean canProceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        teamMap = (HashMap<String, Object>) getIntent().getSerializableExtra("teamDetails");
        teamID = getIntent().getStringExtra("teamID");
        hackathonID = getIntent().getStringExtra("hackathonID");

        initComponents();
    }

    private void initComponents() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        pageTitleTv.setText((String) teamMap.get("teamName"));
        backArrowIv = findViewById(R.id.backArrowIv);
        teamNameTV = findViewById(R.id.teamNameTV);
        teamDescTV = findViewById(R.id.teamDescTV);
        visibilityTV = findViewById(R.id.visibilityTv);
        rankingTV = findViewById(R.id.rankingTV);
        leaderNameTV = findViewById(R.id.leaderNameTV);
        leaderContactTV = findViewById(R.id.leaderContactTV);
        currNumTeamMembersTV = findViewById(R.id.currentNumTeamMembersTV);
        membersTV = findViewById(R.id.membersTV);
        editRankingBtn = findViewById(R.id.editRankingBtn);
        saveRankingBtn = findViewById(R.id.saveRankingBtn);
        rankingET = findViewById(R.id.rankingET);
        deleteTeamBtn = findViewById(R.id.deleteTeamBtn);
        teamIDTV = findViewById(R.id.teamIDTV);

        System.out.println(teamMap.get("teamName"));
        teamIDTV.setText(teamID);
        teamNameTV.setText((String) teamMap.get("teamName"));
        teamDescTV.setText((String) teamMap.get("teamDescription"));
        visibilityTV.setText((String) teamMap.get("teamVisibility"));
        currentTeamRanking =(int)(teamMap.get("ranking"));
        rankingTV.setText( currentTeamRanking > 0 ? "#" + currentTeamRanking : "-");
        leaderNameTV.setText(((ArrayList<String>)teamMap.get("membersName")).get(0));
        leaderContactTV.setText((String) teamMap.get("leaderContact"));
        currNumTeamMembersTV.setText(Integer.toString(((ArrayList<String>)(teamMap.get("membersID"))).size()));
        String membersString = "";
        for (String member : (ArrayList<String>)(teamMap.get("membersName"))) {
            membersString += member + "\n";
        }
        membersTV.setText(membersString);
        rankingTV.setVisibility(View.VISIBLE);
        rankingET.setVisibility(View.INVISIBLE);
        editRankingBtn.setVisibility(View.VISIBLE);
        saveRankingBtn.setVisibility(View.INVISIBLE);
        deleteTeamBtn.setVisibility(View.VISIBLE);

        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editRankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankingTV.setVisibility(View.INVISIBLE);
                rankingET.setVisibility(View.VISIBLE);
                editRankingBtn.setVisibility(View.INVISIBLE);
                saveRankingBtn.setVisibility(View.VISIBLE);
                rankingET.setText(Integer.toString(currentTeamRanking));
            }
        });
        saveRankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankingTV.setVisibility(View.VISIBLE);
                rankingET.setVisibility(View.INVISIBLE);
                editRankingBtn.setVisibility(View.VISIBLE);
                saveRankingBtn.setVisibility(View.INVISIBLE);
                DocumentReference teamDF = FirebaseFirestore.getInstance().collection("Teams").document(teamID);
                int updatedRanking = Integer.parseInt(rankingET.getText().toString());
                teamDF.update("ranking", updatedRanking)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                teamDF.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Team team = documentSnapshot.toObject(Team.class);
                                        ArrayList<String> membersID = team.getMembersID();
                                        final int tempCurrentTeamRanking = currentTeamRanking;
                                        for (int i = 0; i < membersID.size(); i++) {
                                            DocumentReference participantDF = FirebaseFirestore.getInstance().collection("Participants").document(membersID.get(i));
                                            participantDF.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Participant participant = documentSnapshot.toObject(Participant.class);
                                                    int currentPoint = participant.getPoints();
                                                    if (tempCurrentTeamRanking >= 1 && tempCurrentTeamRanking <= 5) {
                                                        currentPoint -= Utility.POINT_REFERENCE[tempCurrentTeamRanking - 1];
                                                    }
                                                    if (updatedRanking >= 1 && updatedRanking <= 5) {
                                                        currentPoint += Utility.POINT_REFERENCE[updatedRanking - 1];
                                                    }
                                                    participantDF.update("points", currentPoint);
                                                }
                                            });
                                        }
                                        currentTeamRanking = updatedRanking;
                                    }
                                });
                            }
                        });
                rankingTV.setText("#" + rankingET.getText().toString());
            }
        });
        deleteTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTeamBtn.setVisibility(View.INVISIBLE);
                deleteTeam();
            }
        });
    }

    private void deleteTeam() {
        ArrayList<String> membersID = (ArrayList<String>) teamMap.get("membersID");
        FirebaseFirestore.getInstance().collection("Teams").document(teamID).delete();
        for (String memberID : membersID) {
            FirebaseFirestore.getInstance().collection("Participants").document(memberID)
                    .update("joinedTeamID", FieldValue.arrayRemove(teamID));
            FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID)
                    .update("participantsWithTeam", FieldValue.arrayRemove(memberID));
        }
        FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID)
                .update("teamsID", FieldValue.arrayRemove(teamID));
        Toast.makeText(this, "Delete team " + teamMap.get("teamName") + " successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }
}