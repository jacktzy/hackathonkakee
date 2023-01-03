package com.yydds.hackathonkakee.participant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.general.LoginActivity;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    //testing purpose
    MaterialButton viewResumeBtn, logoutBtn;
    FloatingActionButton editProfileBtn;

    TextView nameTV, emailTV, birthDateTV, genderTV, phoneTV, insNameTV, majorTV, levelEduTV, CGPATV, interestFieldTV, interestJobPosTV, noResumeAlertTV;
    ImageView profilePicIV;

    String participantID, resumeUrl;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        //get intent extra
        participantID = getActivity().getIntent().getStringExtra("participantID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
        assignValue();




//        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ParticipantEditProfileActivity.class);
                intent.putExtra("participantID", participantID);
                startActivity(intent);
            }
        });
    }

    private void initializeComponents(View view) {
        viewResumeBtn = view.findViewById(R.id.viewResumeBtn);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.emailTV);
        birthDateTV = view.findViewById(R.id.birthDateTV);
        genderTV = view.findViewById(R.id.genderTV);
        phoneTV = view.findViewById(R.id.phoneNumberTV);
        insNameTV = view.findViewById(R.id.institutionNameTV);
        majorTV = view.findViewById(R.id.fieldMajorTV);
        levelEduTV = view.findViewById(R.id.levelOfEducationTV);
        CGPATV = view.findViewById(R.id.CGPATV);
        interestFieldTV = view.findViewById(R.id.interestFieldTV);
        interestJobPosTV = view.findViewById(R.id.jobPositionET);
        noResumeAlertTV = view.findViewById(R.id.noResumeAlertTV);
        profilePicIV = view.findViewById(R.id.profilePictureIV);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ParticipantEditProfileActivity.class);
                intent.putExtra("participantID", participantID);
                startActivity(intent);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void assignValue() {
        DocumentReference df = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Participant participant = documentSnapshot.toObject(Participant.class);
                if (!participant.getProfilePicUrl().isEmpty()) Picasso.get().load(participant.getProfilePicUrl()).into(profilePicIV);

                nameTV.setText(participant.getName());
                emailTV.setText(participant.getEmail());
                birthDateTV.setText(new SimpleDateFormat("dd/MM/yyyy").format(participant.getBirthDate().toDate()));
                genderTV.setText(participant.getGender());
                phoneTV.setText(participant.getPhoneNumber());
                insNameTV.setText(participant.getInstitutionName());
                majorTV.setText(participant.getFieldMajor());
                levelEduTV.setText(participant.getLevelOfEducation());
                CGPATV.setText(Double.toString(participant.getCGPA()));
                interestFieldTV.setText(participant.getInterestField());
                interestJobPosTV.setText(participant.getInterestJobPos());
                resumeUrl = participant.getResumeUrl();

                if (resumeUrl.isEmpty()) {
                    noResumeAlertTV.setVisibility(View.VISIBLE);
                    viewResumeBtn.setVisibility(View.INVISIBLE);
                } else {
                    noResumeAlertTV.setVisibility(View.INVISIBLE);
                    viewResumeBtn.setVisibility(View.VISIBLE);
                    viewResumeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setType("application/pdf");
                            intent.setData(Uri.parse(resumeUrl));
                            getActivity().startActivity(intent);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to load profile information", Toast.LENGTH_SHORT).show();;
                return;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        assignValue();
    }
}