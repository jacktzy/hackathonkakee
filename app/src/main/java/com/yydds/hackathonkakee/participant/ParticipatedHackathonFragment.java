package com.yydds.hackathonkakee.participant;

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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipatedHackathonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipatedHackathonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String participantID;
    private HackathonItemAdapter hackathonItemAdapter;
    private FirebaseFirestore db;
    MaterialCardView noHackathonMCV;
    MaterialButton findHackathonBtn;

    public ParticipatedHackathonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParticipatedHackathonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParticipatedHackathonFragment newInstance(String param1, String param2) {
        ParticipatedHackathonFragment fragment = new ParticipatedHackathonFragment();
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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participated_hackathon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView hackathonListRV = view.findViewById(R.id.hackathonListRV);
        noHackathonMCV = view.findViewById(R.id.noHackathonMCV);
        findHackathonBtn = view.findViewById(R.id.findHackathonBtn);

        findHackathonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.participantAllHackathonFragment);
            }
        });

        Query query = db.collection("Hackathons").whereArrayContains("participantsID", participantID);
        FirestoreRecyclerOptions<Hackathon> options = new FirestoreRecyclerOptions.Builder<Hackathon>().setQuery(query, Hackathon.class).build();
        AggregateQuery aggregateQuery = query.count();
        aggregateQuery.get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
            @Override
            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                long size = aggregateQuerySnapshot.getCount();
                if (size <= 0) {
                    noHackathonMCV.setVisibility(View.VISIBLE);
                } else {
                    noHackathonMCV.setVisibility(View.INVISIBLE);
                }
            }
        });
        hackathonListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        hackathonItemAdapter = new HackathonItemAdapter(options, getContext(), participantID, true);
        hackathonListRV.setAdapter(hackathonItemAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        hackathonItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        hackathonItemAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        hackathonItemAdapter.notifyDataSetChanged();
    }
}