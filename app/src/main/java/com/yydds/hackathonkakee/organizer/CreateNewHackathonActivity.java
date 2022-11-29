package com.yydds.hackathonkakee.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.general.Utility;

public class CreateNewHackathonActivity extends AppCompatActivity {

    final String[] modes = {"Online", "Hybrid", "Physical"};
    String mode = "", hackathonName, hackathonVenue, shortDesc, longDesc;
    int prizePool, maxTeamMembers;

    String organizerID = "";

    TextView pageTitleTv;
    EditText hackathonNameEt, hackathonVenueEt, prizePoolEt, maxTeamMembersEt, shortDescEt, longDescEt;
    MaterialButton createHackathonBtn;
    ImageView backArrowIv;

    AutoCompleteTextView modeActv;

    ArrayAdapter<String> modeAdapterItems;

    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_hackathon);
        organizerID = getIntent().getStringExtra("organizerID");

        firebaseFirestore = FirebaseFirestore.getInstance();

        initializeComponents();
    }

    private void initializeComponents() {
        modeActv = findViewById(R.id.modeActv);
        hackathonNameEt = findViewById(R.id.hackathonNameEt);
        hackathonVenueEt = findViewById(R.id.hackathonVenueEt);
        prizePoolEt = findViewById(R.id.prizePoolEt);
        maxTeamMembersEt = findViewById(R.id.maxTeamMembersEt);
        shortDescEt = findViewById(R.id.shortDescEt);
        longDescEt = findViewById(R.id.longDescEt);
        backArrowIv = findViewById(R.id.backArrowIv);
        pageTitleTv = findViewById(R.id.pageTitleTv);
        createHackathonBtn = findViewById(R.id.createHackathonBtn);

        pageTitleTv.setText("Create New Hackathon");

        createHackathonBtn.setOnClickListener((v) -> createHackathon());

        modeAdapterItems = new ArrayAdapter<String>(this, R.layout.create_hackathon_mode_list_view, modes);
        modeActv.setAdapter(modeAdapterItems);
        modeActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mode = adapterView.getItemAtPosition(i).toString();
            }
        });

        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createHackathon() {
        hackathonName = hackathonNameEt.getText().toString();
        hackathonVenue = hackathonVenueEt.getText().toString();
        shortDesc = shortDescEt.getText().toString();
        longDesc = longDescEt.getText().toString();
        prizePool = 0;
        maxTeamMembers = 0;

        if (!validateInput(hackathonName, hackathonVenue, shortDesc, longDesc)) return;

        createHackathonInFirebase();

        //TODO create hackathon and save it in firebase
    }

    private void createHackathonInFirebase() {
        Hackathon hackathon = new Hackathon(hackathonName, shortDesc, longDesc, mode, hackathonVenue, organizerID, prizePool, maxTeamMembers);
        DocumentReference documentReference = firebaseFirestore.collection("Hackathons").document(Utility.generateID());
        documentReference.set(hackathon).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateNewHackathonActivity.this, "Create new hackathon successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateInput(String hackathonName, String hackathonLocation, String shortDesc, String longDesc) {
        boolean isValid = true;
        if (hackathonName.isEmpty()) {
            hackathonNameEt.setError("Please provide hackathon name.");
            hackathonNameEt.requestFocus();
            isValid = false;
        }
        if (hackathonLocation.isEmpty()) {
            hackathonVenueEt.setError("Please provide hackathon location.");
            hackathonVenueEt.requestFocus();
            isValid = false;
        }
        if (prizePoolEt.getText().toString().isEmpty()) {
            prizePoolEt.setError("Please provide prize pool.");
            prizePoolEt.requestFocus();
            isValid = false;
        } else {
            try {
                prizePool = Integer.parseInt(prizePoolEt.getText().toString());
            } catch (Exception e) {
                prizePoolEt.setError("Please enter amount of prize pool.");
                prizePoolEt.requestFocus();
                isValid = false;
            }
        }
        if (maxTeamMembersEt.getText().toString().isEmpty()) {
            maxTeamMembersEt.setError("Please provide max team members.");
            maxTeamMembersEt.requestFocus();
            isValid = false;
        } else {
            try {
                maxTeamMembers = Integer.parseInt(prizePoolEt.getText().toString());
            } catch (Exception e) {
                maxTeamMembersEt.setError("Please provide max team members in number.");
                maxTeamMembersEt.requestFocus();
                isValid = false;
            }
        }
        if (shortDesc.isEmpty()) {
            shortDescEt.setError("Please provide short description.");
            shortDescEt.requestFocus();
            isValid = false;
        }
        if (longDesc.isEmpty()) {
            longDescEt.setError("Please provide long description");
            longDescEt.requestFocus();
            isValid = false;
        }
        if (mode.isEmpty()) {
            modeActv.setError("Please choose mode.");
            modeActv.requestFocus();
            isValid = false;
        }
        return isValid;
    }
}