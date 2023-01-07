package com.yydds.hackathonkakee.participant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.general.JavaMailAPI;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClaimRewardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClaimRewardFragment extends Fragment {
    String participantID;
    TextView pointTV;
    MaterialButton claim1Btn, claim2Btn, claim3Btn, claim4Btn, claim5Btn;
    DocumentReference participantDR;
    Participant participant;
    ArrayList<String> rewardClaimed;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClaimRewardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClaimRewardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClaimRewardFragment newInstance(String param1, String param2) {
        ClaimRewardFragment fragment = new ClaimRewardFragment();
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
        participantDR = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_claim_reward, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pointTV = view.findViewById(R.id.pointTV);
        claim1Btn = view.findViewById(R.id.claim1Btn);
        claim2Btn = view.findViewById(R.id.claim2Btn);
        claim3Btn = view.findViewById(R.id.claim3Btn);
        claim4Btn = view.findViewById(R.id.claim4Btn);
        claim5Btn = view.findViewById(R.id.claim5Btn);

        participantDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                participant = documentSnapshot.toObject(Participant.class);
                pointTV.setText(Integer.toString(participant.getPoints()));
                rewardClaimed = participant.getRewardClaimed();
                String participantEmail = participant.getEmail();

                for (int i = 0; i < 5; i++) {
                    if (Integer.parseInt(rewardClaimed.get(i)) == 1) {
                        if (i == 0) {
                            claim1Btn.setEnabled(false);
                            claim1Btn.setText("Claimed");
                        } else if (i == 1) {
                            claim2Btn.setEnabled(false);
                            claim2Btn.setText("Claimed");
                        } else if (i == 2) {
                            claim3Btn.setEnabled(false);
                            claim3Btn.setText("Claimed");
                        } else if (i == 3) {
                            claim4Btn.setEnabled(false);
                            claim4Btn.setText("Claimed");
                        } else if (i == 4) {
                            claim5Btn.setEnabled(false);
                            claim5Btn.setText("Claimed");
                        }
                    }
                }

                claim1Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentPoint = participant.getPoints();
                        if (currentPoint >= 500) {
                            claim1Btn.setEnabled(false);
                            claim1Btn.setText("Claimed");
                            rewardClaimed.set(0, "1");
                            participantDR.update("rewardClaimed", rewardClaimed);
                            sendClaimRewardEmail("Hackathon Kakee Keychain", participantEmail);
                            Toast.makeText(getContext(), "You have claimed Hackathon Kakee Keychain!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                claim2Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentPoint = participant.getPoints();
                        if (currentPoint >= 800) {
                            claim2Btn.setEnabled(false);
                            claim2Btn.setText("Claimed");
                            rewardClaimed.set(1, "1");
                            participantDR.update("rewardClaimed", rewardClaimed);
                            sendClaimRewardEmail("Notebook with Big O", participantEmail);
                            Toast.makeText(getContext(), "You have claimed Notebook with Big O!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                claim3Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentPoint = participant.getPoints();
                        if (currentPoint >= 1400) {
                            claim3Btn.setEnabled(false);
                            claim3Btn.setText("Claimed");
                            rewardClaimed.set(2, "1");
                            participantDR.update("rewardClaimed", rewardClaimed);
                            sendClaimRewardEmail("Thermos", participantEmail);
                            Toast.makeText(getContext(), "You have claimed Thermos!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                claim4Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentPoint = participant.getPoints();
                        if (currentPoint >= 2000) {
                            claim4Btn.setEnabled(false);
                            claim4Btn.setText("Claimed");
                            rewardClaimed.set(3, "1");
                            participantDR.update("rewardClaimed", rewardClaimed);
                            sendClaimRewardEmail("Hackathon Kakee Tee", participantEmail);
                            Toast.makeText(getContext(), "You have claimed Hackathon Kakee Tee!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                claim5Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentPoint = participant.getPoints();
                        if (currentPoint >= 3000) {
                            claim5Btn.setEnabled(false);
                            claim5Btn.setText("Claimed");
                            rewardClaimed.set(4, "1");
                            participantDR.update("rewardClaimed", rewardClaimed);
                            sendClaimRewardEmail("Laptop Bag", participantEmail);
                            Toast.makeText(getContext(), "You have claimed Laptop Bag!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void sendClaimRewardEmail(String rewardName, String email) {
        String subject = "Hall of Fame Reward Claimed";
        String message =
                "Congratulations! You successfully claimed " + rewardName + " which only limited in Hackathon Kakee!\n\n" +
                        "For merchandise delivering, please provide us your name, phone number and delivering address by replying this email.\n\n\n" +
                        "Regards,\n" +
                        "TAN ZI YANG\n" +
                        "Director of Hackathon Kakee Company";

        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), email, subject, message);

        javaMailAPI.execute();
    }

}