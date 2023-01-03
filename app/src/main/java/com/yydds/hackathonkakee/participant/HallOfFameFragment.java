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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HallOfFameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HallOfFameFragment extends Fragment {
    RecyclerView hallOfFameRV;
    FloatingActionButton redeemBtn;
    HOFPersonAdapter hofPersonAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HallOfFameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HallOfFameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HallOfFameFragment newInstance(String param1, String param2) {
        HallOfFameFragment fragment = new HallOfFameFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hall_of_fame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hallOfFameRV = view.findViewById(R.id.hallOfFameRV);
        redeemBtn = view.findViewById(R.id.redeemBtn);

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.claimRewardFragment);
            }
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("Participants").orderBy("points", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Participant> options = new FirestoreRecyclerOptions.Builder<Participant>().setQuery(query, Participant.class).build();
        hallOfFameRV.setLayoutManager(new LinearLayoutManager(getContext()));
        hofPersonAdapter = new HOFPersonAdapter(options, getContext());
        hallOfFameRV.setAdapter(hofPersonAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        hofPersonAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        hofPersonAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        hofPersonAdapter.notifyDataSetChanged();
    }
}