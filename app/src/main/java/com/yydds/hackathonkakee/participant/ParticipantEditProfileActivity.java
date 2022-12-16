package com.yydds.hackathonkakee.participant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yydds.hackathonkakee.R;
import com.yydds.hackathonkakee.classes.Participant;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ParticipantEditProfileActivity extends AppCompatActivity {

    StorageReference storageReference;
    DocumentReference documentReference;

    EditText nameET, emailET, birthDateET, genderET, phoneET, insNameET, majorET, levelEduET, GPAET, interestFieldET, interestJobPosET;
    String participantID, resumeUrl = "", picUri = "", name, gender, phoneNumber, insName, major, levelEdu, interestField, interestJobPos;;
    Button uploadResumeBtn, saveBtn, birthDateBtn;
    ImageButton deleteResumeBtn, deletePicBtn;
    ImageView participantPicIV;
    Intent uploadedImageIntent;
    boolean picIsLoaded;
    DatePickerDialog datePickerDialog;
    Timestamp birthDate;
    double GPA;
    RadioGroup genderRG;
    RadioButton maleRB, femaleRB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_edit_profile);

        participantID = getIntent().getStringExtra("participantID");

        storageReference = FirebaseStorage.getInstance().getReference();
        documentReference = FirebaseFirestore.getInstance().collection("Participants").document(participantID);

        initComponents();
        initDatePicker();
        assignValue();
    }

    private void initComponents() {
//        nameET = findViewById(R.id.);
//        emailET = findViewById(R.id.);
//        birthDateET = findViewById(R.id.);
//        genderET = findViewById(R.id.);
//        phoneET = findViewById(R.id.);
//        insNameET = findViewById(R.id.);
//        majorET = .findViewById(R.id.);
//        levelEduET = findViewById(R.id.);
//        GPAET = findViewById(R.id.);
//        interestFieldET = findViewById(R.id.);
//        interestJobPosET = findViewById(R.id.);
//        uploadResumeBtn = findViewById(R.id.name)
//        participantPicIV = findViewById(R.id.name);
//        saveBtn = findViewById(R.id.);
//        genderRG = findViewById(R.id.);
//        maleRB = findViewById(R.id.)
//        femaleRB = findViewById(R.id.)

        participantPicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, 100);
            }
        });
        deleteResumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference reference = storageReference.child("participantResume/" + participantID + "resume.pdf");
                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ParticipantEditProfileActivity.this, "Resume deleted.", Toast.LENGTH_SHORT).show();
                        resumeUrl = "";
                    }
                });
            }
        });
        deletePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference reference = storageReference.child("participantProfilePic/" + participantID + ".png");
                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ParticipantEditProfileActivity.this, "Profile picture deleted.", Toast.LENGTH_SHORT).show();
                        picUri = "";
                        checkPicUrl();
                    }
                });
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveParticipantProfile();
            }
        });
        birthDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void assignValue() {
        DocumentReference df = FirebaseFirestore.getInstance().collection("participants").document(participantID);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Participant participant = documentSnapshot.toObject(Participant.class);
                nameET.setText(participant.getName());
                emailET.setText(participant.getEmail());
                birthDateET.setText(participant.getBirthDate().toString());
                genderET.setText(participant.getGender());
                phoneET.setText(participant.getPhoneNumber());
                insNameET.setText(participant.getInstitutionName());
                majorET.setText(participant.getFieldMajor());
                levelEduET.setText(participant.getLevelOfEducation());
                GPAET.setText(Double.toString(participant.getGPA()));
                interestFieldET.setText(participant.getInterestField());
                interestJobPosET.setText(participant.getInterestJobPos());
                resumeUrl = participant.getResumeUrl();
                picUri = participant.getProfilePicUrl();

                birthDateBtn.setText(new SimpleDateFormat("dd/MM/yyyy").format(participant.getBirthDate().toDate()));

                String genderFromDB = participant.getGender();
                if (maleRB.getText().toString().equals(genderFromDB)) maleRB.setChecked(true);
                else femaleRB.setChecked(true);
            }
        });
        picIsLoaded = false;

        checkResumeUrl();
        checkPicUrl();
    }

    private void saveParticipantProfile() {
        int selectedID = genderRG.getCheckedRadioButtonId();

        name = nameET.getText().toString();
        gender = ((RadioButton) findViewById(selectedID)).getText().toString();
        phoneNumber = phoneET.getText().toString();
        insName = insNameET.getText().toString();
        major = majorET.getText().toString();
        levelEdu = levelEduET.getText().toString();
        GPA = Double.parseDouble(GPAET.getText().toString());
        interestField = interestFieldET.getText().toString();
        interestJobPos = interestJobPosET.getText().toString();
        if (!validateInput(name)) return;
        uploadPicToFirebase();
    }

    private boolean validateInput(String name) {
        boolean isValid = true;
        if (name.isEmpty()) {
            nameET.setError("Name cannot be empty!");
            nameET.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void uploadPicToFirebase() {
        if (!picIsLoaded) return;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile picture is uploading......");
        progressDialog.show();

        StorageReference reference = storageReference.child("participantProfilePic/" + participantID + ".png");
        reference.putFile(uploadedImageIntent.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                picUri = uri.toString();
                progressDialog.dismiss();
                saveParticipantProfileToFirebase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ParticipantEditProfileActivity.this, "Upload profile picture failed.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading profile picture..." + (int) progress + "%");
            }
        });
    }

    private void saveParticipantProfileToFirebase() {
        Participant updatedParticipant = new Participant(name, picUri, resumeUrl, gender, phoneNumber, insName, major, levelEdu, interestField, interestJobPos, GPA, birthDate);
        documentReference.set(updatedParticipant).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ParticipantEditProfileActivity.this, "Update profile successfully.", Toast.LENGTH_SHORT).show();     
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ParticipantEditProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkResumeUrl() {
        if (resumeUrl.isEmpty()) {
            uploadResumeBtn.setText("Upload resume");
            deleteResumeBtn.setVisibility(View.INVISIBLE);
            uploadResumeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO upload resume
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);
                }
            });
        } else {
            uploadResumeBtn.setText("View resume");
            deleteResumeBtn.setVisibility(View.VISIBLE);
            uploadResumeBtn.setOnClickListener(new View.OnClickListener() {
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

    private void checkPicUrl() {
        if (picUri.isEmpty()) {
            deletePicBtn.setVisibility(View.INVISIBLE);
        } else {
            deletePicBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("File is loading......");
            progressDialog.show();

            StorageReference reference = storageReference.child("participantResume/" + participantID + "resume.pdf");
            reference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri uri = uriTask.getResult();
                    resumeUrl = uri.toString();
                    checkResumeUrl();
                    Toast.makeText(ParticipantEditProfileActivity.this, "Resume uploaded.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("File uploaded.." + (int) progress + "%");
                }
            });
        } else if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream inputStream = null;
            try {
                assert selectedImage != null;
                inputStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.decodeStream(inputStream);
            participantPicIV.setImageURI(selectedImage);
            uploadedImageIntent = data;
            picIsLoaded = true;
            deletePicBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;

                String date = makeDateString(day, month, year);
                try {
                    birthDate = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse( day +"/" + month + "/" + year));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                birthDateBtn.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        String monthString = "";
        if (month == 1) monthString = "JAN";
        else if (month == 2) monthString = "FEB";
        else if (month == 3) monthString = "MAR";
        else if (month == 4) monthString = "APR";
        else if (month == 5) monthString = "MAY";
        else if (month == 6) monthString = "JUN";
        else if (month == 7) monthString = "JUL";
        else if (month == 8) monthString = "AUG";
        else if (month == 9) monthString = "SEP";
        else if (month == 10) monthString = "OCT";
        else if (month == 11) monthString = "NOV";
        else if (month == 12) monthString = "DEC";
        return day + " " + monthString + " " + year;
    }
}

