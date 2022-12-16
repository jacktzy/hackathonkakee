package com.yydds.hackathonkakee.participant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

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
    Button editProfileBtn, viewResumeBtn;

    TextView nameTV, emailTV, birthDateTV, genderTV, phoneTV, insNameTV, majorTV, levelEduTV, GPATV, interestFieldTV, interestJobPosTV, noResumeAlertTV;

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
                Navigation.findNavController(view).navigate(R.id.editProfileFragment);
            }
        });
    }
//    TextView nameTV, emailTV, birthDateTV, genderTV, phoneTV, insNameTV, majorTV, levelEduTV, GPATV, interestFieldTV, InterestJobPosTV;
//    EditText nameET, emailET, birthDateET, genderET, phoneET, insNameET, majorET, levelEduET, GPAET, interestFieldET, InterestJobPosET;

    private void initializeComponents(View view) {
        viewResumeBtn = view.findViewById(R.id.name);
        editProfileBtn = view.findViewById(R.id.name);

        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.emailTV);
        birthDateTV = view.findViewById(R.id.birthDateTV);
        genderTV = view.findViewById(R.id.genderTV);
        phoneTV = view.findViewById(R.id.nameTV);
        insNameTV = view.findViewById(R.id.name);
        majorTV = view.findViewById(R.id.name);
        levelEduTV = view.findViewById(R.id.nameTV);
        GPATV = view.findViewById(R.id.name);
        interestFieldTV = view.findViewById(R.id.nameTV);
        interestJobPosTV = view.findViewById(R.id.nameTV);
        noResumeAlertTV = view.findViewById(R.id.name);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ParticipantEditProfileActivity.class);
                intent.putExtra("participantID", participantID);
                startActivity(intent);
            }
        });
    }

    private void assignValue() {
        DocumentReference df = FirebaseFirestore.getInstance().collection("participants").document(participantID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Participant participant = documentSnapshot.toObject(Participant.class);
                nameTV.setText(participant.getName());
                emailTV.setText(participant.getEmail());
                birthDateTV.setText(participant.getBirthDate().toString());
                genderTV.setText(participant.getGender());
                phoneTV.setText(participant.getPhoneNumber());
                insNameTV.setText(participant.getInstitutionName());
                majorTV.setText(participant.getFieldMajor());
                levelEduTV.setText(participant.getLevelOfEducation());
                GPATV.setText(Double.toString(participant.getGPA()));
                interestFieldTV.setText(participant.getInterestField());
                interestJobPosTV.setText(participant.getInterestJobPos());
                resumeUrl = participant.getResumeUrl();
            }
        });

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
                    startActivity(intent);
                }
            });
        }

    }
}