package com.yydds.hackathonkakee.participant.hackathonDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.Participant;
import com.yydds.hackathonkakee.classes.Team;
import com.yydds.hackathonkakee.general.Utility;

import java.sql.Array;
import java.util.ArrayList;

public class CreateTeamActivity extends AppCompatActivity {

    final String[] CONTACT_METHOD = {"Whatsapp", "Facebook", "Discord", "Instagram", "Email", "Phone"};
    String participantID, hackathonID, hackathonName, teamID = "", participantName;
    String teamName, teamDesc, leaderContact, leaderContactMethod = "", teamVisibility = "", teamDocumentPath;

    TextInputLayout teamNameTIL, teamDescTIL, leaderContactTIL;
    TextInputEditText teamNameTIET, teamDescTIET, contactTIET;
    RadioGroup teamVisibilityRG;
    RadioButton publicRB, privateRB;
    MaterialButton saveBtn;
    TextView pageTitleTV, createTeamTV;
    ImageView backArrowIV;
    AutoCompleteTextView methodACTV;
    ArrayAdapter<String> adapterItems;
    Participant participant;
    DocumentReference participantDocumentReference;
    Team currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        participantID = getIntent().getStringExtra("participantID");
        hackathonID = getIntent().getStringExtra("hackathonID");
        hackathonName = getIntent().getStringExtra("hackathonName");
        teamID = getIntent().getStringExtra("teamID");
        System.out.println(teamID);

        initComponents();

        participantDocumentReference = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
        participantDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                participant = documentSnapshot.toObject(Participant.class);
                participantName = participant.getName();
            }
        });
    }

    private void initComponents() {
        teamNameTIL = findViewById(R.id.teamNameTIL);
        teamDescTIL = findViewById(R.id.teamDescTIL);
        leaderContactTIL = findViewById(R.id.contactTIL);
        teamNameTIET = findViewById(R.id.teamNameTIET);
        teamDescTIET = findViewById(R.id.teamDescTIET);
        contactTIET = findViewById(R.id.contactTIET);
        teamVisibilityRG = findViewById(R.id.teamVisibilityRG);
        publicRB = findViewById(R.id.publicRB);
        privateRB = findViewById(R.id.privateRB);
        saveBtn = findViewById(R.id.saveBtn);
        pageTitleTV = findViewById(R.id.pageTitleTv);
        backArrowIV = findViewById(R.id.backArrowIv);
        methodACTV = findViewById(R.id.methodACTV);
        createTeamTV = findViewById(R.id.createTeamTV);

        adapterItems = new ArrayAdapter<String>(this, R.layout.login_role_list_view, CONTACT_METHOD);
        methodACTV.setAdapter(adapterItems);
        methodACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                leaderContactMethod = adapterView.getItemAtPosition(i).toString();
            }
        });

        pageTitleTV.setText(hackathonName);
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        teamVisibilityRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                teamVisibility = radioButton.getText().toString();
            }
        });

        if (teamID != null && !teamID.isEmpty()) {
            createTeamTV.setText("Edit your team!");
            DocumentReference teamDocumentReference = FirebaseFirestore.getInstance().collection("Teams").document(teamID);
            teamDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    currentTeam = documentSnapshot.toObject(Team.class);
                    assignValueFromDB();
                }
            });
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateInput()) return;
                    saveTeam("update");
                }
            });
        } else {
            createTeamTV.setText("Let's create new team!");
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateInput()) return;
                    saveTeam("create");
                }
            });
        }
    }

    private boolean validateInput() {
        boolean isValid = true;
        teamName = teamNameTIET.getText().toString();
        teamDesc = teamDescTIET.getText().toString();
        leaderContact = contactTIET.getText().toString();

        if (teamName.isEmpty()) {
            teamNameTIL.setErrorEnabled(true);
            teamNameTIL.setError("Please enter team name.");
            isValid = false;
        } else {
            teamNameTIL.setErrorEnabled(false);
        }
        if (teamDesc.isEmpty()) {
            teamDescTIL.setErrorEnabled(true);
            teamDescTIL.setError("Please enter team description.");
            isValid = false;
        } else {
            teamDescTIL.setErrorEnabled(false);
        }
        if (leaderContact.isEmpty()) {
            leaderContactTIL.setErrorEnabled(true);
            leaderContactTIL.setError("Please enter team name.");
            isValid = false;
        } else {
            leaderContactTIL.setErrorEnabled(false);
        }
        if (leaderContactMethod.isEmpty()) {
            methodACTV.setError("Choose contact method.");
            isValid = false;
        }
        if (teamVisibility.isEmpty()) {
            Toast.makeText(this, "Please select team visibility", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void saveTeam(String operation) {
        Team teamToBeSaved;
        teamDocumentPath = "";
        if (operation.equals("create")){
            teamDocumentPath = Utility.generateTeamID();
            ArrayList<String> newMembersName = new ArrayList<>();
            newMembersName.add(participantName);
            ArrayList<String> newMembersID = new ArrayList<>();
            newMembersID.add(participantID);
            teamToBeSaved = new Team(teamName, hackathonID, teamDesc, teamVisibility, leaderContactMethod + " " + leaderContact, newMembersName, newMembersID);
        } else if (operation.equals("update")){
            teamDocumentPath = teamID;
            currentTeam.setTeamName(teamName);
            currentTeam.setTeamDescription(teamDesc);
            currentTeam.setTeamVisibility(teamVisibility);
            currentTeam.setLeaderContact(leaderContactMethod + " " + leaderContact);
            teamToBeSaved = currentTeam;
        } else return;
        DocumentReference teamDocumentReference = FirebaseFirestore.getInstance().collection("Teams").document(teamDocumentPath);
        teamDocumentReference.set(teamToBeSaved).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateTeamActivity.this, operation + " team successfully.", Toast.LENGTH_SHORT).show();
                if (operation.equals("create")) {
                    DocumentReference hackathonDocumentReference = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
                    hackathonDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Hackathon hackathon = documentSnapshot.toObject(Hackathon.class);
                            ArrayList<String> updatedParticipantsWithTeam = hackathon.getParticipantsWithTeam();
                            updatedParticipantsWithTeam.add(participantID);
                            hackathonDocumentReference.update("participantsWithTeam", updatedParticipantsWithTeam);
                            ArrayList<String> updatedTeamsID = hackathon.getTeamsID();
                            updatedTeamsID.add(teamDocumentPath);
                            hackathonDocumentReference.update("teamsID", updatedTeamsID);
                        }
                    });
                    ArrayList<String> updatedJoinedTeamID = participant.getJoinedTeamID();
                    updatedJoinedTeamID.add(teamDocumentPath);
                    participantDocumentReference.update("joinedTeamID", updatedJoinedTeamID);
                }
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTeamActivity.this, "failed to " + operation + " team.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void assignValueFromDB() {
        teamNameTIET.setText(currentTeam.getTeamName());
        teamDescTIET.setText(currentTeam.getTeamDescription());
        String contactString = currentTeam.getLeaderContact();
        String contactMethod = contactString.substring(0, contactString.indexOf(" "));
        String contact = contactString.substring(contactString.indexOf(" ") + 1);
        leaderContactMethod = contactMethod;
        methodACTV.setText(contactMethod, false);
        contactTIET.setText(contact);
        if (currentTeam.getTeamVisibility().equals(publicRB.getText())) publicRB.setChecked(true);
        else if (currentTeam.getTeamVisibility().equals(privateRB.getText())) privateRB.setChecked(true);
    }

}