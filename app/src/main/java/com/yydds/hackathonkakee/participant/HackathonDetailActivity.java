package com.yydds.hackathonkakee.participant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Hackathon;
import com.yydds.hackathonkakee.classes.Participant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HackathonDetailActivity extends AppCompatActivity {

    TextView hackathonNameTV, periodTV, venueTV, modeTV, prizePoolTV, maxTeamMembersTV, longDescTV, buttonInsTV, pageTitleTV;
    MaterialButton button, yesBtn, noBtn;
    ImageView backArrowIV;
    ShapeableImageView hackathonIconIV;
    String participantID, hackathonID, hackathonName;
    Hackathon hackathon;
    ArrayList<String> participantsIdList;
    FloatingActionButton shareBtn;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon_detail);

        participantID = getIntent().getStringExtra("participantID");
        hackathonID = getIntent().getStringExtra("hackathonID");
        hackathonName = getIntent().getStringExtra("hackathonName");

        initializeComponents();
        assignHackathonDetails();
    }

    private void initializeComponents() {
        hackathonNameTV = findViewById(R.id.hackathonNameTV);
        periodTV = findViewById(R.id.periodTV);
        venueTV = findViewById(R.id.venueTV);
        modeTV = findViewById(R.id.modeTV);
        prizePoolTV = findViewById(R.id.prizePoolTV);
        maxTeamMembersTV = findViewById(R.id.maxTeamMembersTV);
        longDescTV = findViewById(R.id.longDescTV);
        buttonInsTV = findViewById(R.id.buttonInsTV);
        pageTitleTV = findViewById(R.id.pageTitleTv);
        button = findViewById(R.id.button);
        backArrowIV = findViewById(R.id.backArrowIv);
        hackathonIconIV = findViewById(R.id.hackathonIconIV);
        shareBtn = findViewById(R.id.shareBtn);

        pageTitleTV.setText(hackathonName);

        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void assignHackathonDetails() {
        DocumentReference df = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hackathon = documentSnapshot.toObject(Hackathon.class);

                Picasso.get().load(hackathon.getIconUri()).into(hackathonIconIV);
                hackathonNameTV.setText(hackathon.getName());

                String startDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getStartDateTS().toDate());
                String endDate = new SimpleDateFormat("dd/MM/yyyy").format(hackathon.getEndDateTS().toDate());
                String period = startDate + " - " + endDate;
                periodTV.setText(period);
                venueTV.setText(hackathon.getMode().equals("Online") ? "-" : hackathon.getVenue());
                modeTV.setText(hackathon.getMode());
                prizePoolTV.setText("RM" + Integer.toString(hackathon.getPrizePool()));
                System.out.println(hackathon.getMaxTeamMembers());
                maxTeamMembersTV.setText(Integer.toString(hackathon.getMaxTeamMembers()) + " /group");
                longDescTV.setText(hackathon.getLongDesc());

                participantsIdList = hackathon.getParticipantsID();
                System.out.println(participantsIdList.contains(participantID));
                if (participantsIdList.contains(participantID)) {
                    button.setVisibility(View.GONE);
                    buttonInsTV.setText("You already register in " + hackathon.getName() + "!");
                } else {
                    button.setVisibility(View.VISIBLE);
                    button.setText("Register now!");
                    buttonInsTV.setText("Click the button below to register now!");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog = new Dialog(HackathonDetailActivity.this);
                            dialog.setContentView(R.layout.confirmation_pop_up);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                            }
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(false); //Optional
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                            TextView titleTV = dialog.findViewById(R.id.titleTV), contentTV = dialog.findViewById(R.id.contentTV);
                            titleTV.setText("Registration Confirmation");
                            contentTV.setText("Are you sure to register?");
                            yesBtn = dialog.findViewById(R.id.yesBtn);
                            noBtn = dialog.findViewById(R.id.noBtn);

                            dialog.show();

                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DocumentReference hackathonDF = FirebaseFirestore.getInstance().collection("Hackathons").document(hackathonID);
                                    ArrayList<String> newParticipantIdList = new ArrayList<>(hackathon.getParticipantsID());
                                    newParticipantIdList.add(participantID);
                                    hackathonDF.update("participantsID", newParticipantIdList);

                                    DocumentReference participantDF = FirebaseFirestore.getInstance().collection("Participants").document(participantID);
                                    participantDF.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Participant participant = documentSnapshot.toObject(Participant.class);
                                            ArrayList<String> newHackathonIdList = new ArrayList<>(participant.getParticipatedHackathonId());
                                            newHackathonIdList.add(hackathonID);
                                            participantDF.update("participatedHackathonId", newHackathonIdList);
                                            Toast.makeText(HackathonDetailActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                            });

                            noBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                        // type of the content to be shared
                        sharingIntent.setType("text/plain");

                        // Body of the content
                        String shareBody = "< " + hackathon.getName() + " >" + "\n\n" + hackathon.getShortDesc() + "\n\n" + "Come and join " + hackathon.getName() + " by downloading Hackathon Kakee mobile application!";

                        // subject of the content. you can share anything
                        String shareSubject = hackathon.getName();

                        // passing body of the content
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                        // passing subject of the content
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    }
                });
            }
        });
    }
}