package com.yydds.hackathonkakee.participant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.News;
import com.yydds.hackathonkakee.general.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView upcomingHackathonRV, newsRV;
    String participantID;
    HomeHackathonAdapter homeHackathonAdapter;
    HomeNewsAdapter homeNewsAdapter;
    TextView moreHackathonTV, hiTV, noHackathonTV, noNewsTV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
        setupRecyclerView();
    }

    private void initComponents(View view) {
        upcomingHackathonRV = view.findViewById(R.id.upcomingHackathonRV);
        newsRV = view.findViewById(R.id.newsRV);
        hiTV = view.findViewById(R.id.hiTV);
        moreHackathonTV = view.findViewById(R.id.moreHackathonTV);
        noHackathonTV = view.findViewById(R.id.noHackathonTV);
        noNewsTV = view.findViewById(R.id.noNewsTV);

        hiTV.setText("Hi " + getActivity().getIntent().getStringExtra("participantName"));

        moreHackathonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.hackathonFragment);
                Navigation.findNavController(view).navigate(R.id.participantAllHackathonFragment);
            }
        });
    }

    private void setupRecyclerView() {
        Query hackathonQuery = FirebaseFirestore.getInstance().collection("Hackathons").whereGreaterThan("startDateTS", Timestamp.now());
        FirestoreRecyclerOptions<Hackathon> hackathonOptions = new FirestoreRecyclerOptions.Builder<Hackathon>().setQuery(hackathonQuery, Hackathon.class).build();
        AggregateQuery hackathonCountQuery = hackathonQuery.count();
        hackathonCountQuery.get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
            @Override
            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                long size = aggregateQuerySnapshot.getCount();
                if (size <= 0) {
                    noHackathonTV.setVisibility(View.VISIBLE);
                    upcomingHackathonRV.setVisibility(View.GONE);
                } else {
                    noHackathonTV.setVisibility(View.GONE);
                    upcomingHackathonRV.setVisibility(View.VISIBLE);
                }
            }
        });
        upcomingHackathonRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        homeHackathonAdapter = new HomeHackathonAdapter(hackathonOptions, getContext(), participantID);
        upcomingHackathonRV.setAdapter(homeHackathonAdapter);

        Query newsQuery = FirebaseFirestore.getInstance().collection("News").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<News> newsOptions = new FirestoreRecyclerOptions.Builder<News>().setQuery(newsQuery, News.class).build();
        AggregateQuery newsCountQuery = newsQuery.count();
        newsCountQuery.get(AggregateSource.SERVER).addOnSuccessListener(new OnSuccessListener<AggregateQuerySnapshot>() {
            @Override
            public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                long size = aggregateQuerySnapshot.getCount();
                if (size <= 0) {
                    noNewsTV.setVisibility(View.VISIBLE);
                    newsRV.setVisibility(View.GONE);
                } else {
                    noNewsTV.setVisibility(View.GONE);
                    newsRV.setVisibility(View.VISIBLE);
                }
            }
        });
        newsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        homeNewsAdapter = new HomeNewsAdapter(newsOptions, getContext());
        newsRV.setAdapter(homeNewsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        homeHackathonAdapter.startListening();
        homeNewsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        homeHackathonAdapter.stopListening();
        homeNewsAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeHackathonAdapter.notifyDataSetChanged();
        homeNewsAdapter.notifyDataSetChanged();
    }
}