package com.yydds.hackathonkakee.participant.hackathonDashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.classes.Team;
import com.yydds.hackathonkakee.general.Utility;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindTeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String hackathonID, participantID;

    TextInputLayout teamCodeTIL;
    TextInputEditText teamCodeTIET;
    MaterialButton joinTeamBtn;
    RecyclerView teamListRV;

    TeamAdapter teamAdapter;

    public FindTeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindTeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindTeamFragment newInstance(String param1, String param2) {
        FindTeamFragment fragment = new FindTeamFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);

        Query query = FirebaseFirestore.getInstance().collection("Teams")
                .whereEqualTo("hackathonID", hackathonID)
                .whereEqualTo("teamVisibility", "Public");
        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>().setQuery(query, Team.class).build();
        teamListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        teamAdapter = new TeamAdapter(options, getContext());
        teamListRV.setAdapter(teamAdapter);
    }

    private void initComponents(View view) {
        teamCodeTIET = view.findViewById(R.id.teamCodeTIET);
        teamCodeTIL = view.findViewById(R.id.teamCodeTIL);
        joinTeamBtn = view.findViewById(R.id.joinTeamBtn);
        teamListRV = view.findViewById(R.id.teamListRV);


        joinTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinTeamUsingCode();
            }
        });
    }

    private void joinTeamUsingCode() {
        String inputTeamCode = teamCodeTIET.getText().toString();
        if (inputTeamCode.isEmpty()) {
            teamCodeTIL.setErrorEnabled(true);
            teamCodeTIL.setError("Please enter team code.");
            return;
        }
        teamCodeTIL.setErrorEnabled(false);
        DocumentReference teamDocumentReference = FirebaseFirestore.getInstance().collection("Teams").document(inputTeamCode);
        teamDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    teamCodeTIET.setText("");
                    Toast.makeText(getActivity(), "Team code " + inputTeamCode + " doesn't exist.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Team team = documentSnapshot.toObject(Team.class);
                if (!team.getHackathonID().equals(hackathonID)) {
                    teamCodeTIET.setText("");
                    Toast.makeText(getActivity(), "Team code " + inputTeamCode + " doesn't exist.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference hackathonDocumentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
                hackathonDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hackathon hackathon = documentSnapshot.toObject(Hackathon.class);
                        if (hackathon.getParticipantsWithTeam().contains(participantID)) {
                            teamCodeTIET.setText("");
                            Toast.makeText(getActivity(), "You already joined a team.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (team.getMembersName().size() >= hackathon.getMaxTeamMembers()) {
                            teamCodeTIET.setText("");
                            Toast.makeText(getActivity(), "Team " + team.getTeamName() + " already full.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<String> currentParticipantsWithTeam = hackathon.getParticipantsWithTeam();
                        currentParticipantsWithTeam.add(participantID);
                        hackathonDocumentReference.update("participantsWithTeam", currentParticipantsWithTeam);

                        DocumentReference participantDocumentReference = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
                        participantDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Participant participant = documentSnapshot.toObject(Participant.class);
                                ArrayList<String> currentJoinedTeamID = participant.getJoinedTeamID();
                                currentJoinedTeamID.add(inputTeamCode);
                                int currentPoint = participant.getPoints();
                                if (team.getRanking() >= 1 && team.getRanking() <= 5) {
                                    currentPoint += Utility.POINT_REFERENCE[team.getRanking() - 1];
                                }
                                participantDocumentReference.update("joinedTeamID", currentJoinedTeamID, "points", currentPoint);

                                ArrayList<String> currentMembersName = team.getMembersName();
                                currentMembersName.add(participant.getName());
                                ArrayList<String> currentMembersID = team.getMembersID();
                                currentMembersID.add(participantID);
                                teamDocumentReference.update("membersID", currentMembersID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        teamDocumentReference.update("membersName", currentMembersName);
                                    }
                                });


                                Toast.makeText(getActivity(), "Join " + team.getTeamName() + " successfully.", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getActivity(), R.id.hackathonDashboardFragmentContainer).navigate(R.id.myTeamFragment);
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Get team failed.");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        teamAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        teamAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        teamAdapter.notifyDataSetChanged();
    }
}