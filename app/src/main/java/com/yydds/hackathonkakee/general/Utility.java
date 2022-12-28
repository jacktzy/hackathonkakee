package com.yydds.hackathonkakee.general;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.classes.Team;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Utility {
    //generate hackathon ID
    public static String generateHackathonID() {
        StringBuilder randAlp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randAlp.append((char)('a' + (int)(Math.random() * 26)));
        }
        randAlp.replace(0, 0, String.valueOf(System.currentTimeMillis()));
        return randAlp.toString();
    }

    //generate team ID
    public static String generateTeamID() {
        StringBuilder randAlp = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            randAlp.append((char)('a' + (int)(Math.random() * 26)));
        }
        return randAlp.toString();
    }

    //delete one member from team
    public static void deleteAMemberFromTeam(String participantID, String teamID, String hackathonID) {
        DocumentReference teamDR = FirebaseFirestore.getInstance().collection("Teams").document(teamID);
        teamDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Team team = documentSnapshot.toObject(Team.class);
                final int CURRENT_TEAM_MEMBERS_NUMBER = team.getMembersID().size();
                DocumentReference participantDR = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
                participantDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Participant participant = documentSnapshot.toObject(Participant.class);
                        ArrayList<String> currentJoinedTeamID = participant.getJoinedTeamID();
                        currentJoinedTeamID.remove(teamID);
                        participantDR.update("joinedTeamID", currentJoinedTeamID);
                        System.out.println(team.getMembersID().size());
                        ArrayList<String> currentMembersID = team.getMembersID();
                        ArrayList<String> currentMembersName = team.getMembersName();
                        currentMembersID.remove(participantID);
                        currentMembersName.remove(participant.getName());
                        teamDR.update("membersName", currentMembersName).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                teamDR.update("membersID", currentMembersID);
                            }
                        });
                        DocumentReference hackathonDR = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
                        hackathonDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Hackathon hackathon = documentSnapshot.toObject(Hackathon.class);
                                ArrayList<String> currentParticipantsWithTeam = hackathon.getParticipantsWithTeam();
                                currentParticipantsWithTeam.remove(participantID);
                                hackathonDR.update("participantsWithTeam", currentParticipantsWithTeam);
                                if (CURRENT_TEAM_MEMBERS_NUMBER == 1) {
                                    ArrayList<String> currentTeamsID = hackathon.getTeamsID();
                                    currentTeamsID.remove(teamID);
                                    hackathonDR.update("teamsID", currentTeamsID);
                                    teamDR.delete();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}