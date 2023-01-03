package com.yydds.hackathonkakee.organizer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Announcement;
;
public class CreateAnnouncementActivity extends AppCompatActivity {
    ImageView backArrowIv;
    TextView pageTitleTv;
    TextInputLayout contentTIL, titleTIL;
    TextInputEditText contentTIET, titleTIET;
    MaterialButton saveAnnouncementBtn, deleteAnnouncementBtn;
    String announcementID = "", hackathonID = "";
    Announcement announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_create_announcement);
        announcementID = getIntent().getStringExtra("announcementID");
        hackathonID = getIntent().getStringExtra("hackathonID");

        initializeComponent();

        if (announcementID != null && !announcementID.isEmpty()) {
            pageTitleTv.setText("Edit Announcement");
            deleteAnnouncementBtn.setVisibility(View.VISIBLE);
            saveAnnouncementBtn.setText("Save Announcement");
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Announcements").document(announcementID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    announcement = documentSnapshot.toObject(Announcement.class);
                    initializeFieldForEditAnnouncement();
                }
            });
        } else {
            pageTitleTv.setText("Create Announcement");
            deleteAnnouncementBtn.setVisibility(View.GONE);
            saveAnnouncementBtn.setText("Create New Announcement");
        }
    }

    private void initializeComponent() {
        pageTitleTv = findViewById(R.id.pageTitleTv);
        backArrowIv = findViewById(R.id.backArrowIv);
        contentTIET = findViewById(R.id.contentTIET);
        contentTIL = findViewById(R.id.contentTIL);
        titleTIET = findViewById(R.id.titleTIET);
        titleTIL = findViewById(R.id.titleTIL);
        saveAnnouncementBtn = findViewById(R.id.saveNewsBtn);
        deleteAnnouncementBtn = findViewById(R.id.deleteNewsBtn);


        backArrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveAnnouncementBtn.setOnClickListener((v) -> saveAnnouncement());
        deleteAnnouncementBtn.setOnClickListener((v) -> deleteAnnouncement());
    }

    private void initializeFieldForEditAnnouncement() {
        titleTIET.setText(announcement.getTitle());
        contentTIET.setText(announcement.getContent());
    }

    private void saveAnnouncement() {
        String title = titleTIET.getText().toString();
        String content = contentTIET.getText().toString();
        if (!validateInput(title, content)) return;
        saveAnnouncementInFirebase(title, content);
    }

    private void saveAnnouncementInFirebase(String title, String content) {
        Announcement newAnnouncement;
        DocumentReference documentReference;
        if (announcementID != null && !announcementID.isEmpty()) {
            newAnnouncement = new Announcement(title, content, announcement.getHackathonID(), announcement.getTimestamp());
            documentReference = FirebaseFirestore.getInstance().collection("Announcements").document(announcementID);
        } else {
            newAnnouncement = new Announcement(title, content, hackathonID, Timestamp.now());
            documentReference = FirebaseFirestore.getInstance().collection("Announcements").document();
        }
        documentReference.set(newAnnouncement).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateAnnouncementActivity.this, "Save announcement successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAnnouncementActivity.this, "Failed to save announcement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAnnouncement() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Announcements").document(announcementID);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateAnnouncementActivity.this, "Delete announcement successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAnnouncementActivity.this, "Failed to delete announcement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String title, String content) {
        boolean isValid = true;
        if (title.isEmpty()) {
            titleTIL.setErrorEnabled(true);
            titleTIL.setError("Please enter title");
            isValid = false;
        } else {
            titleTIL.setErrorEnabled(false);
        }
        if (content.isEmpty()) {
            contentTIL.setErrorEnabled(true);
            contentTIL.setError("Please enter content");
            isValid = false;
        } else {
            contentTIL.setErrorEnabled(false);
        }
        return isValid;
    }

}