package com.yydds.hackathonkakee.participant.hackathonDashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.classes.Team;
import com.yydds.hackathonkakee.general.Utility;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String hackathonID, participantID, hackathonName, teamID, participantName;
    int teamMembersMaxNum;
    ArrayList<String> participantJoinedTeamID, hackathonTeamsID;
    TextView teamNameTV, teamDescTV, visibilityTV, rankingTV, leaderNameTV, leaderContactTV, maxNumTeamMembersTV, currNumTeamMembersTV, membersTV;
    ConstraintLayout teamDetailCL, teamNotFoundCL;
    MaterialButton createTeamBtn, findTeamBtn;
    FloatingActionButton editTeamBtn, quitTeamBtn;
    RecyclerView membersRV;

    public MyTeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTeamFragment newInstance(String param1, String param2) {
        MyTeamFragment fragment = new MyTeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        hackathonID = getActivity().getIntent().getStringExtra("hackathonID");
        participantID = getActivity().getIntent().getStringExtra("participantID");
        hackathonName = getActivity().getIntent().getStringExtra("hackathonName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
    }

//    TextView teamNameTV, teamDescTV, visibilityTV, rankingTV, leaderNameTV, leaderContactTV, maxNumTeamMembersTV, currNumTeamMembersTV, membersTV;
    private void initComponents(View view) {
        teamNameTV = view.findViewById(R.id.teamNameTV);
        teamDescTV = view.findViewById(R.id.teamDescTV);
        visibilityTV = view.findViewById(R.id.visibilityTv);
        rankingTV = view.findViewById(R.id.rankingTV);
        leaderNameTV = view.findViewById(R.id.leaderNameTV);
        leaderContactTV = view.findViewById(R.id.leaderContactTV);
        maxNumTeamMembersTV = view.findViewById(R.id.maxTeamMembersTV);
        currNumTeamMembersTV = view.findViewById(R.id.currentNumTeamMembersTV);
        membersTV = view.findViewById(R.id.membersTV);
        teamDetailCL = view.findViewById(R.id.teamDetailCL);
        teamNotFoundCL = view.findViewById(R.id.teamNotFoundCL);
        createTeamBtn = view.findViewById(R.id.createTeamBtn);
        findTeamBtn = view.findViewById(R.id.findTeamBtn);
        editTeamBtn = view.findViewById(R.id.editTeamBtn);
        quitTeamBtn = view.findViewById(R.id.quitTeamBtn);
        membersRV = view.findViewById(R.id.membersRV);

        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateTeamActivity.class);
                intent.putExtra("participantID", participantID);
                intent.putExtra("hackathonID", hackathonID);
                intent.putExtra("hackathonName", hackathonName);
                startActivity(intent);
                Navigation.findNavController(view).navigate(R.id.teamFragment);
            }
        });
        findTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO find team Btn
            }
        });
        editTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateTeamActivity.class);
                intent.putExtra("participantID", participantID);
                intent.putExtra("hackathonID", hackathonID);
                intent.putExtra("hackathonName", hackathonName);
                intent.putExtra("teamID", teamID);
                startActivity(intent);
            }
        });
        quitTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.deleteAMemberFromTeam(participantID, teamID, hackathonID);
                Navigation.findNavController(getActivity(), R.id.hackathonDashboardFragmentContainer).navigateUp();
            }
        });

        DocumentReference participantDocumentReference = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
        participantDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Participant participant = documentSnapshot.toObject(Participant.class);
                System.out.println(participant.getName());
                participantName = participant.getName();
                participantJoinedTeamID = participant.getJoinedTeamID();
                getHackathonTeamsID();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("failed");
            }
        });
    };

    private boolean hasTeam() {
        for (String participantJoinedTeam : participantJoinedTeamID) {
            if (hackathonTeamsID.contains(participantJoinedTeam)) {
                teamID = participantJoinedTeam;
                return true;
            }
        }
        return false;
    }

    private void getHackathonTeamsID() {
        DocumentReference hackathonDocumentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
        hackathonDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Hackathon hackathon = documentSnapshot.toObject(Hackathon.class);
                hackathonTeamsID = hackathon.getTeamsID();
                teamMembersMaxNum = hackathon.getMaxTeamMembers();
                if (!hasTeam()) {
                    teamDetailCL.setVisibility(View.GONE);
                    teamNotFoundCL.setVisibility(View.VISIBLE);
                    editTeamBtn.setVisibility(View.GONE);
                    quitTeamBtn.setVisibility(View.GONE);
                    return;
                }
                teamDetailCL.setVisibility(View.VISIBLE);
                teamNotFoundCL.setVisibility(View.GONE);

                assignValueToField();
            }
        });
    }

    private void assignValueToField() {
        DocumentReference teamDocumentReference = FirebaseFirestore.getInstance().collection("Teams").document(teamID);
        teamDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Team team = documentSnapshot.toObject(Team.class);
                teamNameTV.setText(team.getTeamName());
                teamDescTV.setText(team.getTeamDescription());
                visibilityTV.setText(team.getTeamVisibility());
                rankingTV.setText(team.getRanking() == 0 ? "-" : Integer.toString(team.getRanking()));
                leaderNameTV.setText(team.getMembersName().get(0));
                leaderContactTV.setText(team.getLeaderContact());
                maxNumTeamMembersTV.setText(Integer.toString(teamMembersMaxNum));
                currNumTeamMembersTV.setText(Integer.toString(team.getMembersName().size()));
                if (participantID.equals(team.getMembersID().get(0))) {
                    editTeamBtn.setVisibility(View.VISIBLE);
                    membersTV.setVisibility(View.INVISIBLE);
                    membersRV.setVisibility(View.VISIBLE);
                    MyTeamMemberItem adapter = new MyTeamMemberItem(getContext(), team.getMembersName(), team.getMembersID(), hackathonID, teamID, participantID);
                    membersRV.setHasFixedSize(true);
                    membersRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    membersRV.setAdapter(adapter);
                } else {
                    editTeamBtn.setVisibility(View.GONE);
                    membersTV.setVisibility(View.VISIBLE);
                    membersRV.setVisibility(View.INVISIBLE);
                    String membersString = "";
                    for (String member : team.getMembersName()) {
                        membersString += member + "\n";
                    }
                    membersTV.setText(membersString);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (teamID != null && !teamID.isEmpty()){
            assignValueToField();
        }
    }

    public void refreshValue() {
        assignValueToField();
    }
}