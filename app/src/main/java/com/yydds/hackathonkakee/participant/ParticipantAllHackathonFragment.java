package com.yydds.hackathonkakee.participant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantAllHackathonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantAllHackathonFragment extends Fragment {

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

    ImageButton searchIBtn, sortIBtn, refreshIBtn;
    TextInputLayout keywordTIL;
    TextInputEditText keywordTIET;
    RecyclerView hackathonListRV;

    Query query;

    public ParticipantAllHackathonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParticipantAllHackathonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParticipantAllHackathonFragment newInstance(String param1, String param2) {
        ParticipantAllHackathonFragment fragment = new ParticipantAllHackathonFragment();
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
        return inflater.inflate(R.layout.fragment_participant_all_hackathon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents(view);
    }

    private void initComponents(View view) {
        hackathonListRV = view.findViewById(R.id.hackathonListRV);
        searchIBtn = view.findViewById(R.id.searchIBtn);
        sortIBtn = view.findViewById(R.id.sortIBtn);
        keywordTIL = view.findViewById(R.id.keywordTIL);
        keywordTIET = view.findViewById(R.id.keywordTIET);
        refreshIBtn = view.findViewById(R.id.refreshIBtn);

        query = db.collection("Hackathons").orderBy("startDateTS", Query.Direction.DESCENDING);
        setupRecyclerView(query);

        searchIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = keywordTIET.getText().toString();
                List<String> keywordList = new ArrayList<>();
                for (char c : keyword.toCharArray()) {
                    keywordList.add(Character.toString(c));
                }
                Query newQuery = db.collection("Hackathons").whereEqualTo("name", keyword);
//                newQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        System.out.println(queryDocumentSnapshots.size());
//                    }
//                });
                setupRecyclerView(newQuery);
                Toast.makeText(getActivity(), "Done searching.", Toast.LENGTH_SHORT).show();
            }
        });
        sortIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), sortIBtn);
                popupMenu.getMenu().add("Sort by name (ascending)");
                popupMenu.getMenu().add("Sort by name (descending)");
                popupMenu.getMenu().add("Sort by date (ascending)");
                popupMenu.getMenu().add("Sort by date (descending)");
                popupMenu.getMenu().add("Mode: Online");
                popupMenu.getMenu().add("Mode: Hybrid");
                popupMenu.getMenu().add("Mode: Physical");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle() == "Sort by name (ascending)") {
                            Query newQuery = db.collection("Hackathons").orderBy("name", Query.Direction.ASCENDING);
                            setupRecyclerView(newQuery);
                            return true;
                        } else if (menuItem.getTitle() == "Sort by name (descending)") {
                            Query newQuery = db.collection("Hackathons").orderBy("name", Query.Direction.DESCENDING);
                            setupRecyclerView(newQuery);
                            return true;
                        } else if (menuItem.getTitle() == "Sort by date (ascending)") {
                            Query newQuery = db.collection("Hackathons").orderBy("startDateTS", Query.Direction.ASCENDING);
                            setupRecyclerView(newQuery);
                            return true;
                        } else if (menuItem.getTitle() == "Sort by date (descending)") {
                            Query newQuery = db.collection("Hackathons").orderBy("startDateTS", Query.Direction.DESCENDING);
                            setupRecyclerView(newQuery);
                            return true;
                        } else if (menuItem.getTitle() == "Mode: Online") {
                            Query newQuery = db.collection("Hackathons").whereEqualTo("mode", "Online");
                            setupRecyclerView(newQuery);
                            return true;
                        } else if (menuItem.getTitle() == "Mode: Hybrid") {
                            Query newQuery = db.collection("Hackathons").whereEqualTo("mode", "Hybrid");
                            setupRecyclerView(newQuery);
                            return true;
                        } else if (menuItem.getTitle() == "Mode: Physical") {
                            Query newQuery = db.collection("Hackathons").whereEqualTo("mode", "Physical");
                            setupRecyclerView(newQuery);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        refreshIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = db.collection("Hackathons");
                setupRecyclerView(query);
            }
        });
    }

    private void test (String hName, String ID) {
        List<String> keywordList = new ArrayList<>();
        for (char c : hName.toCharArray()) {
            keywordList.add(Character.toString(Character.toLowerCase(c)));
        }
        FirebaseFirestore.getInstance().collection("Hackathons").document(ID).update("nameWords", keywordList);
    }

    private void setupRecyclerView(Query query) {
        FirestoreRecyclerOptions<Hackathon> options = new FirestoreRecyclerOptions.Builder<Hackathon>().setQuery(query, Hackathon.class).build();
        hackathonListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        hackathonItemAdapter = new HackathonItemAdapter(options, getContext(), participantID, false);
        hackathonListRV.setAdapter(hackathonItemAdapter);
        onStart();
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