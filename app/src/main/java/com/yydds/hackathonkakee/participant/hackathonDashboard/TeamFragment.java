package com.yydds.hackathonkakee.participant.hackathonDashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String hackathonID, participantID, hackathonName;

    private MaterialButton myTeamBtn, createTeamBtn, findTeamBtn;


    public TeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment teamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
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
        participantID = getActivity().getIntent().getStringExtra("participantID");
        hackathonID = getActivity().getIntent().getStringExtra("hackathonID");
        hackathonName = getActivity().getIntent().getStringExtra("hackathonName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
    }

    private void initComponents(View view) {
        myTeamBtn = view.findViewById(R.id.myTeamBtn);
        findTeamBtn = view.findViewById(R.id.findTeamBtn);
        createTeamBtn = view.findViewById(R.id.createTeamBtn);

        getActivity().findViewById(R.id.backArrowIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                try {
                    Navigation.findNavController(getActivity(), R.id.hackathonDashboardFragmentContainer).navigateUp();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        myTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.myTeamFragment);
            }
        });
        findTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO navigate to find team
            }
        });
        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference hackathonDocumentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
                hackathonDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hackathon hackathon = documentSnapshot.toObject(Hackathon.class);
                        ArrayList<String> participantsWithTeam = hackathon.getParticipantsWithTeam();
                        if (participantsWithTeam.contains(participantID)) {
                            Toast.makeText(getContext(), "You have already joined a team.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getContext(), CreateTeamActivity.class);
                            intent.putExtra("hackathonID", hackathonID);
                            intent.putExtra("participantID", participantID);
                            intent.putExtra("hackathonName", hackathonName);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

}